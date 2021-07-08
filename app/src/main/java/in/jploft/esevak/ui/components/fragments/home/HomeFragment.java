package in.jploft.esevak.ui.components.fragments.home;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import in.jploft.esevak.R;
import in.jploft.esevak.databinding.FragmentHomeBinding;
import in.jploft.esevak.network.ApiResponse;
import in.jploft.esevak.pojo.AllCityData;
import in.jploft.esevak.pojo.DashboardData;
import in.jploft.esevak.pojo.UpdateCityData;
import in.jploft.esevak.ui.base.BaseFragment;
import in.jploft.esevak.ui.components.activities.home.HomeActivity;
import in.jploft.esevak.ui.components.adapters.BannerAdapter;
import in.jploft.esevak.ui.components.adapters.CategoryAdapter;
import in.jploft.esevak.ui.components.adapters.OfferAdapter;
import in.jploft.esevak.ui.components.fragments.allCategory.AllCategoryFragment;
import in.jploft.esevak.ui.components.fragments.subCategory.SubCategoryFragment;
import in.jploft.esevak.utils.Constants;
import in.jploft.esevak.utils.ProgressDialog;
import in.jploft.esevak.utils.SessionManager;
import in.jploft.esevak.utils.SingleShotLocationProvider;
import in.jploft.esevak.utils.Utility;

import static in.jploft.esevak.utils.SingleShotLocationProvider.GPSCoordinates;
import static in.jploft.esevak.utils.SingleShotLocationProvider.requestSingleUpdate;

public class HomeFragment extends BaseFragment {
    private static final String TAG = HomeFragment.class.getName();
    private FragmentHomeBinding binding;
    private HomeViewModel viewModel = null;
    private View.OnClickListener onClickListener = null;
    private HomeActivity homeActivity;
    private ArrayList<DashboardData.Banner> bannerData;
    private BannerAdapter bannerAdapter;
    private ArrayList<DashboardData.Category> categoryData;
    private CategoryAdapter categoryAdapter;
    private ArrayList<DashboardData.Offer> offerData;
    private OfferAdapter offerAdapter;
    private ArrayList<AllCityData.Data.CitiesItem> allCityData;


