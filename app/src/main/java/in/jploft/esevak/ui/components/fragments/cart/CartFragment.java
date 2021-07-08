package in.jploft.esevak.ui.components.fragments.cart;

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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import in.jploft.esevak.R;
import in.jploft.esevak.databinding.DialogAddAddressBinding;
import in.jploft.esevak.databinding.DialogSelectDayBinding;
import in.jploft.esevak.databinding.FragementCartBinding;
import in.jploft.esevak.network.ApiResponse;
import in.jploft.esevak.pojo.ApplyCouponData;
import in.jploft.esevak.pojo.MyCartData;
import in.jploft.esevak.pojo.RemoveCartData;
import in.jploft.esevak.pojo.TimeSlotData;
import in.jploft.esevak.ui.base.BaseFragment;
import in.jploft.esevak.ui.components.activities.home.HomeActivity;
import in.jploft.esevak.ui.components.activities.payment.PaymentActivity;
import in.jploft.esevak.ui.components.adapters.CartAdapter;
import in.jploft.esevak.ui.components.adapters.DayAdapter;
import in.jploft.esevak.ui.components.adapters.TimeAdapter;
import in.jploft.esevak.utils.CartInterface;
import in.jploft.esevak.utils.Constants;
import in.jploft.esevak.utils.ProgressDialog;
import in.jploft.esevak.utils.SessionManager;
import in.jploft.esevak.utils.TimeInterface;
import in.jploft.esevak.utils.Utility;

public class CartFragment extends BaseFragment implements CartInterface, TimeInterface {
    private static final String TAG = CartFragment.class.getName();
    private FragementCartBinding binding;
    private CartViewModel viewModel;
    private View.OnClickListener onClickListener = null;
    private HomeActivity homeActivity;
    private SessionManager sessionManager;
    private ArrayList<MyCartData.Datum> myCartData;
    private CartAdapter cartAdapter;
    private String totalPrice = "", newTotalPrice = "", discount = "", finalTotalPrice = "", startTime = "", endTime = "";
    private ArrayList<TimeSlotData.TimeslotItem> dayTimeData;
    private DayAdapter dayAdapter;
    //for today timing
    private List<String> times, times1;
    private TimeAdapter timeAdapter;


