package in.jploft.esevak.ui.components.activities.forgotPassword;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.UiAutomation;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;

import java.util.HashMap;

import in.jploft.esevak.R;
import in.jploft.esevak.databinding.ActivityForgotPassBinding;
import in.jploft.esevak.network.ApiResponse;
import in.jploft.esevak.pojo.ForgotPassData;
import in.jploft.esevak.ui.base.BaseBindingActivity;
import in.jploft.esevak.ui.components.activities.resetPassword.ResetPassActivity;
import in.jploft.esevak.utils.Constants;
import in.jploft.esevak.utils.ProgressDialog;
import in.jploft.esevak.utils.Utility;

public class ForgotPassActivity extends BaseBindingActivity {
    private static final String TAG = ForgotPassActivity.class.getName();
    private ActivityForgotPassBinding binding;
    private ForgotPassViewModel viewModel = null;


    @Override
    protected void setBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_pass);
        viewModel = new ViewModelProvider(this).get(ForgotPassViewModel.class);
        binding.setLifecycleOwner(this);
    }

    @Override
    protected void createActivityObject(@Nullable Bundle savedInstanceState) {
        mActivity = this;
    }

    @SuppressLint("ResourceType")
    @Override
    protected void initializeObject() {
        viewModel.responseLiveData.observe(this, new Observer<ApiResponse<ForgotPassData>>() {
            @Override
            public void onChanged(ApiResponse<ForgotPassData> it) {
                handleResult(it);
            }
        });
    }

    @Override
    protected void setListeners() {
        binding.btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Utility.hideKeyboard(mActivity);
        switch (view.getId()) {
            case R.id.btnSubmit:
                forgotPassApi();
                break;
        }
    }

    private void forgotPassApi() {
        String mobile = binding.edtEmialPhone.getText().toString().trim();

        if (viewModel.isValidFormData(mActivity, mobile)) {

            HashMap<String, String> reqData = new HashMap<>();
            reqData.put("phone", mobile);

            Log.e(TAG, "Api parameters - " + reqData.toString());
            viewModel.forgotPass(reqData);
        }
    }

    private void handleResult(ApiResponse<ForgotPassData> result) {
        switch (result.getStatus()) {
            case ERROR:
                ProgressDialog.hideProgressDialog();
                Utility.showToastMessageError(mActivity, result.getError().getMessage());
                Log.e(TAG, "error - " + result.getError().getMessage());
                break;
            case LOADING:
                ProgressDialog.showProgressDialog(this);
                break;
            case SUCCESS:
                ProgressDialog.hideProgressDialog();
                    Log.e(TAG, "Response - " + new Gson().toJson(result));
                    if (result.getData().getStatusCode() == Constants.Success) {

                        Bundle bundle = new Bundle();
                        bundle.putString("userId",result.getData().getData().getUserId());
                        bundle.putString("otp",result.getData().getData().getOtp());
                        bundle.putString("mobile",binding.edtEmialPhone.getText().toString().trim());
                        ResetPassActivity.startActivity(mActivity,bundle,false);
                        finish();

                    } else {
                        Utility.showToastMessageError(mActivity, result.getData().getMessage());
                    }

                break;
        }
    }


    public static void startActivity(Activity activity, Bundle bundle, boolean isClear) {
        Intent intent = new Intent(activity, ForgotPassActivity.class);
        if (bundle != null) intent.putExtra("bundle", bundle);
        if (isClear)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    @Override
    public void onDestroy() {
        viewModel.disposeSubscriber();
        super.onDestroy();
    }
}
