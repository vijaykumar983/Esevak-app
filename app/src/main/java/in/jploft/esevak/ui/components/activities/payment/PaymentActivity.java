package in.jploft.esevak.ui.components.activities.payment;

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

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.UUID;

import in.jploft.esevak.R;
import in.jploft.esevak.databinding.ActivityPaymentBinding;
import in.jploft.esevak.databinding.DialogThankYouBinding;
import in.jploft.esevak.network.ApiResponse;
import in.jploft.esevak.pojo.PlaceOrderData;
import in.jploft.esevak.pojo.WalletData;
import in.jploft.esevak.ui.base.BaseBindingActivity;
import in.jploft.esevak.ui.components.activities.home.HomeActivity;
import in.jploft.esevak.ui.components.activities.paymentGatway.PaymentWebViewActivity;
import in.jploft.esevak.utils.Constants;
import in.jploft.esevak.utils.ProgressDialog;
import in.jploft.esevak.utils.SessionManager;
import in.jploft.esevak.utils.Utility;

public class PaymentActivity extends BaseBindingActivity  {
    private static final String TAG = PaymentActivity.class.getName();
    private ActivityPaymentBinding binding;
    private PaymentViewModel viewModel = null;
    private SessionManager sessionManager;
    private Bundle bundle;
    private String applyCoupon = "", finalTotalPrice = "", walletBalance = "0",day="",time="";


