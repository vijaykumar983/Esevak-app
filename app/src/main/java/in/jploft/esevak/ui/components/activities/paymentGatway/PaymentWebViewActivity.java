package in.jploft.esevak.ui.components.activities.paymentGatway;

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
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.databinding.DataBindingUtil;

import in.jploft.esevak.R;
import in.jploft.esevak.databinding.ActivityPaymentWebviewBinding;
import in.jploft.esevak.databinding.DialogThankYouBinding;
import in.jploft.esevak.ui.base.BaseBindingActivity;
import in.jploft.esevak.ui.components.activities.home.HomeActivity;
import in.jploft.esevak.utils.Constants;

public class PaymentWebViewActivity extends BaseBindingActivity {
    private static final String TAG = PaymentWebViewActivity.class.getName();
    private ActivityPaymentWebviewBinding binding;
    private Bundle bundle;
    private String payment_url = "", type = "";

    @Override
    protected void setBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_webview);
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @Override
    protected void initializeObject() {
        getIntentData();
        setWebView();
    }

    private void getIntentData() {
        bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            payment_url = bundle.getString("url");
            type = bundle.getString("type");
        }
        Log.e(TAG, "url - " + payment_url);
    }

    @Override
    protected void setListeners() {

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setWebView() {
        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.getSettings().setLoadWithOverviewMode(true);
        binding.webview.getSettings().setUseWideViewPort(true);
        binding.webview.getSettings().setBuiltInZoomControls(true);
        binding.webview.setWebViewClient(new WebViewClient());
        binding.webview.loadUrl(payment_url);

        binding.webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView wView, String url) {
                Log.e(TAG, "success/failure url - " + url);
                if (url.equals(Constants.PAYMENT_SUCCESS)) //check if that's a url you want to load internally
                {
                    //startActivity(new Intent(PaymentWebViewActivity.this, PaymentSuccessActivity.class).putExtra("tag", "1"));
                    if (type.equals("payment")) {
                        successFailedDialog("Thank you for your order", "You can see you order in \n 'Order history' section.", "1");
                    } else {
                        successFailedDialog("Success", "Amount added in wallet", "1");
                    }

                } else if (url.equals(Constants.PAYMENT_FAILURE)) {
                    //startActivity(new Intent(PaymentWebViewActivity.this, PaymentSuccessActivity.class).putExtra("tag", "2"));
                    if (type.equals("payment")) {
                        successFailedDialog("Payment Failed", "Your payment was processed unsuccessfully.", "0");
                    } else {
                        successFailedDialog("Failed", "Amount not added in wallet", "0");
                    }
                }
                return false;
            }
        });
    }

    private void successFailedDialog(String title, String description, String status) {
        final Dialog dialog = new Dialog(mActivity, R.style.Theme_Dialog);
        DialogThankYouBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(mActivity), R.layout.dialog_thank_you, null, false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialogBinding.tvTitle.setText(title);
        dialogBinding.tvDescription.setText(description);
        dialogBinding.tvOk.setText("HOME");
        if (type.equals("wallet")) {
            dialogBinding.ivLogo1.setVisibility(View.VISIBLE);
            dialogBinding.ivLogo.setVisibility(View.GONE);
        } else {
            if (status.equals("1")) {
                dialogBinding.ivLogo.setVisibility(View.VISIBLE);
                dialogBinding.ivLogo1.setVisibility(View.GONE);
                dialogBinding.ivLogo.setImageResource(R.drawable.ic_thanks_order);
            } else {
                dialogBinding.ivLogo1.setVisibility(View.VISIBLE);
                dialogBinding.ivLogo.setVisibility(View.GONE);
                dialogBinding.ivLogo1.setImageResource(R.drawable.ic_failed);
            }
        }
        if (status.equals("1")) {
            dialogBinding.ivLogo1.setImageResource(R.drawable.ic_success);
        } else {
            dialogBinding.ivLogo1.setImageResource(R.drawable.ic_failed);
        }
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(dialogBinding.getRoot());

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public static void startActivity(Activity activity, Bundle bundle, boolean isClear) {
        Intent intent = new Intent(activity, PaymentWebViewActivity.class);
        if (bundle != null) intent.putExtra("bundle", bundle);
        if (isClear)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }
}
