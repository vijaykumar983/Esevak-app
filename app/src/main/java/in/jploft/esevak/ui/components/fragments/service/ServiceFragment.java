package in.jploft.esevak.ui.components.fragments.service;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import in.jploft.esevak.R;
import in.jploft.esevak.databinding.FragmentServiceBinding;
import in.jploft.esevak.network.ApiResponse;
import in.jploft.esevak.pojo.ServiceData;
import in.jploft.esevak.ui.base.BaseFragment;
import in.jploft.esevak.ui.components.activities.home.HomeActivity;
import in.jploft.esevak.ui.components.adapters.ProductAdapter;
import in.jploft.esevak.ui.components.fragments.cart.CartFragment;
import in.jploft.esevak.ui.components.fragments.productDetail.ProductDetailFragment;
import in.jploft.esevak.utils.Constants;
import in.jploft.esevak.utils.ProgressDialog;
import in.jploft.esevak.utils.SessionManager;
import in.jploft.esevak.utils.Utility;

public class ServiceFragment extends BaseFragment {
    private static final String TAG = ServiceFragment.class.getName();
    private FragmentServiceBinding binding;
    private ServiceViewModel viewModel;
    private View.OnClickListener onClickListener = null;
    private HomeActivity homeActivity;
    private SessionManager sessionManager;
    private Bundle bundle;
    private String subCatId = "", subCatName = "", subCatImage = "";
    private ArrayList<ServiceData.Product> productData;
    private ProductAdapter productAdapter;


    @Override
    protected ViewDataBinding setBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_service, container, false);
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
        productData = new ArrayList<>();
        getIntentData();
        serviceAPI();
    }

    private void getIntentData() {
        bundle = this.getArguments();
        if (bundle != null) {
            subCatId = bundle.getString("subCatId");
            subCatName = bundle.getString("subCatName");
            subCatImage = bundle.getString("subCatImage");
            //Utility.loadPicture(binding.imgBanner, Constants.imageUrl + catImage);
            Utility.loadPicture(binding.imgBanner, subCatImage);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Utility.setAppBar(mActivity);
        binding.appBar.tvTitle.setText(Utility.toTitleCase(subCatName));
        binding.appBar.tvTitle.setSelected(true);
    }


    @Override
    protected void initializeOnCreateObject() {
        homeActivity = (HomeActivity) getActivity();
        viewModel = new ViewModelProvider(this).get(ServiceViewModel.class);
        viewModel.responseLiveData.observe(this, new Observer<ApiResponse<ServiceData>>() {
            @Override
            public void onChanged(ApiResponse<ServiceData> it) {
                handleResult(it);
            }
        });
    }


    @Override
    protected void setListeners() {
        binding.appBar.ivBack.setOnClickListener(this);
        binding.appBar.ivCart.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rowProductItem:
                int position = (int) view.getTag();
                productAdapter.selectedPos = position;
                Bundle bundle = new Bundle();
                bundle.putString("productId", productData.get(position).getId());
                bundle.putString("productName", productData.get(position).getName());
                bundle.putString("productImage", productData.get(position).getImage());

                homeActivity.changeFragment(new ProductDetailFragment(), true, bundle);
                break;
            case R.id.ivBack:
                homeActivity.onBackPressed();
                break;
            case R.id.ivCart:
                homeActivity.changeFragment(new CartFragment(), true);
                break;
        }
    }

    private void serviceAPI() {
        String userId = sessionManager.getUserData().getId();
        if (userId != null && !userId.equals("") && subCatId != null && !subCatId.equals("")) {

            HashMap<String, String> reqData = new HashMap<>();
            reqData.put("userId", userId);
            reqData.put("catid", subCatId);
            reqData.put("lat", sessionManager.getLATITUDE());
            reqData.put("lng", sessionManager.getLONGITUDE());

            Log.e(TAG, "Service Api parameters - " + reqData.toString());
            viewModel.serviceApi(reqData);
        }
    }

    private void handleResult(ApiResponse<ServiceData> result) {
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
                    binding.rvProduct.setVisibility(View.VISIBLE);
                    binding.layoutError.rlerror.setVisibility(View.GONE);

                    if (result.getData().getData().getProduct() != null && !result.getData().getData().getProduct().isEmpty()) {
                        binding.rvProduct.setVisibility(View.VISIBLE);
                        binding.layoutError.rlerror.setVisibility(View.GONE);
                        productData.clear();
                        productData.addAll(result.getData().getData().getProduct());
                        if (productData.size() != 0) {
                            productAdapter = new ProductAdapter(mActivity, onClickListener, productData);
                            binding.rvProduct.setAdapter(productAdapter);
                        }
                    } else {
                        binding.rvProduct.setVisibility(View.GONE);
                        binding.layoutError.rlerror.setVisibility(View.VISIBLE);
                    }

                } else {
                    binding.rvProduct.setVisibility(View.GONE);
                    binding.layoutError.rlerror.setVisibility(View.VISIBLE);
                }
                break;
        }
    }


    @Override
    public void onDestroy() {
        viewModel.disposeSubscriber();
        super.onDestroy();
    }
}