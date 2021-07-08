package in.jploft.esevak.ui.components.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import in.jploft.esevak.R;
import in.jploft.esevak.pojo.DashboardData;
import in.jploft.esevak.utils.Constants;
import in.jploft.esevak.utils.Utility;

public class BannerAdapter extends PagerAdapter {
    private AppCompatActivity mActivity;
    private LayoutInflater layoutInflater;
    private View.OnClickListener onClickListener;
    private ArrayList<DashboardData.Banner> list;

    public BannerAdapter(AppCompatActivity mActivity, View.OnClickListener onClickListener, ArrayList<DashboardData.Banner> list) {
        this.mActivity = mActivity;
        this.onClickListener = onClickListener;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.row_banner, null);

        ImageView bannerImg = view.findViewById(R.id.imgBanner);
        //Utility.loadPicture(bannerImg, Constants.imageUrl + list.get(position).getImage());
        Utility.loadPicture(bannerImg,  list.get(position).getImage());

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }
}
