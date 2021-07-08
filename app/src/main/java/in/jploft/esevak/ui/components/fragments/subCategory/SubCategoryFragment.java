package in.jploft.esevak.ui.components.fragments.subCategory;

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
import in.jploft.esevak.databinding.FragmentSubCateogryBinding;
import in.jploft.esevak.network.ApiResponse;
import in.jploft.esevak.pojo.AllCategoryData;
import in.jploft.esevak.pojo.SubCategoryData;
import in.jploft.esevak.ui.base.BaseFragment;
import in.jploft.esevak.ui.components.activities.home.HomeActivity;
import in.jploft.esevak.ui.components.adapters.AllCategoryAdapter;
import in.jploft.esevak.ui.components.adapters.SubCategoryAdapter;
import in.jploft.esevak.ui.components.fragments.cart.CartFragment;
import in.jploft.esevak.ui.components.fragments.service.ServiceFragment;
import in.jploft.esevak.utils.Constants;
import in.jploft.esevak.utils.ProgressDialog;
import in.jploft.esevak.utils.SessionManager;
import in.jploft.esevak.utils.Utility;

public class SubCategoryFragment extends BaseFragment {
    private static final String TAG = SubCategoryFragment.class.getName();
    private FragmentSubCateogryBinding binding;
    private SubCategoryViewModel viewModel;
    private View.OnClickListener onClickListener = null;
    private HomeActivity homeActivity;
    private SessionManager sessionManager;
    private ArrayList<SubCategoryData.Data.CategoryItem> subCategoryData;
    private SubCategoryAdapter subCategoryAdapter;
    private Bundle bundle;
    private String catId = "", catName = "", catImage = "";


    @Override
    protected ViewDataBinding setBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sub_cateogry, container, false);
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
        getIntentData();
        sessionManager = new SessionManager();
        subCategoryData = new ArrayList<>();
        //binding.appBar.tvTitle.setText("Sub Categories");
        subCategoryAPI();
    }

    private void getIntentData() {
        bundle = this.getArguments();
        if (bundle != null) {
            catId = bundle.getString("catId");
            catName = bundle.getString("catName");
            catImage = bundle.getString("catImage");
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        Utility.setAppBar(mActivity);
        binding.appBar.tvTitle.setText(Utility.toTitleCase(catName));
        binding.appBar.tvTitle.setSelected(true);
    }


    @Override
    protected void initializeOnCreateObject() {
        homeActivity = (HomeActivity) getActivity();
        viewModel = new ViewModelProvider(this).get(SubCategoryViewModel.class);
        viewModel.responseLiveData.observe(this, new Observer<ApiResponse<SubCategoryData>>() {
            @Override
            public void onChanged(ApiResponse<SubCategoryData> it) {
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
                subCategoryAdapter.selectedPos = position;
                Bundle bundle = new Bundle();
                bundle.putString("subCatId", subCategoryData.get(position).getId());
                bundle.putString("subCatName", subCategoryData.get(position).getName());
                bundle.putString("subCatImage", subCategoryData.get(position).getImage());

                homeActivity.changeFragment(new ServiceFragment(), true, bundle);
                break;
            case R.id.ivCart:
                homeActivity.changeFragment(new CartFragment(),true);
                break;
        }
    }

    private void subCategoryAPI() {
        String userId = sessionManager.getUserData().getId();
        if (catId != null && !catId.equals("")) {

            HashMap<String, String> reqData = new HashMap<>();
            reqData.put("category_id", catId);

            Log.e(TAG, "Api parameters - " + reqData.toString());
            viewModel.subCategory(reqData);
        }
    }

    private void handleResult(ApiResponse<SubCategoryData> result) {
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
                    binding.rvSubCategory.setVisibility(View.VISIBLE);
                    binding.layoutError.rlerror.setVisibility(View.GONE);

                    if (result.getData().getData().getCategory() != null && !result.getData().getData().getCategory().isEmpty()) {
                        binding.rvSubCategory.setVisibility(View.VISIBLE);
                        binding.layoutError.rlerror.setVisibility(View.GONE);
                        subCategoryData.clear();
                        subCategoryData.addAll(result.getData().getData().getCategory());
                        if (subCategoryData.size() != 0) {
                            subCategoryAdapter = new SubCategoryAdapter(mActivity, onClickListener, subCategoryData);
                            binding.rvSubCategory.setAdapter(subCategoryAdapter);
                        }
                    } else {
                        binding.rvSubCategory.setVisibility(View.GONE);
                        binding.layoutError.rlerror.setVisibility(View.VISIBLE);
                    }

                } else {
                    binding.rvSubCategory.setVisibility(View.GONE);
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