    @Override
    protected ViewDataBinding setBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        onClickListener = this;
        binding.setLifecycleOwner(this);
        return binding;
    }

    @Override
    protected void createActivityObject() {
        mActivity = (AppCompatActivity) getActivity();
    }

    @Override
    protected void initializeObject() {
        sessionManager = new SessionManager();
        allCityData = new ArrayList<>();
        bannerData = new ArrayList<>();
        categoryData = new ArrayList<>();
        offerData = new ArrayList<>();
        if (Utility.isDeviceOnline(mActivity)) {
            dashboardAPI();
            viewModel.allCityApi();
        } else {
            Utility.showToastMessageError(mActivity, getString(R.string.no_internet));
        }
        binding.swipeToRefresh.setColorSchemeResources(R.color.color_FF653B);
        binding.swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utility.isDeviceOnline(mActivity)) {
                    dashboardAPI();
                } else {
                    Utility.showToastMessageError(mActivity, getString(R.string.no_internet));
                }
                binding.swipeToRefresh.setRefreshing(false);
            }
        });
        checkWhetherLocationSettingsAreSatisfied();
    }

    private void checkWhetherLocationSettingsAreSatisfied() {

        LocationRequest mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000)
                .setNumUpdates(2);

        final LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        builder.setNeedBle(true);
        SettingsClient client = LocationServices.getSettingsClient(mActivity);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(mActivity, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Log.w(TAG, "onSuccess() called with: locationSettingsResponse = [" + locationSettingsResponse + "]");
                //hasLocationPermission();


                try {
                    requestSingleUpdate(mActivity, new SingleShotLocationProvider.LocationCallback() {
                        @Override
                        public void onNewLocationAvailable(GPSCoordinates location) {
                            Log.e(TAG, "my location is - " + location.latitude + " " + location.longitude);
                            if (!SessionManager.getInstance(mActivity).isSELECT_LOCATION()) {
                                sessionManager.setLOCATION(Utility.getCompleteAddressString(mActivity, location.latitude, location.longitude));
                                sessionManager.setLATITUDE(String.valueOf(location.latitude));
                                sessionManager.setLONGITUDE(String.valueOf(location.longitude));
                                HomeActivity.txtAddress.setText(sessionManager.getLOCATION());
                                HomeActivity.txtAddress.setSelected(true);
                            }
                            Log.e(TAG, "location - " + sessionManager.getLOCATION());
                        }
                    });

                } catch (Exception e) {
                    Log.e(TAG, "error - " + e.getMessage());
                }

            }
        });
        task.addOnFailureListener(mActivity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception error) {
                Log.d(TAG, "onSuccess --> onFailure() called with: e = [" + error + "]");
                if (error instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) error;
                        resolvable.startResolutionForResult(mActivity, HomeActivity.REQUEST_CHECK_SETTINGS);

                    } catch (IntentSender.SendIntentException e) {

                        e.printStackTrace();
                    }
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Toolbar toolbar = mActivity.findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        HomeActivity.txtAddress.setText(sessionManager.getLOCATION());
        HomeActivity.txtAddress.setSelected(true);
    }


    @Override
    protected void initializeOnCreateObject() {
        homeActivity = (HomeActivity) getActivity();
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        viewModel.responseLiveData.observe(this, new Observer<ApiResponse<DashboardData>>() {
            @Override
            public void onChanged(ApiResponse<DashboardData> it) {
                handleResult(it);
            }
        });
        viewModel.responseAllCityLiveData.observe(this, new Observer<ApiResponse<AllCityData>>() {
            @Override
            public void onChanged(ApiResponse<AllCityData> it) {
                handleAllCityResult(it);
            }
        });
        viewModel.responseUpdateCityLiveData.observe(this, new Observer<ApiResponse<UpdateCityData>>() {
            @Override
            public void onChanged(ApiResponse<UpdateCityData> it) {
                handleUpdateCityResult(it);
            }
        });
    }


    @Override
    protected void setListeners() {
        binding.tvViewAll.setOnClickListener(this);
        LinearLayout linearLocation = getActivity().findViewById(R.id.linearLocation);
        linearLocation.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvViewAll:
                homeActivity.changeFragment(new AllCategoryFragment(), true);
                break;
            case R.id.rowCategoryItem:
                int position = (int) view.getTag();
                categoryAdapter.selectedPos = position;
                Bundle bundle = new Bundle();
                bundle.putString("catId", categoryData.get(position).getId());
                bundle.putString("catName", categoryData.get(position).getName());
                bundle.putString("catImage", categoryData.get(position).getImage());

                homeActivity.changeFragment(new SubCategoryFragment(), true, bundle);
                break;
            case R.id.linearLocation:
                showAllCityDialog();
                break;
        }
    }

    private void showAllCityDialog() {
        if (allCityData.size() != 0) {
            ArrayList<String> a1 = new ArrayList<>();

            for (int i = 0; i < allCityData.size(); i++) {
                a1.add(allCityData.get(i).getName());
            }
            String[] stockArr = a1.toArray(new String[a1.size()]);

            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setTitle("Select location");
            builder.setItems(stockArr, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Toast.makeText(mActivity, "Position: " + which + " Value: " + listItems[which], Toast.LENGTH_LONG).show();
                    sessionManager.setLOCATION(stockArr[which]);
                    HomeActivity.txtAddress.setText(sessionManager.getLOCATION());
                    HomeActivity.txtAddress.setSelected(true);
                    sessionManager.setSELECT_LOCATION();
                    Log.e(TAG, "city Id - " + allCityData.get(which).getId());
                    updateCityApi(allCityData.get(which).getId(),sessionManager.getUserData().getId());
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            Utility.showToastMessageError(mActivity, "No Data available!");
        }
    }

    private void updateCityApi(String cityId, String userId) {
        if (cityId != null && !cityId.equals("") && userId != null && !userId.equals("")) {

            HashMap<String, String> reqData = new HashMap<>();
            reqData.put("cityId", cityId);
            reqData.put("userId", userId);

            Log.e(TAG, "Api parameters - " + reqData.toString());
            viewModel.updateCityApi(reqData);
        }
    }

    private void handleUpdateCityResult(ApiResponse<UpdateCityData> result) {
        switch (result.getStatus()) {
            case ERROR:
                ProgressDialog.hideProgressDialog();
                Utility.showToastMessageError(mActivity, result.getError().getMessage());
                Log.e(TAG, "error - " + result.getError().getMessage());
                break;
            case LOADING:
                ProgressDialog.showProgressDialog(mActivity);
                break;
            case SUCCESS:
                ProgressDialog.hideProgressDialog();
                if (result.getData().getStatusCode() == Constants.Success) {
                    Log.e(TAG, "Response - " + new Gson().toJson(result));

                    dashboardAPI();

                } else {
                }
                break;
        }
    }

    private void dashboardAPI() {
        String userId = sessionManager.getUserData().getId();
        if (userId != null && !userId.equals("")) {

            HashMap<String, String> reqData = new HashMap<>();
            reqData.put("userId", userId);

            Log.e(TAG, "Api parameters - " + reqData.toString());
            viewModel.dashboardApi(reqData);
        }
    }

    private void handleResult(ApiResponse<DashboardData> result) {
        switch (result.getStatus()) {
            case ERROR:
                ProgressDialog.hideProgressDialog();
                Utility.showToastMessageError(mActivity, result.getError().getMessage());
                Log.e(TAG, "error - " + result.getError().getMessage());
                break;
            case LOADING:
                ProgressDialog.showProgressDialog(mActivity);
                break;
            case SUCCESS:
                ProgressDialog.hideProgressDialog();
                if (result.getData().getStatusCode() == Constants.Success) {
                    Log.e(TAG, "Response - " + new Gson().toJson(result));
                    binding.linearDashboard.setVisibility(View.VISIBLE);
                    binding.layoutError.rlerror.setVisibility(View.GONE);

                    setDashboardData(result.getData().getData());

                } else {
                    binding.linearDashboard.setVisibility(View.GONE);
                    binding.layoutError.rlerror.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void handleAllCityResult(ApiResponse<AllCityData> result) {
        switch (result.getStatus()) {
            case ERROR:
                ProgressDialog.hideProgressDialog();
                Utility.showToastMessageError(mActivity, result.getError().getMessage());
                Log.e(TAG, "error - " + result.getError().getMessage());
                break;
            case LOADING:
                ProgressDialog.showProgressDialog(mActivity);
                break;
            case SUCCESS:
                ProgressDialog.hideProgressDialog();
                if (result.getData().getStatusCode() == Constants.Success) {
                    Log.e(TAG, "Response - " + new Gson().toJson(result));
                    binding.linearDashboard.setVisibility(View.VISIBLE);
                    binding.layoutError.rlerror.setVisibility(View.GONE);

                    Log.e(TAG, "Response Location - " + new Gson().toJson(result));

                    if (result.getData().getData().getCities() != null && !result.getData().getData().getCities().isEmpty()) {
                        allCityData.clear();
                        allCityData.addAll(result.getData().getData().getCities());
                    } else {
                        Utility.showToastMessageError(mActivity, result.getData().getMessage());
                    }

                } else {
                    Utility.showToastMessageError(mActivity, result.getData().getMessage());
                }
                break;
        }
    }

    private void setDashboardData(DashboardData.Data data) {
        /*set Banner Data*/
        if (data.getBanners() != null && !data.getBanners().isEmpty()) {
            binding.linearDashboard.setVisibility(View.VISIBLE);
            binding.layoutError.rlerror.setVisibility(View.GONE);
            bannerData.clear();
            bannerData.addAll(data.getBanners());
            if (bannerData.size() != 0) {
                bannerAdapter = new BannerAdapter(mActivity, onClickListener, bannerData);
                binding.viewPager.setAdapter(bannerAdapter);
                binding.dotsIndicator.setViewPager(binding.viewPager);
            }
        } else {
            binding.linearDashboard.setVisibility(View.GONE);
            binding.layoutError.rlerror.setVisibility(View.VISIBLE);
        }


        /*set Category Data*/
        if (data.getCategory() != null && !data.getCategory().isEmpty()) {
            binding.linearDashboard.setVisibility(View.VISIBLE);
            binding.layoutError.rlerror.setVisibility(View.GONE);
            categoryData.clear();
            categoryData.addAll(data.getCategory());
            if (categoryData.size() != 0) {
                categoryAdapter = new CategoryAdapter(mActivity, onClickListener, categoryData);
                binding.rvCategory.setAdapter(categoryAdapter);
            }
        } else {
            binding.linearDashboard.setVisibility(View.GONE);
            binding.layoutError.rlerror.setVisibility(View.VISIBLE);
        }
        /*set Offer Data*/
        if (data.getOffers() != null && !data.getOffers().isEmpty()) {
            binding.linearDashboard.setVisibility(View.VISIBLE);
            binding.layoutError.rlerror.setVisibility(View.GONE);
            offerData.clear();
            offerData.addAll(data.getOffers());
            if (offerData.size() != 0) {
                offerAdapter = new OfferAdapter(mActivity, onClickListener, offerData);
                //binding.rvOffer.setSlideOnFling(true);
                binding.rvOffer.setAdapter(offerAdapter);
            }
        } else {
            binding.linearDashboard.setVisibility(View.GONE);
            binding.layoutError.rlerror.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        viewModel.disposeSubscriber();
        super.onDestroy();
    }

}