package in.jploft.esevak.ui.components.adapters;

import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.databinding.ViewDataBinding;

import java.util.ArrayList;

import in.jploft.esevak.R;
import in.jploft.esevak.pojo.DashboardData;
import in.jploft.esevak.ui.base.RecyclerBaseAdapter;
import in.jploft.esevak.utils.Constants;
import in.jploft.esevak.utils.Utility;


public class OfferAdapter extends RecyclerBaseAdapter {
    private AppCompatActivity mActivity;
    private View.OnClickListener onClickListener;
    private ArrayList<DashboardData.Offer> list;


    public OfferAdapter(AppCompatActivity mActivity, View.OnClickListener onClickListener, ArrayList<DashboardData.Offer> list) {
        this.mActivity = mActivity;
        this.onClickListener = onClickListener;
        this.list = list;
    }

    @Override
    protected int getLayoutIdForPosition(int position) {
        return R.layout.row_offer;
    }

    @Override
    public Object getViewModel(int position) {
        return list.get(position);
    }

    @Override
    protected void putViewDataBinding(ViewDataBinding viewDataBinding, int position) {
        View view = viewDataBinding.getRoot();
        CardView rootHeader = view.findViewById(R.id.rowOfferItem);
        rootHeader.setTag(position);
        rootHeader.setOnClickListener(onClickListener);
        ImageView ivOffer = view.findViewById(R.id.ivOffer);

        //Utility.loadPicture(ivOffer, Constants.imageUrl + list.get(position).getImage());
        Utility.loadPicture(ivOffer, list.get(position).getImage());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}

