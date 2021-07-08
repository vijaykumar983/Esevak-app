package in.jploft.esevak.ui.components.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ViewDataBinding;

import java.util.ArrayList;

import in.jploft.esevak.R;
import in.jploft.esevak.pojo.ServiceData;
import in.jploft.esevak.ui.base.RecyclerBaseAdapter;
import in.jploft.esevak.utils.Utility;

public class ProductAdapter extends RecyclerBaseAdapter {
    private AppCompatActivity mActivity;
    private View.OnClickListener onClickListener;
    private ArrayList<ServiceData.Product> list;
    public  int selectedPos = 0;


    public ProductAdapter(AppCompatActivity mActivity, View.OnClickListener onClickListener, ArrayList<ServiceData.Product> list) {
        this.mActivity = mActivity;
        this.onClickListener = onClickListener;
        this.list = list;
    }


    @Override
    protected int getLayoutIdForPosition(int position) {
        return R.layout.row_product;
    }

    @Override
    public Object getViewModel(int position) {
        return list.get(position);
    }

    @Override
    protected void putViewDataBinding(ViewDataBinding viewDataBinding, int position) {
        View view = viewDataBinding.getRoot();
        RelativeLayout rootHeader = view.findViewById(R.id.rowProductItem);
        rootHeader.setTag(position);
        rootHeader.setOnClickListener(onClickListener);
        ImageView ivProduct = view.findViewById(R.id.ivProduct);
        TextView tvProductTitle = view.findViewById(R.id.tvProductTitle);

        //Utility.loadPicture(ivProduct, Constants.imageUrl + list.get(position).getImage());
        Utility.loadPicture(ivProduct, list.get(position).getImage());
        tvProductTitle.setText(Utility.toTitleCase(list.get(position).getName()));
        tvProductTitle.setSelected(true);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}