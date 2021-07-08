package in.jploft.esevak.ui.components.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ViewDataBinding;

import java.util.ArrayList;

import in.jploft.esevak.R;
import in.jploft.esevak.pojo.ProductDetailData;
import in.jploft.esevak.ui.base.RecyclerBaseAdapter;
import in.jploft.esevak.utils.Constants;
import in.jploft.esevak.utils.Utility;

public class FAQAdapter extends RecyclerBaseAdapter {
    private AppCompatActivity mActivity;
    private View.OnClickListener onClickListener;
    private ArrayList<ProductDetailData.Faq> list;
    private int currentPosition = 0;
    private boolean flag = true;


    public FAQAdapter(AppCompatActivity mActivity, View.OnClickListener onClickListener, ArrayList<ProductDetailData.Faq> list) {
        this.mActivity = mActivity;
        this.onClickListener = onClickListener;
        this.list = list;
    }

    @Override
    protected int getLayoutIdForPosition(int position) {
        return R.layout.row_faq;
    }

    @Override
    public Object getViewModel(int position) {
        return list.get(position);
    }

    @Override
    protected void putViewDataBinding(ViewDataBinding viewDataBinding, int position) {
        View view = viewDataBinding.getRoot();
        LinearLayout rootHeader = view.findViewById(R.id.rowFAQItem);
        rootHeader.setTag(position);
        rootHeader.setOnClickListener(onClickListener);

        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvFAQDescription = view.findViewById(R.id.tvFAQDescription);
        tvTitle.setText(Utility.toTitleCase(list.get(position).getTitle()));
        tvFAQDescription.setText(list.get(position).getContent());
        ImageView ivArrow = view.findViewById(R.id.ivArrow);

        LinearLayout linearFAQDescription = view.findViewById(R.id.linearFAQDescription);
        LinearLayout linearFAQTitle = view.findViewById(R.id.linearFAQTitle);
        linearFAQDescription.setVisibility(View.GONE);

       /* if (currentPosition == position) {
            //toggling visibility
                linearFAQDescription.setVisibility(View.VISIBLE);
                ivArrow.setImageResource(R.drawable.ic_minus);
                tvTitle.setTextColor(mActivity.getResources().getColor(R.color.color_FF653B));

        } else {
            linearFAQDescription.setVisibility(View.GONE);
            ivArrow.setImageResource(R.drawable.ic_plus);
            tvTitle.setTextColor(mActivity.getResources().getColor(R.color.colorBlack));
        }*/

        linearFAQTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // currentPosition = position; //getting the position of the item to expand it
                // notifyDataSetChanged(); //reloding the list
                if (Constants.allSelectPosition.contains(list.get(position).getId())) {
                    Constants.allSelectPosition.remove(list.get(position).getId());
                    changeColor(linearFAQDescription, ivArrow, tvTitle, false);
                } else {
                    Constants.allSelectPosition.add(list.get(position).getId());
                    changeColor(linearFAQDescription, ivArrow, tvTitle, true);
                }
            }
        });
    }

    private void changeColor(LinearLayout linearFAQDescription, ImageView ivArrow, TextView tvTitle, boolean condition) {
        if (condition) {
            linearFAQDescription.setVisibility(View.VISIBLE);
            ivArrow.setImageResource(R.drawable.ic_minus);
            tvTitle.setTextColor(mActivity.getResources().getColor(R.color.color_FF653B));
        } else {
            linearFAQDescription.setVisibility(View.GONE);
            ivArrow.setImageResource(R.drawable.ic_plus);
            tvTitle.setTextColor(mActivity.getResources().getColor(R.color.colorBlack));
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}

