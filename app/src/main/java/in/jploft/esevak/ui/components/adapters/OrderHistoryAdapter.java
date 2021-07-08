package in.jploft.esevak.ui.components.adapters;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ViewDataBinding;

import java.util.ArrayList;

import in.jploft.esevak.R;
import in.jploft.esevak.pojo.MyOrdersData;
import in.jploft.esevak.ui.base.RecyclerBaseAdapter;
import in.jploft.esevak.utils.Constants;
import in.jploft.esevak.utils.Utility;

public class OrderHistoryAdapter extends RecyclerBaseAdapter {
    private AppCompatActivity mActivity;
    private View.OnClickListener onClickListener;
    private ArrayList<MyOrdersData.Order> list;


    public OrderHistoryAdapter(AppCompatActivity mActivity, View.OnClickListener onClickListener, ArrayList<MyOrdersData.Order> list) {
        this.mActivity = mActivity;
        this.onClickListener = onClickListener;
        this.list = list;
    }

    @Override
    protected int getLayoutIdForPosition(int position) {
        return R.layout.row_order_history;
    }

    @Override
    public Object getViewModel(int position) {
        return list.get(position);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void putViewDataBinding(ViewDataBinding viewDataBinding, int position) {
        View view = viewDataBinding.getRoot();
        RelativeLayout rootHeader = view.findViewById(R.id.rowOrderHistoryItem);
        rootHeader.setTag(position);
        rootHeader.setOnClickListener(onClickListener);
        ImageView ivOrders = view.findViewById(R.id.ivOrders);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvDate = view.findViewById(R.id.tvDate);
        TextView tvPrice = view.findViewById(R.id.tvPrice);
        Button btnStatus = view.findViewById(R.id.btnStatus);

        //Utility.loadPicture(ivOrders, Constants.imageUrl + list.get(position).getImage());
        Utility.loadPicture(ivOrders, list.get(position).getImage());
        tvTitle.setText(list.get(position).getProduct());
        tvTitle.setSelected(true);
        tvPrice.setText("Rs. " + list.get(position).getPrice());
        tvDate.setText(Utility.getChangeDate(list.get(position).getDate()));

        if (list.get(position).getStatus().equals("0")) {
            btnStatus.setBackground(mActivity.getResources().getDrawable(R.drawable.bg_rounded_pending));
            btnStatus.setText("pending");
        } else if (list.get(position).getStatus().equals("1")) {
            btnStatus.setBackground(mActivity.getResources().getDrawable(R.drawable.bg_rounded_pending));
            btnStatus.setText("cencel");
        } else {
            btnStatus.setBackground(mActivity.getResources().getDrawable(R.drawable.bg_rounded_done));
            btnStatus.setText("done");
        }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}

