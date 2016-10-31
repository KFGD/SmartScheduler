package cnu.mobilesoftware.smartscheduler.KFGD_SchedulerUI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016-10-31.
 */

public class KFGD_Scheduler extends LinearLayout {

    private final int COLUMN_COUNT = 5;
    ArrayList<SelectedLinearLayout> columns = new ArrayList<>(COLUMN_COUNT);

    public KFGD_Scheduler(Context context) {
        super(context);
        Init(context);
    }

    public KFGD_Scheduler(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init(context);
    }

    private void Init(Context context) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        for(int i=0 ;i<COLUMN_COUNT; ++i) {
            SelectedLinearLayout v = new SelectedLinearLayout(context);
            v.setOrientation(VERTICAL);
            v.setLayoutParams(lp);
            addView(v);
            columns.add(v);
        }
    }

    public void setOnCellSelectedListener(OnCellSelectedListener onCellSelectedListener){
        for(int i=0; i<COLUMN_COUNT; ++i){
            columns.get(i).setOnCellSelectedListener(onCellSelectedListener);
        }
    }

    public void mergeCellList(SelectedLinearLayout selectedLinearLayout, ArrayList<SelectedCell> sourceList, ArrayList<SelectedCell> selectedList){
        if(0>=selectedList.size())
            return;

        int weight = selectedList.size();
        int startPosition = selectedList.get(0).position;
        for(int i=selectedList.size()-1; i>=1; --i)
            sourceList.remove(selectedList.get(i).position);
        for(int i=startPosition+1 ; i<sourceList.size(); ++i)
            sourceList.get(i).position -= weight-1;

        sourceList.get(selectedList.get(0).position).weight = weight;
        sourceList.get(selectedList.get(0).position).setIsUsed(true);
        sourceList.get(selectedList.get(0).position).refreshSelectedCell();
        selectedLinearLayout.refreshSelectedLinearLayout();
    }

    public void divideCell(SelectedLinearLayout selectedLinearLayout, ArrayList<SelectedCell> sourceList, SelectedCell selectedCell){
        int startPosition = selectedCell.position;
        int weight = selectedCell.weight;
        for(int i=startPosition+1; i<startPosition+weight; ++i)
            sourceList.add(i, new SelectedCell(getContext(), i));
        for(int i=startPosition+weight; i<sourceList.size(); ++i)
            sourceList.get(i).position += weight-1;
        sourceList.get(selectedCell.position).weight = 1;
        sourceList.get(selectedCell.position).setIsUsed(false);
        sourceList.get(selectedCell.position).refreshSelectedCell();
        selectedLinearLayout.refreshSelectedLinearLayout();
    }
}
