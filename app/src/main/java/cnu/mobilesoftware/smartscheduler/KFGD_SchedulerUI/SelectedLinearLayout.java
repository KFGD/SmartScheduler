package cnu.mobilesoftware.smartscheduler.KFGD_SchedulerUI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by GwanYongKim on 2016-09-01.
 */
public class SelectedLinearLayout extends LinearLayout implements View.OnTouchListener{

    //외부에서 사용
    private OnObservedSelectedLinearLayout onObservedSelectedLinearLayout;
    private SchedulerUtils.DAY_TAG day_tag = SchedulerUtils.DAY_TAG.NONE;

    //내부에서 사용
    private int CELL_COUNT = 13;    //12에서 1 더 추가된것은 dummyCell을 위해서
    private final Context mContext;
    private boolean isDivideFlag = false;
    private float start_y, end_y;


    //내부외부 둘다
    private ArrayList<SelectedCell> sourceList = new ArrayList<>(CELL_COUNT);

    public SelectedLinearLayout(Context context) {
        super(context);
        this.mContext = context;
        initCellList();
    }

    public SelectedLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initCellList();
    }

    public SelectedLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initCellList();
    }

    public void setOnObservedSelectedLinearLayout(OnObservedSelectedLinearLayout onObservedSelectedLinearLayout) {
        this.onObservedSelectedLinearLayout = onObservedSelectedLinearLayout;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                start_y = event.getY();
                if (0 < selectedCellList(start_y, start_y).size() && null != onObservedSelectedLinearLayout) {
                    SelectedCell selectedcell = selectedCellList(start_y, start_y).get(0);
                    //병합된 셀인지 구분
                    if (selectedcell.getIsUsed()) {
                        isDivideFlag = true;
                    }else{
                        isDivideFlag = false;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                end_y = event.getY();
                ArrayList<SelectedCell> selectedCellList = selectedCellList(start_y, end_y);
                if (0<selectedCellList.size() && null != onObservedSelectedLinearLayout) {
                    if(isDivideFlag){
                        SelectedCell divideCell = selectedCellList.get(0);
                        onObservedSelectedLinearLayout.selectedMergedCell(this, sourceList, divideCell);
                    } else if (selectedListCanMerged(selectedCellList)) {
                        onObservedSelectedLinearLayout.selectedNormalCellList(this, sourceList, selectedCellList);
                    } else{
                        onObservedSelectedLinearLayout.error();
                    }
                }
                break;
        }
        return true;
    }

    private void initCellList() {
        //dummyCell
        SelectedCell dummy = new SelectedCell(mContext);
        dummy.setPosition(0);
        dummy.setWeight(0);
        sourceList.add(dummy);

        for (int i = 1; i < CELL_COUNT; ++i) {
            SelectedCell cell = new SelectedCell(mContext);
            cell.setPosition(i);
            cell.setWeight(1);
            sourceList.add(cell);
        }
        updateLayoutWithCell();
        this.setOnTouchListener(this);
    }

    public void updateLayoutWithCell() {
        this.removeAllViews();
        for (SelectedCell cell : sourceList) {
            cell.updateLayout();
            this.addView(cell);
        }
    }

    public boolean updateInsertDataInCell(ScheduleItem item){
        int position = item.startTime;
        int weight = item.endTime - item.startTime + 1;

        //병합 가능한지 여부 확인
        for(int i=position; i<=item.endTime; ++i){
            if(sourceList.get(i).getIsUsed()){
                return false;
            }
        }

        SelectedCell selectedCell = sourceList.get(position);
        selectedCell.setIsUsed(true);
        selectedCell.setWeight(weight);
        selectedCell.setSubjectName(item.subjectName);
        selectedCell.setClassNum(item.classNum);
        selectedCell.setProfessor(item.professor);
        selectedCell.setColorOfCell(item.colorOfCell);
        for(int i=position+1; i<=item.endTime; ++i){
            sourceList.get(i).setWeight(0);
            sourceList.get(i).setIsUsed(true);
        }
        return true;
    }

    private boolean selectedListCanMerged(ArrayList<SelectedCell> selectedList){
        boolean isCanMerged = true;
        for(SelectedCell cell : selectedList){
            if(cell.getIsUsed()){
                isCanMerged = false;
                break;
            }
        }
        return isCanMerged;
    }

    private ArrayList<SelectedCell> selectedCellList(float startPoint_y, float endPoint_y) {
        ArrayList<SelectedCell> selectList = new ArrayList<>();
        for (SelectedCell cell : this.sourceList) {
            if (cell.getY() >= startPoint_y - cell.getHeight() && cell.getY() <= endPoint_y)
                selectList.add(cell);
        }
        return selectList;
    }

    public void setDay_tagWithCell(SchedulerUtils.DAY_TAG day_tag){
        this.day_tag = day_tag;
        for(SelectedCell cell : sourceList)
            cell.setDayTag(day_tag);
    }
    public SchedulerUtils.DAY_TAG getDay_tag(){return this.day_tag;}
}
