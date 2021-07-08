package in.jploft.esevak.ui.components.fragments.wallet;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import androidx.recyclerview.widget.DividerItemDecoration;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import in.jploft.esevak.R;
import in.jploft.esevak.databinding.DialogAddAmountBinding;
import in.jploft.esevak.databinding.FragmentWalletBinding;
import in.jploft.esevak.network.ApiResponse;
import in.jploft.esevak.pojo.TransactionHistoryData;
import in.jploft.esevak.pojo.WalletData;
import in.jploft.esevak.ui.base.BaseFragment;
import in.jploft.esevak.ui.components.activities.home.HomeActivity;
import in.jploft.esevak.ui.components.activities.paymentGatway.PaymentWebViewActivity;
import in.jploft.esevak.ui.components.adapters.TransHistoryAdapter;
import in.jploft.esevak.utils.Constants;
import in.jploft.esevak.utils.ProgressDialog;
import in.jploft.esevak.utils.Utility;

public class WalletFragment extends BaseFragment {
    private static final String TAG = WalletFragment.class.getName();
    private FragmentWalletBinding binding;
    private WalletViewModel viewModel;
    private View.OnClickListener onClickListener = null;
    private HomeActivity homeActivity;
    private ArrayList<TransactionHistoryData.Transaction> transactionData;
    private TransHistoryAdapter tranHistoryAdapter;


    @Override
    protected ViewDataBinding setBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wallet, container, false);
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
        transactionData = new ArrayList<>();
        walletAPI();
        tranHistoryAPI();
    }

    @Override
    public void onResume() {
        super.onResume();
        Utility.setAppBar(mActivity);
        binding.appBar.tvTitle.setText("Wallet");
        binding.appBar.ivCart.setVisibility(View.GONE);
    }


    @Override
    protected void initializeOnCreateObject() {
        homeActivity = (HomeActivity) getActivity();
        viewModel = new ViewModelProvider(this).get(WalletViewModel.class);
        viewModel.responseLiveData.observe(this, new Observer<ApiResponse<WalletData>>() {
            @Override
            public void onChanged(ApiResponse<WalletData> it) {
                handleResult(it);
            }
        });
        viewModel.responseTranHistoryLiveData.observe(this, new Observer<ApiResponse<TransactionHistoryData>>() {
            @Override
            public void onChanged(ApiResponse<TransactionHistoryData> it) {
                handleTranHistoryResult(it);
            }
        });
    }


    @Override
    protected void setListeners() {
        binding.appBar.ivBack.setOnClickListener(this);
        binding.linearAddMoneyWallet.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        Utility.hideKeyboard(mActivity);
        switch (view.getId()) {
            case R.id.ivBack:
                homeActivity.onBackPressed();
                break;
            case R.id.linearAddMoneyWallet:
                AddAmountDialog();
                break;
        }
    }

    private void AddAmountDialog() {
        final Dialog dialog = new Dialog(mActivity, R.style.Theme_Dialog);
        DialogAddAmountBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(mActivity), R.layout.dialog_add_amount, null, false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(dialogBinding.getRoot());


        dialogBinding.tvTitle.setText("Add Money to Wallet");
        dialogBinding.btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideKeyboard(mActivity);
                dialog.dismiss();
                if (!dialogBinding.edtAmount.getText().toString().isEmpty() && Long.parseLong(dialogBinding.edtAmount.getText().toString()) > 0) {
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "wallet");
                    bundle.putString("url", Constants.ADD_WALLET + sessionManager.getUserData().getId() + "/" + dialogBinding.edtAmount.getText().toString());
                    PaymentWebViewActivity.startActivity(getActivity(), bundle, false);
                } else {
                    Utility.showToastMessageError(mActivity, "Please enter amount");
                }
            }
        });
        dialogBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideKeyboard(mActivity);
                dialog.dismiss();
            }
        });

        dialog.show();
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

    private void handleResult(ApiResponse<WalletData> result) {
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

                    binding.tvMoney.setText("Rs. " + result.getData().getWallet());

                } else {
                    Utility.showToastMessageError(mActivity, result.getData().getMessage());
                }
                break;
        }
    }

    private void tranHistoryAPI() {
        String userId = sessionManager.getUserData().getId();
        if (userId != null && !userId.equals("")) {

            HashMap<String, String> reqData = new HashMap<>();
            reqData.put("userId", userId);

            Log.e(TAG, "Api parameters - " + reqData.toString());
            viewModel.transactionHistoryApi(reqData);
        }
    }

    private void handleTranHistoryResult(ApiResponse<TransactionHistoryData> result) {
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
                    binding.rvTransHistory.setVisibility(View.VISIBLE);
                    binding.layoutError.rlerror.setVisibility(View.GONE);

                    if (result.getData().getData().getTransactions() != null && !result.getData().getData().getTransactions().isEmpty()) {
                        binding.rvTransHistory.setVisibility(View.VISIBLE);
                        binding.layoutError.rlerror.setVisibility(View.GONE);
                        transactionData.clear();
                        transactionData.addAll(result.getData().getData().getTransactions());
                        if (transactionData.size() != 0) {
                            tranHistoryAdapter = new TransHistoryAdapter(mActivity, onClickListener, transactionData);
                            binding.rvTransHistory.setAdapter(tranHistoryAdapter);
                            binding.rvTransHistory.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
                        }
                    } else {
                        binding.rvTransHistory.setVisibility(View.GONE);
                        binding.layoutError.rlerror.setVisibility(View.VISIBLE);
                    }
                } else {
                    binding.rvTransHistory.setVisibility(View.GONE);
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