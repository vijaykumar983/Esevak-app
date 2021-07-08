package in.jploft.esevak.ui.components.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ViewDataBinding;

import java.util.ArrayList;

import in.jploft.esevak.R;
import in.jploft.esevak.pojo.AllCategoryData;
import in.jploft.esevak.pojo.SubCategoryData;
import in.jploft.esevak.ui.base.RecyclerBaseAdapter;
import in.jploft.esevak.utils.Utility;

public class SubCategoryAdapter extends RecyclerBaseAdapter {
    private AppCompatActivity mActivity;
    private View.OnClickListener onClickListener;
    private ArrayList<SubCategoryData.Data.CategoryItem> list;
    public  int selectedPos = 0;

    public SubCategoryAdapter(AppCompatActivity mActivity, View.OnClickListener onClickListener, ArrayList<SubCategoryData.Data.CategoryItem> list) {
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

