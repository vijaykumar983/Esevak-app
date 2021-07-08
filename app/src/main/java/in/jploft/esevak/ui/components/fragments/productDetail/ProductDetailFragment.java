package in.jploft.esevak.ui.components.fragments.productDetail;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import in.jploft.esevak.R;
import in.jploft.esevak.databinding.FragmentProductDetailBinding;
import in.jploft.esevak.network.ApiResponse;
import in.jploft.esevak.pojo.AddToCartData;
import in.jploft.esevak.pojo.ProductDetailData;
import in.jploft.esevak.ui.base.BaseFragment;
import in.jploft.esevak.ui.components.activities.home.HomeActivity;
import in.jploft.esevak.ui.components.adapters.FAQAdapter;
import in.jploft.esevak.ui.components.fragments.cart.CartFragment;
import in.jploft.esevak.utils.Constants;
import in.jploft.esevak.utils.ProgressDialog;
import in.jploft.esevak.utils.SessionManager;
import in.jploft.esevak.utils.Utility;

public class ProductDetailFragment extends BaseFragment {
    private static final String TAG = ProductDetailFragment.class.getName();
    private FragmentProductDetailBinding binding;
    private ProductDetailViewModel viewModel;
    private View.OnClickListener onClickListener = null;
    private HomeActivity homeActivity;
    private Bundle bundle;
    private String productId = "", productName = "", productImage = "";
    private ArrayList<ProductDetailData.Faq> faqData;
    private FAQAdapter faqAdapter;


    @Override
    protected ViewDataBinding setBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_detail, container, false);
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
        faqData = new ArrayList<>();
        getIntentData();
        productDetailAPI();
    }

    private void getIntentData() {
        bundle = this.getArguments();
        if (bundle != null) {
            productId = bundle.getString("productId");
            productName = bundle.getString("productName");
            productImage = bundle.getString("productImage");
            //Utility.loadPicture(binding.imgBanner, Constants.imageUrl + productImage);
            Utility.loadPicture(binding.imgBanner, productImage);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        Utility.setAppBar(mActivity);
        binding.appBar.tvTitle.setText(Utility.toTitleCase(productName));
        binding.appBar.tvTitle.setSelected(true);
    }


    @Override
    protected void initializeOnCreateObject() {
        homeActivity = (HomeActivity) getActivity();
        viewModel = new ViewModelProvider(this).get(ProductDetailViewModel.class);
        viewModel.responseLiveData.observe(this, new Observer<ApiResponse<ProductDetailData>>() {
            @Override
            public void onChanged(ApiResponse<ProductDetailData> it) {
                handleResult(it);
            }
        });
        viewModel.responseAddCartLiveData.observe(this, new Observer<ApiResponse<AddToCartData>>() {
            @Override
            public void onChanged(ApiResponse<AddToCartData> it) {
                handleAddCartResult(it);
            }
        });
    }


    @Override
    protected void setListeners() {
        binding.appBar.ivBack.setOnClickListener(this);
        binding.appBar.ivCart.setOnClickListener(this);
        binding.tvDescription.setOnClickListener(this);
        binding.tvFAQ.setOnClickListener(this);
        binding.btnAddToCart.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                homeActivity.onBackPressed();
                break;
            case R.id.ivCart:
                homeActivity.changeFragment(new CartFragment(), true);
                break;
            case R.id.tvDescription:
                setBackground(binding.tvDescription, binding.tvFAQ);
                binding.linearDescription.setVisibility(View.VISIBLE);
                binding.linearFaq.setVisibility(View.GONE);
                break;
            case R.id.tvFAQ:
                setBackground(binding.tvFAQ, binding.tvDescription);
                binding.linearFaq.setVisibility(View.VISIBLE);
                binding.linearDescription.setVisibility(View.GONE);
                break;
            case R.id.btnAddToCart:
                addCartAPI();
                break;
        }
    }

    private void productDetailAPI() {
        String userId = sessionManager.getUserData().getId();
        if (userId != null && !userId.equals("") && productId != null && !productId.equals("")) {

            HashMap<String, String> reqData = new HashMap<>();
            reqData.put("userId", userId);
            reqData.put("pid", productId);

            Log.e(TAG, "Api parameters - " + reqData.toString());
            viewModel.productDetail(reqData);
        }
    }


    private void handleResult(ApiResponse<ProductDetailData> result) {
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
                    binding.layoutError.rlerror.setVisibility(View.GONE);
                    binding.layoutError1.rlerror.setVisibility(View.GONE);

                    binding.tvAmount.setText("Rs. " + result.getData().getData().getPrice());

                    if (result.getData().getData().getDescription() != null && !result.getData().getData().getDescription().isEmpty()) {
                        binding.tvDesDetails.setVisibility(View.VISIBLE);
                        binding.layoutError.rlerror.setVisibility(View.GONE);
                        binding.tvDesDetails.setText(result.getData().getData().getDescription());
                    } else {
                        binding.tvDesDetails.setVisibility(View.GONE);
                        binding.layoutError.rlerror.setVisibility(View.VISIBLE);
                    }


                    if (result.getData().getData().getFaq() != null && !result.getData().getData().getFaq().isEmpty()) {
                        binding.rvFAQ.setVisibility(View.VISIBLE);
                        binding.layoutError1.rlerror.setVisibility(View.GONE);
                        faqData.clear();
                        faqData.addAll(result.getData().getData().getFaq());
                        if (faqData.size() != 0) {
                            faqAdapter = new FAQAdapter(mActivity, onClickListener, faqData);
                            binding.rvFAQ.setAdapter(faqAdapter);
                            binding.rvFAQ.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
                        }
                    } else {
                        binding.rvFAQ.setVisibility(View.GONE);
                        binding.layoutError1.rlerror.setVisibility(View.VISIBLE);
                    }

                } else {
                    binding.layoutError.rlerror.setVisibility(View.VISIBLE);
                    binding.layoutError1.rlerror.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void addCartAPI() {
        if (sessionManager.getUserData().getId() != null && !sessionManager.getUserData().getId().equals("") && productId != null && !productId.equals("")) {

            HashMap<String, String> reqData = new HashMap<>();
            reqData.put("userId", sessionManager.getUserData().getId());
            reqData.put("productId", productId);

            Log.e(TAG, "Api parameters - " + reqData.toString());
            viewModel.addToCartApi(reqData);
        }
    }


    private void handleAddCartResult(ApiResponse<AddToCartData> result) {
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

                    homeActivity.changeFragment(new CartFragment(), true);

                } else {
                    Utility.showToastMessageError(mActivity, result.getData().getMessage());
                }
                break;
        }
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private void setBackground(AppCompatTextView txt1, AppCompatTextView txt2) {
        txt1.setTextColor(mActivity.getResources().getColor(R.color.colorWhite));
        txt1.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.bg_rounded_darkblue));
        txt2.setTextColor(mActivity.getResources().getColor(R.color.colorBlack));
        txt2.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.bg_rounded_white));
    }

    @Override
    public void onDestroyView() {
        Constants.allSelectPosition.clear();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        viewModel.disposeSubscriber();
        super.onDestroy();
    }
}