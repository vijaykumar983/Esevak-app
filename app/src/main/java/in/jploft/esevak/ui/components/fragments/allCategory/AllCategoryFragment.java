package in.jploft.esevak.ui.components.fragments.allCategory;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
import in.jploft.esevak.databinding.FragmentAllCateogryBinding;
import in.jploft.esevak.network.ApiResponse;
import in.jploft.esevak.pojo.AllCategoryData;
import in.jploft.esevak.ui.base.BaseFragment;

import in.jploft.esevak.ui.components.activities.home.HomeActivity;
import in.jploft.esevak.ui.components.adapters.AllCategoryAdapter;
import in.jploft.esevak.ui.components.fragments.cart.CartFragment;
import in.jploft.esevak.ui.components.fragments.service.ServiceFragment;
import in.jploft.esevak.ui.components.fragments.subCategory.SubCategoryFragment;
import in.jploft.esevak.utils.Constants;
import in.jploft.esevak.utils.ProgressDialog;
import in.jploft.esevak.utils.SessionManager;
import in.jploft.esevak.utils.Utility;

public class AllCategoryFragment extends BaseFragment {
    private static final String TAG = AllCategoryFragment.class.getName();
    private FragmentAllCateogryBinding binding;
    private AllCategoryViewModel viewModel;
    private View.OnClickListener onClickListener = null;
    private HomeActivity homeActivity;
    private SessionManager sessionManager;
    private ArrayList<AllCategoryData.Category> allCategoryData;
    private AllCategoryAdapter allCategoryAdapter;


    @Override
    protected ViewDataBinding setBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_cateogry, container, false);
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
        allCategoryData = new ArrayList<>();
        binding.appBar.tvTitle.setText("Categories");
        allCategoryAPI();
    }


    @Override
    public void onResume() {
        super.onResume();
        Utility.setAppBar(mActivity);
    }


    @Override
    protected void initializeOnCreateObject() {
        homeActivity = (HomeActivity) getActivity();
        viewModel = new ViewModelProvider(this).get(AllCategoryViewModel.class);
        viewModel.responseLiveData.observe(this, new Observer<ApiResponse<AllCategoryData>>() {
            @Override
            public void onChanged(ApiResponse<AllCategoryData> it) {
                handleResult(it);
            }
        });
    }


    @Override
    protected void setListeners() {
        binding.appBar.ivBack.setOnClickListener(this);
        binding.appBar.ivCart.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                homeActivity.onBackPressed();
                break;
            case R.id.rowCategoryItem:
                int position = (int) view.getTag();
                allCategoryAdapter.selectedPos = position;
                Bundle bundle = new Bundle();
                bundle.putString("catId", allCategoryData.get(position).getId());
                bundle.putString("catName", allCategoryData.get(position).getName());
                bundle.putString("catImage", allCategoryData.get(position).getImage());

                homeActivity.changeFragment(new SubCategoryFragment(), true, bundle);
                break;
            case R.id.ivCart:
                homeActivity.changeFragment(new CartFragment(),true);
                break;
        }
    }

    private void allCategoryAPI() {
        String userId = sessionManager.getUserData().getId();
        if (userId != null && !userId.equals("")) {

            HashMap<String, String> reqData = new HashMap<>();
            reqData.put("userId", userId);

            Log.e(TAG, "Api parameters - " + reqData.toString());
            viewModel.allCategory(reqData);
        }
    }

    private void handleResult(ApiResponse<AllCategoryData> result) {
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
                    binding.rvAllCategory.setVisibility(View.VISIBLE);
                    binding.layoutError.rlerror.setVisibility(View.GONE);

                    if (result.getData().getData().getCategory() != null && !result.getData().getData().getCategory().isEmpty()) {
                        binding.rvAllCategory.setVisibility(View.VISIBLE);
                        binding.layoutError.rlerror.setVisibility(View.GONE);
                        allCategoryData.clear();
                        allCategoryData.addAll(result.getData().getData().getCategory());
                        if (allCategoryData.size() != 0) {
                            allCategoryAdapter = new AllCategoryAdapter(mActivity, onClickListener, allCategoryData);
                            binding.rvAllCategory.setAdapter(allCategoryAdapter);
                        }
                    } else {
                        binding.rvAllCategory.setVisibility(View.GONE);
                        binding.layoutError.rlerror.setVisibility(View.VISIBLE);
                    }

                } else {
                    binding.rvAllCategory.setVisibility(View.GONE);
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