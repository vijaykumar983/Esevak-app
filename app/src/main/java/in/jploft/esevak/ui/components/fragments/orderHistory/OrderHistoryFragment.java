package in.jploft.esevak.ui.components.fragments.orderHistory;

import android.annotation.SuppressLint;
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
import in.jploft.esevak.databinding.FragmentOrderHistoryBinding;
import in.jploft.esevak.network.ApiResponse;
import in.jploft.esevak.pojo.MyOrdersData;
import in.jploft.esevak.ui.base.BaseFragment;
import in.jploft.esevak.ui.components.activities.home.HomeActivity;
import in.jploft.esevak.ui.components.adapters.OrderHistoryAdapter;
import in.jploft.esevak.utils.Constants;
import in.jploft.esevak.utils.ProgressDialog;
import in.jploft.esevak.utils.Utility;

public class OrderHistoryFragment extends BaseFragment {
    private static final String TAG = OrderHistoryFragment.class.getName();
    private FragmentOrderHistoryBinding binding;
    private OrderHistoryViewModel viewModel;
    private View.OnClickListener onClickListener = null;
    private HomeActivity homeActivity;
    private ArrayList<MyOrdersData.Order> myOrdersData;
    private OrderHistoryAdapter orderHistoryAdapter;


    @Override
    protected ViewDataBinding setBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_history, container, false);
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
        myOrdersData = new ArrayList<>();
        myOrdersAPI();
    }

    @Override
    public void onResume() {
        super.onResume();
        Utility.setAppBar(mActivity);
        binding.appBar.tvTitle.setText("Order History");
        binding.appBar.ivCart.setVisibility(View.GONE);
    }


    @Override
    protected void initializeOnCreateObject() {
        homeActivity = (HomeActivity) getActivity();
        viewModel = new ViewModelProvider(this).get(OrderHistoryViewModel.class);
        viewModel.responseLiveData.observe(this, new Observer<ApiResponse<MyOrdersData>>() {
            @Override
            public void onChanged(ApiResponse<MyOrdersData> it) {
                handleResult(it);
            }
        });
    }


    @Override
    protected void setListeners() {
        binding.appBar.ivBack.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                homeActivity.onBackPressed();
                break;
        }
    }

    private void myOrdersAPI() {
        String userId = sessionManager.getUserData().getId();
        if (userId != null && !userId.equals("")) {

            HashMap<String, String> reqData = new HashMap<>();
            reqData.put("userId", userId);

            Log.e(TAG, "Api parameters - " + reqData.toString());
            viewModel.myOrdersApi(reqData);
        }
    }

    private void handleResult(ApiResponse<MyOrdersData> result) {
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
                    binding.rvOrderHistory.setVisibility(View.VISIBLE);
                    binding.layoutError.rlerror.setVisibility(View.GONE);

                    if (result.getData().getData().getOrders() != null && !result.getData().getData().getOrders().isEmpty()) {
                        binding.rvOrderHistory.setVisibility(View.VISIBLE);
                        binding.layoutError.rlerror.setVisibility(View.GONE);
                        myOrdersData.clear();
                        myOrdersData.addAll(result.getData().getData().getOrders());
                        if (myOrdersData.size() != 0) {
                            orderHistoryAdapter = new OrderHistoryAdapter(mActivity, onClickListener, myOrdersData);
                            binding.rvOrderHistory.setAdapter(orderHistoryAdapter);
                        }
                    } else {
                        binding.rvOrderHistory.setVisibility(View.GONE);
                        binding.layoutError.rlerror.setVisibility(View.VISIBLE);
                    }

                } else {
                    binding.rvOrderHistory.setVisibility(View.GONE);
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