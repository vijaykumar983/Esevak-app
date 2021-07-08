package in.jploft.esevak.ui.components.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import java.util.ArrayList;

import in.jploft.esevak.R;
import in.jploft.esevak.databinding.RowDrawerBinding;
import in.jploft.esevak.pojo.DrawerData;
import in.jploft.esevak.ui.base.RecyclerBaseAdapter;


public class DrawerAdapter extends RecyclerBaseAdapter {
    private AppCompatActivity context;
    private View.OnClickListener onClickListener;
    private ArrayList<DrawerData> list;
    public  int selectedPos = 0;
    private RowDrawerBinding rowDrawerBinding;


    public DrawerAdapter(AppCompatActivity context, ArrayList<DrawerData> list, View.OnClickListener onClickListener) {
        this.context = context;
        this.list = list;
        this.onClickListener = onClickListener;

    }

    @Override
    protected int getLayoutIdForPosition(int position) {
        return R.layout.row_drawer;
    }

    @Override
    public Object getViewModel(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        rowDrawerBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), viewType, parent, false);
        return new RecyclerViewHolder(rowDrawerBinding);
    }

    @Override
    protected void putViewDataBinding(ViewDataBinding viewDataBinding, int position) {
        View view = viewDataBinding.getRoot();
        LinearLayout llMain = view.findViewById(R.id.ll_main);
        TextView txtDrawerName = llMain.findViewById(R.id.txtDrawerName);

        if (viewDataBinding == rowDrawerBinding) {
             llMain.setTag(position);
              llMain.setOnClickListener(onClickListener);
            if (selectedPos == position) {
              //  llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.color_B7B7B7));
                txtDrawerName.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            } else {
               //  llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                 txtDrawerName.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
            }
             txtDrawerName.setText(list.get(position).getName());
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