    @Override
    protected void setBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment);
        viewModel = new ViewModelProvider(this).get(PaymentViewModel.class);
        binding.setLifecycleOwner(this);
    }

    @Override
    protected void createActivityObject(@Nullable Bundle savedInstanceState) {
        mActivity = this;
    }

    @SuppressLint("ResourceType")
    @Override
    protected void initializeObject() {
        binding.appBar.tvTitle.setText("Payment");
        binding.appBar.ivCart.setVisibility(View.GONE);
        sessionManager = new SessionManager();
        getIntentData();
        viewModel.responseLiveData.observe(this, new Observer<ApiResponse<PlaceOrderData>>() {
            @Override
            public void onChanged(ApiResponse<PlaceOrderData> it) {
                handleResult(it);
            }
        });
        viewModel.responseWalletLiveData.observe(this, new Observer<ApiResponse<WalletData>>() {
            @Override
            public void onChanged(ApiResponse<WalletData> it) {
                handleWalletResult(it);
            }
        });
        walletAPI();
    }

    private void getIntentData() {
        bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            finalTotalPrice = bundle.getString("finalTotalPrice");
            applyCoupon = bundle.getString("ApplyCoupon");
            day = bundle.getString("day");
            time = bundle.getString("time");
        }
    }


    @Override
    protected void setListeners() {
        binding.appBar.ivBack.setOnClickListener(this);
        binding.linearPaytm.setOnClickListener(this);
        binding.linearWallet.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        Utility.hideKeyboard(mActivity);
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.linearPaytm:
                binding.ivPaytmCheck.setVisibility(View.VISIBLE);
                binding.ivWalletCheck.setVisibility(View.INVISIBLE);
                Bundle bundle = new Bundle();
                bundle.putString("type", "payment");
                bundle.putString("url", Constants.PAYMENT_ORDER + generateOrderId() + "/" + sessionManager.getUserData().getId() + "/" +
                        finalTotalPrice + "/" + Utility.commaRemove(sessionManager.getLOCATION()) + "/1/" + applyCoupon+"/"+day+"/"+time);
                PaymentWebViewActivity.startActivity(mActivity, bundle, false);
                break;
            case R.id.linearWallet:
                binding.ivWalletCheck.setVisibility(View.VISIBLE);
                binding.ivPaytmCheck.setVisibility(View.INVISIBLE);
                if (Double.parseDouble(walletBalance) >= Double.parseDouble(finalTotalPrice)) {
                    //placeOrderAPI("wallet");
                    placeOrderAPI();
                } else {
                    Utility.showToastMessageError(mActivity, " Insufficient balance in your wallet");
                    binding.ivWalletCheck.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }

    private String generateOrderId() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }

    private void walletAPI() {
        String userId = sessionManager.getUserData().getId();
        if (userId != null && !userId.equals("")) {

            HashMap<String, String> reqData = new HashMap<>();
            reqData.put("userId", userId);

            Log.e(TAG, "Api parameters - " + reqData.toString());
            viewModel.walletApi(reqData);
        }
    }

    private void handleWalletResult(ApiResponse<WalletData> result) {
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

                    walletBalance = result.getData().getWallet();

                } else {
                    Utility.showToastMessageError(mActivity, result.getData().getMessage());
                }
                break;
        }
    }

   /* private void placeOrderAPI(String paymentMode) {
        if (!sessionManager.getUserData().getId().isEmpty() && !sessionManager.getUserData().getId().equals("") &&
                !sessionManager.getLOCATION().isEmpty() && !sessionManager.getLOCATION().equals("")
                && !finalTotalPrice.isEmpty() && !Objects.equals(finalTotalPrice, "")) {

            HashMap<String, String> reqData = new HashMap<>();
            reqData.put("userId", sessionManager.getUserData().getId());
            reqData.put("address", sessionManager.getLOCATION());
            reqData.put("code", applyCoupon);
            reqData.put("totalAmount", finalTotalPrice);
            reqData.put("paymentMode", paymentMode);

            Log.e(TAG, "Api parameters - " + reqData.toString());
            viewModel.placeOrderApi(reqData);
        }
    }

    private void handleResult(ApiResponse<PlaceOrderData> result) {
        Utility.hideKeyboard(mActivity);
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
                if (result.getData().getStatusCode().equals("200")) {
                    Log.e(TAG, "Response - " + new Gson().toJson(result));

                    //thankYouDialog();

                } else {
                    Utility.showToastMessageError(mActivity, result.getData().getMessage());
                }
                break;
        }
    }*/

    private void placeOrderAPI() {
        String url = Constants.PAYMENT_ORDER + generateOrderId() + "/" + sessionManager.getUserData().getId() + "/" +
                finalTotalPrice + "/" + Utility.commaRemove(sessionManager.getLOCATION()) + "/0/" + applyCoupon+"/"+day+"/"+time;
        Log.e(TAG, "Api parameters - " + url);
        viewModel.placeOrderApi(url);
    }

    private void handleResult(ApiResponse<PlaceOrderData> result) {
        Utility.hideKeyboard(mActivity);
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
                if (result.getData().getStatusCode().equals("200")) {
                    Log.e(TAG, "Response - " + new Gson().toJson(result));

                    thankYouDialog();

                } else {
                    Utility.showToastMessageError(mActivity, result.getData().getMessage());
                }
                break;
        }
    }

    private void thankYouDialog() {
        final Dialog dialog = new Dialog(mActivity, R.style.Theme_Dialog);
        DialogThankYouBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(mActivity), R.layout.dialog_thank_you, null, false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialogBinding.ivLogo.setVisibility(View.VISIBLE);
        dialogBinding.ivLogo1.setVisibility(View.GONE);
        dialogBinding.tvOk.setText("HOME");

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(dialogBinding.getRoot());
        dialog.setCancelable(false);

        dialogBinding.tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                HomeActivity.startActivity(mActivity, null, true);
                finish();
            }
        });
        dialog.show();
    }


    public static void startActivity(Activity activity, Bundle bundle, boolean isClear) {
        Intent intent = new Intent(activity, PaymentActivity.class);
        if (bundle != null) intent.putExtra("bundle", bundle);
        if (isClear)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        binding.ivPaytmCheck.setVisibility(View.INVISIBLE);
        binding.ivWalletCheck.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroy() {
        viewModel.disposeSubscriber();
        super.onDestroy();
    }
}
