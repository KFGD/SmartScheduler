package cnu.mobilesoftware.smartscheduler.KFGD_SchedulerUI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import cnu.mobilesoftware.smartscheduler.R;


/**
 * Created by Administrator on 2016-10-31.
 */

public class KFGD_Scheduler extends LinearLayout {

    private final int COLUMN_COUNT = 5;
    ArrayList<SelectedLinearLayout> columns = new ArrayList<>(COLUMN_COUNT);
    int idOfColumns[] = {R.id.dayOfMon, R.id.dayOfTue, R.id.dayOfWed, R.id.dayOfThu, R.id.dayOfFri};
    SchedulerUtils.DAY_TAG day_tag[] = {SchedulerUtils.DAY_TAG.MONDAY, SchedulerUtils.DAY_TAG.TUESDAY, SchedulerUtils.DAY_TAG.WEDNESDAY, SchedulerUtils.DAY_TAG.THURSDAY, SchedulerUtils.DAY_TAG.FRIDAY};

    public KFGD_Scheduler(Context context) {
        super(context);
        InitLayout(context);
    }

    public KFGD_Scheduler(Context context, AttributeSet attrs) {
        super(context, attrs);
        InitLayout(context);
    }

    public KFGD_Scheduler(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitLayout(context);
    }

    public void setOnObservedSelectedLinearLayoutList(OnObservedSelectedLinearLayout onObservedSelecteLinearyLayout){
        for(int i=0; i<columns.size(); ++i)
            columns.get(i).setOnObservedSelectedLinearLayout(onObservedSelecteLinearyLayout);
    }

    private void InitLayout(Context context) {

        View v = LayoutInflater.from(context).inflate(R.layout.ui_scheduler, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(v);
        for(int i=0 ;i<idOfColumns.length; ++i) {
            SelectedLinearLayout column = (SelectedLinearLayout) v.findViewById(idOfColumns[i]);
            column.setDay_tagWithCell(day_tag[i]);
            columns.add(column);
        }
        for(int i=0; i<columns.size(); ++i)
            columns.get(i).updateLayoutWithCell();
    }

    public boolean insertCellDataWithScheduleItem(ScheduleItem item){
        int columnIndex = SchedulerUtils.convertStringToDAY_TAG(item.day).ordinal();
        SelectedLinearLayout selectedLinearLayout = columns.get(columnIndex-1);
        if(!selectedLinearLayout.updateInsertDataInCell(item)){
            return false;
        }
        selectedLinearLayout.updateLayoutWithCell();
        return true;
    }

    public boolean deleteCellDataWithScheduleItem(ScheduleItem item){
        int columnIndex = SchedulerUtils.convertStringToDAY_TAG(item.day).ordinal();
        SelectedLinearLayout selectedLinearLayout = columns.get(columnIndex-1);
        selectedLinearLayout.updateDeleteDataInCell(item);
        selectedLinearLayout.updateLayoutWithCell();
        return true;
    }

    public boolean editCellDataWithScheduleItem(ScheduleItem sourceItem, ScheduleItem updateItem){
        int columnIndex = SchedulerUtils.convertStringToDAY_TAG(sourceItem.day).ordinal();
        SelectedLinearLayout selectedLinearLayout = columns.get(columnIndex-1);
        if(!selectedLinearLayout.updateEditDataInCell(sourceItem, updateItem)){
            return false;
        }
        selectedLinearLayout.updateLayoutWithCell();
        return true;
    }
}
