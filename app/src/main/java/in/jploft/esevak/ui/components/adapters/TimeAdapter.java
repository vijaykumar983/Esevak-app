package in.jploft.esevak.ui.components.adapters;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ViewDataBinding;

import java.util.List;

import in.jploft.esevak.R;
import in.jploft.esevak.ui.base.RecyclerBaseAdapter;
import in.jploft.esevak.utils.Constants;
import in.jploft.esevak.utils.Utility;


public class TimeAdapter extends RecyclerBaseAdapter {
    private AppCompatActivity mActivity;
    private View.OnClickListener onClickListener;
    private int currentPosition = -1;
    private List<String> times, times1;
    private String am_pm = "", am_pm1 = "";
    private String time = null, time1 = null, time24 = null;


    public TimeAdapter(AppCompatActivity mActivity, View.OnClickListener onClickListener, List<String> times, List<String> times1) {
        this.mActivity = mActivity;
        this.onClickListener = onClickListener;
        this.times = times;
        this.times1 = times1;
    }

    @Override
    protected int getLayoutIdForPosition(int position) {
        return R.layout.row_day;
    }

    @Override
    public Object getViewModel(int position) {
        return times.get(position);
    }

    @Override
    protected void putViewDataBinding(ViewDataBinding viewDataBinding, int position) {
        View view = viewDataBinding.getRoot();
        LinearLayout rootHeader = view.findViewById(R.id.rowDayItem);
        rootHeader.setTag(position);
        rootHeader.setOnClickListener(onClickListener);
        TextView txtDayTime = view.findViewById(R.id.txtDayTime);
        Log.e("Test", "selected id - " + Constants.selectedTime);


        if (currentPosition == position) {
            Constants.selectedTime = time + " " + am_pm + " - " + time1 + " " + am_pm1;
            txtDayTime.setTextColor(mActivity.getResources().getColor(R.color.colorWhite));
            rootHeader.setBackgroundColor(mActivity.getResources().getColor(R.color.color_FF653B));

        } else {
            txtDayTime.setTextColor(mActivity.getResources().getColor(R.color.colorBlack));
            rootHeader.setBackgroundColor(mActivity.getResources().getColor(R.color.colorWhite));
        }

        try {
            if (Utility.getTimeInHour(times.get(position)) >= 12) {
                am_pm = "p.m.";
                time = Utility.getChange12Format(times.get(position));
            } else {
                am_pm = "a.m.";
                time = times.get(position);
            }
            if (Utility.getTimeInHour(times1.get(position)) >= 12) {
                am_pm1 = "p.m.";
                time1 = Utility.getChange12Format(times1.get(position));
                time24 = times1.get(position);
            } else {
                am_pm1 = "a.m.";
                time1 = times1.get(position);
                time24 = times1.get(position);
            }

            txtDayTime.setText(time + " " + am_pm + " - " + time1 + " " + am_pm1);
            txtDayTime.setSelected(true);

        } catch (Exception e) {
            e.printStackTrace();
        }


        txtDayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPosition = position; //getting the position of the item to expand it
                notifyDataSetChanged(); //reloding the list
            }
        });

    }


    @Override
    public int getItemCount() {
        return times.size();
    }
}

