package in.jploft.esevak.ui.components.activities.home;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

import in.jploft.esevak.R;
import in.jploft.esevak.databinding.ActivityHomeBinding;
import in.jploft.esevak.databinding.DialogLogoutBinding;
import in.jploft.esevak.databinding.DialogSuccessBinding;
import in.jploft.esevak.pojo.DrawerData;
import in.jploft.esevak.ui.base.BaseBindingActivity;
import in.jploft.esevak.ui.components.activities.loginSignup.LoginSignupActivity;
import in.jploft.esevak.ui.components.adapters.DrawerAdapter;
import in.jploft.esevak.ui.components.fragments.allCategory.AllCategoryFragment;
import in.jploft.esevak.ui.components.fragments.cart.CartFragment;
import in.jploft.esevak.ui.components.fragments.feedback.FeedbackFragment;
import in.jploft.esevak.ui.components.fragments.home.HomeFragment;
import in.jploft.esevak.ui.components.fragments.orderHistory.OrderHistoryFragment;
import in.jploft.esevak.ui.components.fragments.profile.ProfileFragment;
import in.jploft.esevak.ui.components.fragments.support.SupportFragment;
import in.jploft.esevak.ui.components.fragments.wallet.WalletFragment;
import in.jploft.esevak.utils.SessionManager;
import in.jploft.esevak.utils.SingleShotLocationProvider;
import in.jploft.esevak.utils.Utility;

import static in.jploft.esevak.utils.SingleShotLocationProvider.requestSingleUpdate;

public class HomeActivity extends BaseBindingActivity {
    private static final String TAG = HomeActivity.class.getName();
    private ActivityHomeBinding binding;
    private ActionBarDrawerToggle toggle;
    private ArrayList<DrawerData> list = null;
    private DrawerAdapter adapter = null;
    private View.OnClickListener onClickListener = null;
    private SessionManager sessionManager;
    public static AppCompatTextView txtuserName, tvEmail, txtAddress;
    public static CircularImageView ivUserProfile, ivHeaderProfile;

    public static final int REQUEST_CHECK_SETTINGS = 123;


    @Override
    protected void setBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        onClickListener = this;
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }


    @Override
    protected void initializeObject() {
        sessionManager = new SessionManager();
        initView();
        setActionBar();
        setUserData();
        replaceFragment(new HomeFragment(), null);

        setDrawerAdapter();
    }

    private void initView() {
        ivUserProfile = binding.navHeader.ivUserProfile;
        ivHeaderProfile = binding.layoutMain.appBar.ivHeaderProfile;
        txtuserName = binding.navHeader.txtuserName;
        tvEmail = binding.navHeader.tvEmail;
        txtAddress = binding.layoutMain.appBar.txtAddress;
    }

    private void setUserData() {
        Utility.loadImage(binding.navHeader.ivUserProfile, sessionManager.getUserData().getProfileImage());
        Utility.loadImage(binding.layoutMain.appBar.ivHeaderProfile, sessionManager.getUserData().getProfileImage());
        binding.navHeader.txtuserName.setText(sessionManager.getUserData().getFullname());
        binding.navHeader.tvEmail.setText(sessionManager.getUserData().getEmail());
    }

    private void setDrawerAdapter() {
        list = new ArrayList<>();
        String[] mTestArray = getResources().getStringArray(R.array.drawerNames);

        for (int i = 0; i < mTestArray.length; i++) {
            list.add(new DrawerData(i, mTestArray[i]));
        }

        adapter = new DrawerAdapter(mActivity, list, onClickListener);
        binding.recyclerView.setAdapter(adapter);
    }

    private void setActionBar() {
        toggle = new ActionBarDrawerToggle(this, binding.drawerLayout,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.layoutMain.appBar.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Utility.hideKeyboard(mActivity);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Utility.hideKeyboard(mActivity);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };
        toggle.setDrawerIndicatorEnabled(false);


        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu);

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                } else binding.drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    protected void setListeners() {
        binding.navHeader.imvClose.setOnClickListener(this);
        binding.layoutMain.appBar.ivHeaderProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_main: {
                int position = (int) view.getTag();
                adapter.selectedPos = position;
                //adapter.notifyDataSetChanged();


                switch (position) {
                    case 0:
                        binding.drawerLayout.closeDrawer(GravityCompat.START);
                        replaceFragment(new HomeFragment(), null);
                        return;
                    case 1:
                        binding.drawerLayout.closeDrawer(GravityCompat.START);
                        replaceFragment(new AllCategoryFragment(), null);
                        return;
                    case 2:
                        binding.drawerLayout.closeDrawer(GravityCompat.START);
                        replaceFragment(new CartFragment(), null);
                        return;
                    case 3:
                        binding.drawerLayout.closeDrawer(GravityCompat.START);
                        replaceFragment(new WalletFragment(), null);
                        return;
                    case 4:
                        binding.drawerLayout.closeDrawer(GravityCompat.START);
                        replaceFragment(new OrderHistoryFragment(), null);
                        return;
                    case 5:
                        binding.drawerLayout.closeDrawer(GravityCompat.START);
                        replaceFragment(new SupportFragment(), null);
                        return;
                    case 6:
                        binding.drawerLayout.closeDrawer(GravityCompat.START);
                        replaceFragment(new ProfileFragment(), null);
                        return;
                    case 7:
                        binding.drawerLayout.closeDrawer(GravityCompat.START);
                        replaceFragment(new FeedbackFragment(), null);
                        return;
                    case 8:
                        binding.drawerLayout.closeDrawer(GravityCompat.START);
                        logOutDialog();
                        return;
                }
                binding.drawerLayout.closeDrawer(GravityCompat.START);
            }
            break;
            case R.id.imvClose:
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.ivHeaderProfile:
                replaceFragment(new ProfileFragment(), null);
                break;
        }
    }

    private void logOutDialog() {
        final Dialog dialog = new Dialog(this, R.style.Theme_Dialog);
        DialogLogoutBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(mActivity), R.layout.dialog_logout, null, false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(dialogBinding.getRoot());


        dialogBinding.tvTitle.setText("Logout");
        dialogBinding.tvMessage.setText("Are you sure, you want to logout?");
        dialogBinding.btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialogBinding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                sessionManager.logout();
                LoginSignupActivity.startActivity(mActivity, null, true);
                finish();
            }
        });

        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CHECK_SETTINGS) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                try {
                    requestSingleUpdate(mActivity, new SingleShotLocationProvider.LocationCallback() {
                        @Override
                        public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
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

            } else {
                //User clicks No
                buildAlertMessageNoGps();
            }
        }

    }

    private void buildAlertMessageNoGps() {
        final Dialog dialog = new Dialog(this, R.style.Theme_Dialog);
        DialogSuccessBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(mActivity), R.layout.dialog_success, null, false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(dialogBinding.getRoot());

        dialogBinding.tvTitle.setText("GPS Settings");
        dialogBinding.tvMessage.setText("GPS is not enabled. Please goto settings page to enable");
        dialogBinding.tvOk.setText("Settings");
        dialogBinding.llOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                finish();
            }
        });

        dialog.show();
    }


    public static void startActivity(Activity activity, Bundle bundle, boolean isClear) {
        Intent intent = new Intent(activity, HomeActivity.class);
        if (bundle != null) intent.putExtra("bundle", bundle);
        if (isClear)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                finish();
            } else {
                super.onBackPressed();
            }
        }
    }
}
