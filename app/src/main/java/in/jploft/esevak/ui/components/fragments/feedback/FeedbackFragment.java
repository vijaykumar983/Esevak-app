package in.jploft.esevak.ui.components.fragments.feedback;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;

import java.util.HashMap;

import in.jploft.esevak.R;
import in.jploft.esevak.databinding.DialogSuccessBinding;
import in.jploft.esevak.databinding.FragmentFeedbackBinding;
import in.jploft.esevak.network.ApiResponse;
import in.jploft.esevak.pojo.FeedbackData;
import in.jploft.esevak.ui.base.BaseFragment;
import in.jploft.esevak.ui.components.activities.home.HomeActivity;
import in.jploft.esevak.utils.Constants;
import in.jploft.esevak.utils.EmojiExcludeFilter;
import in.jploft.esevak.utils.ProgressDialog;
import in.jploft.esevak.utils.SessionManager;
import in.jploft.esevak.utils.Utility;

public class FeedbackFragment extends BaseFragment {
    private static final String TAG = FeedbackFragment.class.getName();
    private FragmentFeedbackBinding binding;
    private FeedbackViewModel viewModel;
    private View.OnClickListener onClickListener = null;
    private HomeActivity homeActivity;
    private SessionManager sessionManager;


    @Override
    protected ViewDataBinding setBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_feedback, container, false);
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
        binding.edtDescription.setFilters(new InputFilter[]{new EmojiExcludeFilter()});
        binding.txtuserName.setText(sessionManager.getUserData().getFullname());
        Utility.loadImage(binding.ivProfile, sessionManager.getUserData().getProfileImage());
    }

    @Override
    public void onResume() {
        super.onResume();
        Utility.setAppBar(mActivity);
        binding.appBar.tvTitle.setText("Feedback");
        binding.appBar.ivCart.setVisibility(View.GONE);
    }


    @Override
    protected void initializeOnCreateObject() {
        homeActivity = (HomeActivity) getActivity();
        viewModel = new ViewModelProvider(this).get(FeedbackViewModel.class);
        viewModel.responseLiveData.observe(this, new Observer<ApiResponse<FeedbackData>>() {
            @Override
            public void onChanged(ApiResponse<FeedbackData> it) {
                handleResult(it);
            }
        });
    }


    @Override
    protected void setListeners() {
        binding.appBar.ivBack.setOnClickListener(this);
        binding.btnSubmitFeedback.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                homeActivity.onBackPressed();
                break;
            case R.id.btnSubmitFeedback:
                if (Utility.isDeviceOnline(mActivity)) {
                    feedbackAPI();
                } else {
                    Utility.showToastMessageError(mActivity, getString(R.string.no_internet));
                }
                break;
        }
    }

    private void feedbackAPI() {
        String description = binding.edtDescription.getText().toString().trim();

        if (viewModel.isValidFormData(mActivity, description)) {

            HashMap<String, String> reqData = new HashMap<>();
            reqData.put("rating", String.valueOf(binding.myRatingBar.getRating()));
            reqData.put("user", sessionManager.getUserData().getFullname());
            reqData.put("description", description);


            Log.e(TAG, "Api parameters - " + reqData.toString());
            viewModel.feedbackApi(reqData);
        }
    }

    private void handleResult(ApiResponse<FeedbackData> result) {
        switch (result.getStatus()) {
            case ERROR:
                ProgressDialog.hideProgressDialog();
                Utility.showSnackBarMsgError(mActivity, result.getError().getMessage());
                Log.e(TAG, "error - " + result.getError().getMessage());
                break;
            case LOADING:
                ProgressDialog.showProgressDialog(mActivity);
                break;
            case SUCCESS:
                ProgressDialog.hideProgressDialog();
                Log.e(TAG, "Response - " + new Gson().toJson(result));
                if (result.getData().getStatusCode().equals("200")) {

                    showSuccessfullyDialog();

                } else {
                    Utility.showToastMessageError(mActivity, result.getData().getMessage());
                }
                break;
        }
    }

    private void showSuccessfullyDialog() {
        final Dialog dialog = new Dialog(mActivity, R.style.Theme_Dialog);
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
        dialogBinding.tvMessage.setText("Your feedback has been successfully submitted.");
        dialogBinding.llOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                mActivity.onBackPressed();
            }
        });

        dialog.show();
    }

    @Override
    public void onDestroy() {
        viewModel.disposeSubscriber();
        super.onDestroy();
    }
}