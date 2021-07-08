package in.jploft.esevak.ui.components.fragments.support;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.drawerlayout.widget.DrawerLayout;

import in.jploft.esevak.R;
import in.jploft.esevak.databinding.FragmentSupportBinding;
import in.jploft.esevak.ui.base.BaseFragment;
import in.jploft.esevak.ui.components.activities.home.HomeActivity;
import in.jploft.esevak.utils.Utility;


public class SupportFragment extends BaseFragment {
    private FragmentSupportBinding binding;
    private SupportViewModel homeViewModel;
    private View.OnClickListener onClickListener = null;
    private HomeActivity homeActivity;


    @Override
    protected ViewDataBinding setBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_support, container, false);
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

    }

    @Override
    public void onResume() {
        super.onResume();
        Utility.setAppBar(mActivity);
        binding.appBar.tvTitle.setText("Support");
        binding.appBar.ivCart.setVisibility(View.GONE);
    }


    @Override
    protected void initializeOnCreateObject() {
        homeActivity = (HomeActivity) getActivity();
        //viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
    }


    @Override
    protected void setListeners() {
        binding.appBar.ivBack.setOnClickListener(this);
        binding.linearContact.setOnClickListener(this);
        binding.tvEmailId.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                homeActivity.onBackPressed();
                break;
            case R.id.linearContact:
                Utility.audioCall(mActivity,"+91-9658741241");
                break;
            case R.id.tvEmailId:
               Utility.sendEmail(mActivity,"support@esevak.com");
                break;
        }
    }
    @Override
    public void onDestroy() {
        //viewModel.disposeSubscriber();
        super.onDestroy();
    }
}