package in.jploft.esevak.ui.components.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import java.util.ArrayList;

import in.jploft.esevak.R;
import in.jploft.esevak.databinding.DialogLogoutBinding;
import in.jploft.esevak.pojo.MyCartData;
import in.jploft.esevak.ui.base.RecyclerBaseAdapter;
import in.jploft.esevak.ui.components.activities.loginSignup.LoginSignupActivity;
import in.jploft.esevak.utils.CartInterface;
import in.jploft.esevak.utils.Constants;
import in.jploft.esevak.utils.Utility;

public class CartAdapter extends RecyclerBaseAdapter {
    private AppCompatActivity mActivity;
    private View.OnClickListener onClickListener;
    private ArrayList<MyCartData.Datum> list;
    private CartInterface cartInterface;



    public CartAdapter(AppCompatActivity mActivity, View.OnClickListener onClickListener, ArrayList<MyCartData.Datum> list, CartInterface cartInterface) {
        this.mActivity = mActivity;
        this.onClickListener = onClickListener;
        this.list = list;
        this.cartInterface = cartInterface;
    }

    @Override
    protected int getLayoutIdForPosition(int position) {
        return R.layout.row_cart;
    }

    @Override
    public Object getViewModel(int position) {
        return list.get(position);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void putViewDataBinding(ViewDataBinding viewDataBinding, int position) {
        View view = viewDataBinding.getRoot();
        RelativeLayout rootHeader = view.findViewById(R.id.rowCartItem);
        rootHeader.setTag(position);
        rootHeader.setOnClickListener(onClickListener);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvDescription = view.findViewById(R.id.tvDescription);
        TextView tvPrice = view.findViewById(R.id.tvPrice);
        ImageView ivProduct = view.findViewById(R.id.ivProduct);
        ImageView ivDelete = view.findViewById(R.id.ivDelete);

        //Utility.loadImage(ivProduct, Constants.imageUrl + list.get(position).getImage());
        Utility.loadImage(ivProduct,  list.get(position).getImage());
        tvTitle.setText(list.get(position).getProduct());
        tvTitle.setSelected(true);
        tvDescription.setText(list.get(position).getDescription());
        tvDescription.setSelected(true);
        tvPrice.setText("Rs. " + list.get(position).getPrice());
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeCartDialog(list.get(position).getId());
            }
        });
    }

    private void removeCartDialog(String id) {
        final Dialog dialog= new Dialog(mActivity,R.style.Theme_Dialog);
        DialogLogoutBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(mActivity), R.layout.dialog_logout, null, false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(dialogBinding.getRoot());


        dialogBinding.tvTitle.setText("Remove this item?");
        dialogBinding.tvMessage.setText("Do you want to remove this item from cart?");
        dialogBinding.btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialogBinding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                cartInterface.updateData(id);
            }
        });

        dialog.show();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}

