package in.jploft.esevak.ui.components.activities.paymentGatway;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import in.jploft.esevak.R;
import in.jploft.esevak.databinding.ActivityPaymenttSuccessBinding;
import in.jploft.esevak.ui.base.BaseBindingActivity;
import in.jploft.esevak.ui.components.activities.home.HomeActivity;

public class PaymentSuccessActivity extends BaseBindingActivity {
    private static final String TAG = PaymentSuccessActivity.class.getName();
    private ActivityPaymenttSuccessBinding binding;


    @Override
    protected void setBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_paymentt_success);
    }

    @Override
    protected void createActivityObject(Bundle savedInstanceState) {
        mActivity = this;
    }

    @Override
    protected void initializeObject() {

        if (getIntent().getStringExtra("tag").equals("1")) {
            binding.ivimage.setVisibility(View.VISIBLE);
            binding.ivimage1.setVisibility(View.GONE);
        } else {
            binding.ivimage.setVisibility(View.GONE);
            binding.ivimage1.setVisibility(View.VISIBLE);
            binding.ivimage.setBackground(getResources().getDrawable(R.drawable.paymentfaild));
        }

    }

    @Override
    protected void setListeners() {
        binding.btnHome.setOnClickListener(this);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        HomeActivity.startActivity(mActivity, null, true);
        finish();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnHome:
                HomeActivity.startActivity(mActivity, null, true);
                finish();
                break;
        }
    }

    public static void startActivity(Activity activity, Bundle bundle, boolean isClear) {
        Intent intent = new Intent(activity, PaymentSuccessActivity.class);
        if (bundle != null) intent.putExtra("bundle", bundle);
        if (isClear)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }
}
