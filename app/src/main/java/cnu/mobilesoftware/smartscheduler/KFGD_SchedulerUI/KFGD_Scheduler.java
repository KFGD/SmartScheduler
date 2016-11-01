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

    public void setOnObservedSelecteLinearyLayoutList(OnObservedSelectedLinearLayout onObservedSelecteLinearyLayout){
        for(int i=0; i<columns.size(); ++i)
            columns.get(i).setOnObservedSelectedLinearLayout(onObservedSelecteLinearyLayout);
    }

    private void InitLayout(Context context) {

        View v = LayoutInflater.from(context).inflate(R.layout.ui_scheduler, null, false);
        v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(v);
        for(int i=0 ;i<idOfColumns.length; ++i) {
            SelectedLinearLayout column = (SelectedLinearLayout) v.findViewById(idOfColumns[i]);
            columns.add(column);
        }
        for(int i=0; i<columns.size(); ++i)
            columns.get(i).refreshSelectedLinearLayout();
    }

    public void mergeCellList(SelectedLinearLayout selectedLinearLayout, ArrayList<SelectedCell> sourceList, ArrayList<SelectedCell> selectedList){
        if(selectedList.size()<=1)
            return;

        int startPosition = selectedList.get(0).getPosition();
        sourceList.get(startPosition).setWeight(selectedList.size());
        sourceList.get(startPosition).setIsMerged(true);
        for(int i=1; i < selectedList.size(); ++i){
            selectedList.get(i).setWeight(0);
            selectedList.get(i).setIsMerged(true);
        }
        for(int i=0; i<selectedList.size(); ++i)
            selectedList.get(i).refreshSelectedCell();
        selectedLinearLayout.refreshSelectedLinearLayout();
    }

    public void divideCell(SelectedLinearLayout selectedLinearLayout, ArrayList<SelectedCell> sourceList, SelectedCell selectedCell){
        if(selectedCell.getWeight() == 1)
            return;

        int startPosition = selectedCell.getPosition();
        int endPosition = startPosition + selectedCell.getWeight();
        for(int i = startPosition; i<endPosition; ++i){
            SelectedCell currentCell = sourceList.get(i);
            currentCell.setWeight(1);
            currentCell.setIsMerged(false);
            currentCell.refreshSelectedCell();
        }
        selectedLinearLayout.refreshSelectedLinearLayout();
    }

}
