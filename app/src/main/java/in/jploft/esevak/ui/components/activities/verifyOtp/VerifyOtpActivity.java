package in.jploft.esevak.ui.components.activities.verifyOtp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;

import java.util.HashMap;

import in.jploft.esevak.R;
import in.jploft.esevak.databinding.ActivityVerifyOtpBinding;
import in.jploft.esevak.databinding.DialogSuccessBinding;
import in.jploft.esevak.network.ApiResponse;
import in.jploft.esevak.pojo.ResendOtpData;
import in.jploft.esevak.pojo.UserData;
import in.jploft.esevak.ui.base.BaseBindingActivity;
import in.jploft.esevak.ui.components.activities.createProfile.UpdateProfileActivity;
import in.jploft.esevak.utils.Constants;
import in.jploft.esevak.utils.ProgressDialog;
import in.jploft.esevak.utils.SessionManager;
import in.jploft.esevak.utils.Utility;


public class VerifyOtpActivity extends BaseBindingActivity {
    private static final String TAG = VerifyOtpActivity.class.getName();
    private ActivityVerifyOtpBinding binding;
    private VerifyOtpViewModel viewModel;
    private Bundle mBundle;
    private String otp = "", userId = "", mobile = "";
    private SessionManager sessionManager;


    @Override
    protected void setBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verify_otp);
        viewModel = new ViewModelProvider(this).get(VerifyOtpViewModel.class);
        binding.setLifecycleOwner(this);
    }

    @Override
    protected void createActivityObject(@Nullable Bundle savedInstanceState) {
        mActivity = this;
    }

    @SuppressLint("ResourceType")
    @Override
    protected void initializeObject() {
        sessionManager = new SessionManager();
        getIntentData();
        viewModel.responseLiveData.observe(this, new Observer<ApiResponse<UserData>>() {
            @Override
            public void onChanged(ApiResponse<UserData> it) {
                handleResult(it);
            }
        });
        viewModel.responseResendLiveData.observe(this, new Observer<ApiResponse<ResendOtpData>>() {
            @Override
            public void onChanged(ApiResponse<ResendOtpData> it) {
                handleResendOtpResult(it);
            }
        });
        setOtp(otp);

        onTextChange(binding.edtOne, binding.edtOne, binding.edtTwo);
        onTextChange(binding.edtOne, binding.edtTwo, binding.edtThree);
        onTextChange(binding.edtTwo, binding.edtThree, binding.edtFour);
        onTextChange(binding.edtThree, binding.edtFour, binding.edtFive);
        onTextChange(binding.edtFour, binding.edtFive, binding.edtFive);

    }

    private void getIntentData() {
        mBundle = getIntent().getBundleExtra("bundle");
        if (mBundle != null) {
            otp = mBundle.getString("otp");
            userId = mBundle.getString("userId");
            mobile = mBundle.getString("mobile");
        }
    }

    private void setOtp(String otp) {
        Log.w(TAG, "otp - " + otp);

        if (otp != null) {
            binding.edtOne.setText(String.valueOf(otp.charAt(0)));
            binding.edtTwo.setText(String.valueOf(otp.charAt(1)));
            binding.edtThree.setText(String.valueOf(otp.charAt(2)));
            binding.edtFour.setText(String.valueOf(otp.charAt(3)));
            binding.edtFive.setText(String.valueOf(otp.charAt(4)));
        }
    }


    private void onTextChange(AppCompatEditText edtFirst, AppCompatEditText edtCurrent, AppCompatEditText edtLast) {

        edtCurrent.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() != KeyEvent.ACTION_DOWN)
                    return false;
                if (i == KeyEvent.KEYCODE_DEL) {
                    if (TextUtils.isEmpty(edtCurrent.getText().toString())) {
                        edtFirst.setText("");
                        edtFirst.requestFocus();
                    }
                }

                return false;
            }
        });
        edtCurrent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edtCurrent.length() > 0)
                    edtLast.requestFocus();
                else
                    edtCurrent.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }


    @Override
    protected void setListeners() {
        binding.btnVerifyOtp.setOnClickListener(this);
        binding.tvResendOtp.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        Utility.hideKeyboard(mActivity);
        switch (view.getId()) {
            case R.id.btnVerifyOtp:
                verifyOTPApi();
                break;
            case R.id.tvResendOtp:
                resendOtpApi();
                break;
        }
    }

    private void resendOtpApi() {
        if (userId != null && !userId.equals("")) {

            HashMap<String, String> reqData = new HashMap<>();
            reqData.put("user_id", userId);

            Log.e(TAG, "Api parameters - " + reqData.toString());
            viewModel.resendOtp(reqData);
        }
    }

    private void handleResendOtpResult(ApiResponse<ResendOtpData> result) {
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

                        setOtp(result.getData().getData().getOtp());
                        otp = result.getData().getData().getOtp();
                        userId = result.getData().getData().getUserId();
                        showResendOtpDialog();

                    } else {
                        Utility.showToastMessageError(mActivity, result.getData().getMessage());
                    }
                break;
        }
    }

    private void verifyOTPApi() {
        String edt1 = binding.edtOne.getText().toString().trim();
        String edt2 = binding.edtTwo.getText().toString().trim();
        String edt3 = binding.edtThree.getText().toString().trim();
        String edt4 = binding.edtFour.getText().toString().trim();
        String edt5 = binding.edtFive.getText().toString().trim();

        if (viewModel.isValidFormData(mActivity, edt1, edt2, edt3, edt4, edt5)) {
            otp=edt1+edt2+edt3+edt4+edt5;

            HashMap<String, String> reqData = new HashMap<>();
            reqData.put("user_id", userId);
            reqData.put("otp", otp);

            Log.e(TAG, "Api parameters - " + reqData.toString());
            viewModel.verifyOtp(reqData);
        }
    }

    private void handleResult(ApiResponse<UserData> result) {
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

                        sessionManager.setUserData(result.getData().getData());
                        UpdateProfileActivity.startActivity(mActivity, null, false);

                    } else {
                        Utility.showToastMessageError(mActivity, result.getData().getMessage());
                    }
                break;
        }
    }

    private void showResendOtpDialog() {
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
        Intent intent = new Intent(activity, VerifyOtpActivity.class);
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
