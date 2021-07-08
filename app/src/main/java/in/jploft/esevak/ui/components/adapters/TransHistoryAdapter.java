package in.jploft.esevak.ui.components.adapters;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ViewDataBinding;

import java.util.ArrayList;

import in.jploft.esevak.R;
import in.jploft.esevak.pojo.TransactionHistoryData;
import in.jploft.esevak.ui.base.RecyclerBaseAdapter;
import in.jploft.esevak.utils.Utility;

public class TransHistoryAdapter extends RecyclerBaseAdapter {
    private AppCompatActivity mActivity;
    private View.OnClickListener onClickListener;
    private ArrayList<TransactionHistoryData.Transaction> list;


    public TransHistoryAdapter(AppCompatActivity mActivity, View.OnClickListener onClickListener, ArrayList<TransactionHistoryData.Transaction> list) {
        this.mActivity = mActivity;
        this.onClickListener = onClickListener;
        this.list = list;
    }

    @Override
    protected int getLayoutIdForPosition(int position) {
        return R.layout.row_trans_history;
    }

    @Override
    public Object getViewModel(int position) {
        return list.get(position);
    }


    @Override
    protected void putViewDataBinding(ViewDataBinding viewDataBinding, int position) {
        View view = viewDataBinding.getRoot();
        LinearLayout rootHeader = view.findViewById(R.id.rowTransHistoryItem);
        rootHeader.setTag(position);
        rootHeader.setOnClickListener(onClickListener);
        TextView tvPrice = view.findViewById(R.id.tvPrice);
        TextView tvPaymentStatus = view.findViewById(R.id.tvPaymentStatus);
        TextView tvDate = view.findViewById(R.id.tvDate);
        ImageView ivTransaction = view.findViewById(R.id.ivTransaction);
        ImageView ivArrow = view.findViewById(R.id.ivArrow);


        tvDate.setText(Utility.getFormatChangeDate(list.get(position).getDate()));

        if (list.get(position).getStatus().equals("1")) {
            tvPaymentStatus.setText("Payment Received");
            tvPrice.setText("+ Rs " + list.get(position).getAmount());
            tvPrice.setTextColor(mActivity.getResources().getColor(R.color.color_20C715));
            ivTransaction.setImageResource(R.drawable.ic_payment_receive);
            ivArrow.setImageResource(R.drawable.ic_arrow_received);
        } else if (list.get(position).getStatus().equals("2")) {
            tvPaymentStatus.setText("Amount Refund");
            tvPrice.setText("- Rs " + list.get(position).getAmount());
            tvPrice.setTextColor(mActivity.getResources().getColor(R.color.color_FF653B));
            ivTransaction.setImageResource(R.drawable.ic_payment_refund);
            ivArrow.setImageResource(R.drawable.ic_arrow_refund);
        } else {
            tvPaymentStatus.setText("Payment Pending");
            tvPrice.setText("- Rs " + list.get(position).getAmount());
            tvPrice.setTextColor(mActivity.getResources().getColor(R.color.color_FF653B));
            ivTransaction.setImageResource(R.drawable.ic_payment_refund);
            ivArrow.setImageResource(R.drawable.ic_arrow_refund);
        }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}

