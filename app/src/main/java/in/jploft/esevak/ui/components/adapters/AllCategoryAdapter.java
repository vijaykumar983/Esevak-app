package in.jploft.esevak.ui.components.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ViewDataBinding;

import java.util.ArrayList;

import in.jploft.esevak.R;
import in.jploft.esevak.pojo.AllCategoryData;
import in.jploft.esevak.ui.base.RecyclerBaseAdapter;
import in.jploft.esevak.utils.Constants;
import in.jploft.esevak.utils.Utility;

public class AllCategoryAdapter extends RecyclerBaseAdapter {
    private AppCompatActivity mActivity;
    private View.OnClickListener onClickListener;
    private ArrayList<AllCategoryData.Category> list;
    public  int selectedPos = 0;

    public AllCategoryAdapter(AppCompatActivity mActivity, View.OnClickListener onClickListener, ArrayList<AllCategoryData.Category> list) {
        this.mActivity = mActivity;
        this.onClickListener = onClickListener;
        this.list = list;
    }

    @Override
    protected int getLayoutIdForPosition(int position) {
        return R.layout.row_category;
    }

    @Override
    public Object getViewModel(int position) {
        return list.get(position);
    }

    @Override
    protected void putViewDataBinding(ViewDataBinding viewDataBinding, int position) {
        View view = viewDataBinding.getRoot();
        LinearLayout rootHeader = view.findViewById(R.id.rowCategoryItem);
        rootHeader.setTag(position);
        rootHeader.setOnClickListener(onClickListener);
        ImageView ivCategory = view.findViewById(R.id.ivCategory);
        //TextView tvCategoryTitle = view.findViewById(R.id.tvCategoryTitle);

        //Utility.loadImage(ivCategory, Constants.imageUrl + list.get(position).getImage());
        Utility.loadPicture(ivCategory, list.get(position).getImage());
        //tvCategoryTitle.setText(Utility.toTitleCase(list.get(position).getName()));
        //tvCategoryTitle.setSelected(true);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}

