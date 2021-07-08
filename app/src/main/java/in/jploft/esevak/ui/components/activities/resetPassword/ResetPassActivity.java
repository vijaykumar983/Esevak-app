package in.jploft.esevak.ui.components.activities.resetPassword;

import android.annotation.SuppressLint;
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
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;

import java.util.HashMap;

import in.jploft.esevak.R;
import in.jploft.esevak.databinding.ActivityResetPassBinding;
import in.jploft.esevak.databinding.DialogSuccessBinding;
import in.jploft.esevak.network.ApiResponse;
import in.jploft.esevak.pojo.ForgotPassData;
import in.jploft.esevak.pojo.ResetPassData;
import in.jploft.esevak.ui.base.BaseBindingActivity;
import in.jploft.esevak.ui.components.activities.loginSignup.LoginSignupActivity;
import in.jploft.esevak.utils.Constants;
import in.jploft.esevak.utils.ProgressDialog;
import in.jploft.esevak.utils.Utility;

public class ResetPassActivity extends BaseBindingActivity {
    private static final String TAG = ResetPassActivity.class.getName();
    private ActivityResetPassBinding binding;
    private ResetPassViewModel viewModel = null;
    private Bundle mBundle;
    private String otp="",userId="",mobile="";


    @Override
    protected void setBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reset_pass);
        viewModel = new ViewModelProvider(this).get(ResetPassViewModel.class);
        binding.setLifecycleOwner(this);
    }

    @Override
    protected void createActivityObject(@Nullable Bundle savedInstanceState) {
        mActivity = this;
    }

    @SuppressLint("ResourceType")
    @Override
    protected void initializeObject() {
        getIntentData();
        viewModel.responseLiveData.observe(this, new Observer<ApiResponse<ResetPassData>>() {
            @Override
            public void onChanged(ApiResponse<ResetPassData> it) {
                handleResult(it);
            }
        });
        viewModel.responseResendLiveData.observe(this, new Observer<ApiResponse<ForgotPassData>>() {
            @Override
            public void onChanged(ApiResponse<ForgotPassData> it) {
                handleResendOtpResult(it);
            }
        });
    }



    private void getIntentData() {
        mBundle = getIntent().getBundleExtra("bundle");
        if(mBundle!=null)
        {
           otp = mBundle.getString("otp");
           userId = mBundle.getString("userId");
           mobile = mBundle.getString("mobile");
           binding.edtOTP.setText(otp);
        }
    }

    @Override
    protected void setListeners() {
        binding.btnSubmit.setOnClickListener(this);
        binding.tvResendOtp.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        Utility.hideKeyboard(mActivity);
        switch (view.getId()) {
            case R.id.btnSubmit:
                resetPassApi();
                break;
            case R.id.tvResendOtp:
                resendOtpApi();
                break;
        }
    }

    private void resendOtpApi() {
        if (mobile!=null && !mobile.equals("")) {

            HashMap<String, String> reqData = new HashMap<>();
            reqData.put("phone", mobile);

            Log.e(TAG, "Api parameters - " + reqData.toString());
            viewModel.resendOtp(reqData);
        }
    }

    private void handleResendOtpResult(ApiResponse<ForgotPassData> result) {
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

                        binding.edtOTP.setText(result.getData().getData().getOtp());
                        otp = result.getData().getData().getOtp();
                        userId = result.getData().getData().getUserId();
                        showResendOtpDialog();

                    } else {
                        Utility.showToastMessageError(mActivity, result.getData().getMessage());
                    }
                break;
        }
    }

    private void resetPassApi() {
        String otp = binding.edtOTP.getText().toString().trim();
        String pass = binding.edtNewPass.getText().toString().trim();
        String confirmPass = binding.edtConfirmPass.getText().toString().trim();

        if (viewModel.isValidFormData(mActivity, otp,pass,confirmPass)) {

            HashMap<String, String> reqData = new HashMap<>();
            reqData.put("user_id", userId);
            reqData.put("password", confirmPass);
            reqData.put("otp", otp);

            Log.e(TAG, "Api parameters - " + reqData.toString());
            viewModel.resetPass(reqData);
        }
    }

    private void handleResult(ApiResponse<ResetPassData> result) {
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

                        showSuccessDialog();
                    } else {
                        Utility.showToastMessageError(mActivity, result.getData().getMessage());
                    }
                break;
        }
    }

    private void showSuccessDialog() {
        final Dialog dialog= new Dialog(this,R.style.Theme_Dialog);
        DialogSuccessBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(mActivity), R.layout.dialog_success, null, false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(dialogBinding.getRoot());

        dialogBinding.tvTitle.setText("Successfully");
        dialogBinding.tvMessage.setText("Your password successfully reset.");
        dialogBinding.llOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                LoginSignupActivity.startActivity(mActivity,null,true);
                finish();
            }
        });

        dialog.show();
    }

    private void showResendOtpDialog() {
        final Dialog dialog= new Dialog(this,R.style.Theme_Dialog);
        DialogSuccessBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(mActivity), R.layout.dialog_success, null, false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(dialogBinding.getRoot());

        dialogBinding.tvTitle.setText("Resend OTP");
        dialogBinding.tvMessage.setText("Your new One Time Pin(OTP) will be SMS to you");
        dialogBinding.llOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    public static void startActivity(Activity activity, Bundle bundle, boolean isClear) {
        Intent intent = new Intent(activity, ResetPassActivity.class);
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
