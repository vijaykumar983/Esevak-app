package in.jploft.esevak.ui.components.adapters;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ViewDataBinding;

import java.util.ArrayList;

import in.jploft.esevak.R;
import in.jploft.esevak.pojo.TimeSlotData;
import in.jploft.esevak.ui.base.RecyclerBaseAdapter;
import in.jploft.esevak.utils.Constants;
import in.jploft.esevak.utils.TimeInterface;


public class DayAdapter extends RecyclerBaseAdapter {
    private AppCompatActivity mActivity;
    private View.OnClickListener onClickListener;
    private ArrayList<TimeSlotData.TimeslotItem> list;
    private int currentPosition = -1;
    private boolean flag = false;
    private TimeInterface timeInterface;

    public DayAdapter(AppCompatActivity mActivity, View.OnClickListener onClickListener, ArrayList<TimeSlotData.TimeslotItem> list, TimeInterface timeInterface) {
        this.mActivity = mActivity;
        this.onClickListener = onClickListener;
        this.list = list;
        this.timeInterface = timeInterface;
    }

    @Override
    protected int getLayoutIdForPosition(int position) {
        return R.layout.row_day;
    }

    @Override
    public Object getViewModel(int position) {
        return list.get(position);
    }

    @Override
    protected void putViewDataBinding(ViewDataBinding viewDataBinding, int position) {
        View view = viewDataBinding.getRoot();
        LinearLayout rootHeader = view.findViewById(R.id.rowDayItem);
        rootHeader.setTag(position);
        rootHeader.setOnClickListener(onClickListener);
        TextView txtDayTime = view.findViewById(R.id.txtDayTime);
        Log.e("Test", "selected id - " + Constants.selectedDayId + " rev id - " + list.get(position).getId());


        if (currentPosition == position) {
            Constants.selectedDayId = list.get(position).getId();
            Constants.selectedDayName = list.get(position).getDay();
            txtDayTime.setTextColor(mActivity.getResources().getColor(R.color.colorWhite));
            rootHeader.setBackgroundColor(mActivity.getResources().getColor(R.color.color_FF653B));
            timeInterface.updateData(list.get(position).getStartTime(),list.get(position).getEndTime());

        } else {
            txtDayTime.setTextColor(mActivity.getResources().getColor(R.color.colorBlack));
            rootHeader.setBackgroundColor(mActivity.getResources().getColor(R.color.colorWhite));
        }

        txtDayTime.setText(list.get(position).getDay());
        txtDayTime.setSelected(true);
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
        return list.size();
    }
}