    @Override
    protected ViewDataBinding setBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragement_cart, container, false);
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
        myCartData = new ArrayList<>();
        dayTimeData = new ArrayList<>();
        myCartAPI();
    }


    @Override
    public void onResume() {
        super.onResume();
        Utility.setAppBar(mActivity);
        binding.appBar.tvTitle.setText("Cart");
        binding.appBar.ivCart.setVisibility(View.GONE);
        binding.tvAddress.setText(sessionManager.getLOCATION());
        binding.tvAddress.setSelected(true);
    }

    @Override
    protected void initializeOnCreateObject() {
        homeActivity = (HomeActivity) getActivity();
        viewModel = new ViewModelProvider(this).get(CartViewModel.class);
        viewModel.responseLiveData.observe(this, new Observer<ApiResponse<MyCartData>>() {
            @Override
            public void onChanged(ApiResponse<MyCartData> it) {
                handleResult(it);
            }
        });
        viewModel.responseRemoveCartLiveData.observe(this, new Observer<ApiResponse<RemoveCartData>>() {
            @Override
            public void onChanged(ApiResponse<RemoveCartData> it) {
                handleRemoveCartResult(it);
            }
        });
        viewModel.responseApplyCouponLiveData.observe(this, new Observer<ApiResponse<ApplyCouponData>>() {
            @Override
            public void onChanged(ApiResponse<ApplyCouponData> it) {
                handleApplyCouponResult(it);
            }
        });
        viewModel.responseTimeSlotLiveData.observe(this, new Observer<ApiResponse<TimeSlotData>>() {
            @Override
            public void onChanged(ApiResponse<TimeSlotData> it) {
                handleTimeSlotResult(it);
            }
        });
        viewModel.timeSlotApi();
    }


    @Override
    protected void setListeners() {
        binding.appBar.ivBack.setOnClickListener(this);
        binding.btnCheckout.setOnClickListener(this);
        binding.tvApplyCode.setOnClickListener(this);
        binding.ivAddAddress.setOnClickListener(this);
        binding.ivCouponCancel.setOnClickListener(this);
        binding.llSelectDay.setOnClickListener(this);
        binding.llSelectTime.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                homeActivity.onBackPressed();
                break;
            case R.id.btnCheckout:
                String day = binding.tvSelectDay.getText().toString().trim();
                String time = binding.tvSelectTime.getText().toString().trim();
                if (day.equals("Select day")) {
                    Toast.makeText(mActivity, "Please select day", Toast.LENGTH_SHORT).show();
                } else if (time.equals("Select time")) {
                    Toast.makeText(mActivity, "Please select time", Toast.LENGTH_SHORT).show();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("ApplyCoupon", binding.edtCouponCode.getText().toString());
                    bundle.putString("finalTotalPrice", finalTotalPrice);
                    bundle.putString("day", day);
                    bundle.putString("time", time);
                    PaymentActivity.startActivity(mActivity, bundle, false);
                }

                break;
            case R.id.tvApplyCode:
                applyCouponAPI();
                break;
            case R.id.ivAddAddress:
                addLocationDialog();
                break;
            case R.id.ivCouponCancel:
                binding.linearApplyCoupon.setVisibility(View.GONE);
                binding.tvApplyCode.setVisibility(View.VISIBLE);
                binding.edtCouponCode.setEnabled(true);
                binding.edtCouponCode.setText("");
                discount = "0";
                binding.tvTotalAmount.setText(totalPrice);
                binding.tvDiscount.setText(discount);
                finalTotalPrice = totalPrice;
                break;
            case R.id.llSelectDay:
                selectDayDialog();
                break;
            case R.id.llSelectTime:
                if (binding.tvSelectDay.getText().toString().equals("Select day")) {
                    Toast.makeText(mActivity, "Please select day", Toast.LENGTH_SHORT).show();
                } else {
                    selectTimeDialog();
                }
                break;
        }
    }

    private void selectDayDialog() {
        final Dialog dialog = new Dialog(mActivity, R.style.Theme_Dialog);
        DialogSelectDayBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(mActivity), R.layout.dialog_select_day, null, false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(dialogBinding.getRoot());

        dialogBinding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constants.selectedDayId.equals("")) {
                    Toast.makeText(mActivity, "Please select day", Toast.LENGTH_SHORT).show();
                    binding.tvSelectDay.setText("Select day");
                    binding.tvSelectDay.setSelected(true);
                } else {
                    dialog.dismiss();
                    binding.tvSelectDay.setText(Constants.selectedDayName);
                    binding.tvSelectDay.setSelected(true);
                    getTodayTiming();
                }
            }
        });
        dialogBinding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        if (dayTimeData.size() > 0) {
            dialogBinding.rvDay.setVisibility(View.VISIBLE);
            dialogBinding.btnDone.setVisibility(View.VISIBLE);
            dialogBinding.layoutError1.rlerror.setVisibility(View.GONE);
            dayAdapter = new DayAdapter(mActivity, onClickListener, dayTimeData, this);
            dialogBinding.rvDay.setAdapter(dayAdapter);
        } else {
            dialogBinding.rvDay.setVisibility(View.GONE);
            dialogBinding.btnDone.setVisibility(View.GONE);
            dialogBinding.layoutError1.rlerror.setVisibility(View.VISIBLE);
        }

        dialog.show();
    }

    private void getTodayTiming() {
        try {
            Date d = null;
            SimpleDateFormat df = null;

            //for today time
            String current_Hour = null;
            int total_hours_today = 0;

            //current hour and minute
            Calendar calendar = Calendar.getInstance();
            //int hourOfDay = (calendar.get(Calendar.HOUR_OF_DAY) + 1);
            int hourOfDay = (calendar.get(Calendar.HOUR_OF_DAY));
            int minuteOfDay = calendar.get(Calendar.MINUTE);
            current_Hour = hourOfDay + ":" + minuteOfDay;


            if (hourOfDay >= Utility.getTimeInHour(startTime) && hourOfDay <= Utility.getTimeInHour(endTime)) {

                startTime = current_Hour;
                total_hours_today = Utility.getTotalHour(current_Hour, endTime);
            } else if (hourOfDay == 24 || (hourOfDay >= 1 && hourOfDay <= Utility.getTimeInHour(startTime))) {
                startTime = startTime;
                total_hours_today = Utility.getTotalHour(startTime, endTime);
            }

            df = new SimpleDateFormat("HH:mm");
            d = df.parse(startTime);


            Log.v(TAG, "today total hours = " + total_hours_today);

            times = new ArrayList<>();
            times1 = new ArrayList<>();

            int gapInMinutes = 0;
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            for (int i = 1; i <= total_hours_today; i++) {
                times.add(startTime);
                cal.add(Calendar.MINUTE, gapInMinutes + 60);
                startTime = df.format(cal.getTime());
                times1.add(startTime);
            }

            Log.v(TAG, "today time size : " + times.size() + "  today times slots" + times.toString());

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void selectTimeDialog() {
        final Dialog dialog = new Dialog(mActivity, R.style.Theme_Dialog);
        DialogSelectDayBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(mActivity), R.layout.dialog_select_day, null, false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(dialogBinding.getRoot());

        dialogBinding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constants.selectedTime.equals("")) {
                    Toast.makeText(mActivity, "Please select time", Toast.LENGTH_SHORT).show();
                    binding.tvSelectTime.setText("Select Time");
                    binding.tvSelectTime.setSelected(true);
                } else {
                    dialog.dismiss();
                    binding.tvSelectTime.setText(Constants.selectedTime);
                    binding.tvSelectTime.setSelected(true);
                }
            }
        });
        dialogBinding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        if (times.size() > 0 && times1.size() > 0) {
            timeAdapter = new TimeAdapter(mActivity, onClickListener, times, times1);
            dialogBinding.rvDay.setAdapter(timeAdapter);
        } else {
            dialogBinding.rvDay.setVisibility(View.GONE);
            dialogBinding.btnDone.setVisibility(View.GONE);
            dialogBinding.layoutError1.rlerror.setVisibility(View.VISIBLE);
        }

        dialog.show();
    }


    private void handleTimeSlotResult(ApiResponse<TimeSlotData> result) {
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

                    if (result.getData().getTimeslot() != null && !result.getData().getTimeslot().isEmpty()) {
                        dayTimeData.clear();
                        dayTimeData.addAll(result.getData().getTimeslot());
                    }

                } else {
                    Utility.showToastMessageError(mActivity, result.getData().getMessage());
                }
                break;
        }
    }

    private void myCartAPI() {
        String userId = sessionManager.getUserData().getId();
        if (userId != null && !userId.equals("")) {

            HashMap<String, String> reqData = new HashMap<>();
            reqData.put("userId", userId);

            Log.e(TAG, "Api parameters - " + reqData.toString());
            viewModel.myCartApi(reqData);
        }
    }


    private void handleResult(ApiResponse<MyCartData> result) {
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
                    binding.linearMain.setVisibility(View.VISIBLE);
                    binding.linearEmptyCart.setVisibility(View.GONE);

                    if (result.getData().getData() != null && !result.getData().getData().isEmpty()) {
                        binding.rvCart.setVisibility(View.VISIBLE);
                        binding.layoutError.rlerror.setVisibility(View.GONE);

                        binding.tvCartTotal.setText(String.valueOf(result.getData().getTotalCart()));
                        binding.tvTax.setText(String.valueOf(result.getData().getTexi()));
                        totalPrice = String.valueOf(result.getData().getTotalAmount());
                        binding.tvTotalAmount.setText(totalPrice);
                        discount = "0";
                        binding.tvDiscount.setText(discount);
                        finalTotalPrice = totalPrice;

                        myCartData.clear();
                        myCartData.addAll(result.getData().getData());
                        if (myCartData.size() != 0) {
                            cartAdapter = new CartAdapter(mActivity, onClickListener, myCartData, this);
                            binding.rvCart.setAdapter(cartAdapter);
                        }
                    } else {
                        binding.rvCart.setVisibility(View.GONE);
                        binding.layoutError.rlerror.setVisibility(View.VISIBLE);
                    }

                } else {
                    binding.linearMain.setVisibility(View.GONE);
                    binding.linearEmptyCart.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void removeCartAPI(String id) {
        if (id != null && !id.equals("")) {

            HashMap<String, String> reqData = new HashMap<>();
            reqData.put("id", id);

            Log.e(TAG, "Api parameters - " + reqData.toString());
            viewModel.removeCartApi(reqData);
        }
    }

    private void handleRemoveCartResult(ApiResponse<RemoveCartData> result) {
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


                    binding.linearApplyCoupon.setVisibility(View.GONE);
                    binding.tvApplyCode.setVisibility(View.VISIBLE);
                    binding.edtCouponCode.setEnabled(true);
                    binding.edtCouponCode.setText("");
                    myCartAPI();

                } else {
                    Utility.showToastMessageError(mActivity, result.getData().getMessage());
                }
                break;
        }
    }

    private void applyCouponAPI() {
        String code = binding.edtCouponCode.getText().toString();
        if (!code.isEmpty() && !code.equals("")) {

            HashMap<String, String> reqData = new HashMap<>();
            reqData.put("code", code);
            reqData.put("amount", totalPrice);

            Log.e(TAG, "Api parameters - " + reqData.toString());
            viewModel.applyCouponApi(reqData);
        } else {
            Utility.showToastMessageError(mActivity, "Please enter a valid coupon code");
        }
    }

    private void handleApplyCouponResult(ApiResponse<ApplyCouponData> result) {
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

                    binding.edtCouponCode.setEnabled(false);
                    binding.tvApplyCode.setVisibility(View.GONE);
                    binding.linearApplyCoupon.setVisibility(View.VISIBLE);
                    binding.tvCode.setText(binding.edtCouponCode.getText().toString());

                    newTotalPrice = result.getData().getNewprice();
                    discount = result.getData().getDiscount();
                    binding.tvTotalAmount.setText(newTotalPrice);
                    binding.tvDiscount.setText(discount);
                    finalTotalPrice = newTotalPrice;

                } else {
                    Utility.showToastMessageError(mActivity, result.getData().getMessage());
                }
                break;
        }
    }

    private void addLocationDialog() {
        final Dialog dialog = new Dialog(mActivity, R.style.Theme_Dialog);
        DialogAddAddressBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(mActivity), R.layout.dialog_add_address, null, false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(dialogBinding.getRoot());


        dialogBinding.tvTitle.setText("Add Address");
        dialogBinding.edtAddress.setText(sessionManager.getLOCATION());
        dialogBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utility.hideKeyboard(mActivity);
                if (!dialogBinding.edtAddress.getText().toString().isEmpty()) {
                    if (dialogBinding.edtAddress.getText().toString().length() < 3) {
                        Utility.showToastMessageError(mActivity, mActivity.getString(R.string.minimum_3_char_long_address));
                    } else {
                        dialog.dismiss();
                        sessionManager.setLOCATION(dialogBinding.edtAddress.getText().toString().trim());
                        binding.tvAddress.setText(sessionManager.getLOCATION());
                        binding.tvAddress.setSelected(true);
                    }
                } else {
                    Utility.showToastMessageError(mActivity, "Please enter address");
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

    @Override
    public void updateData(String id) {
        removeCartAPI(id);
    }

    @Override
    public void onDestroy() {
        viewModel.disposeSubscriber();
        Constants.selectedDayId = "";
        Constants.selectedDayName = "";
        Constants.selectedTime = "";
        super.onDestroy();
    }

    @Override
    public void updateData(String stTime, String edTime) {
        startTime = stTime;
        endTime = edTime;
        Log.e(TAG, "start time - " + stTime + " " + "end time - " + endTime);
    }
}