package cnu.mobilesoftware.smartscheduler.KFGD_SchedulerUI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

import cnu.mobilesoftware.smartscheduler.DBHelper;

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
        dummy.setEndTime(-1);
        sourceList.add(dummy);

        for (int i = 1; i < CELL_COUNT; ++i) {
            SelectedCell cell = new SelectedCell(mContext);
            cell.setPosition(i);
            cell.setEndTime(i);
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

    public void setDBData(ArrayList<ScheduleItem> items){
        for(ScheduleItem item : items){
            int startTime = item.startTime;
            SelectedCell cell = sourceList.get(startTime);
            cell.setEndTime(item.endTime);

            //추후, 바꿔야할 코드, 지금은 임시방편
            if(item.startTime != item.endTime)
                cell.setIsUsed(true);

            cell.setSubjectName(item.subjectName);
            cell.setClassNum(item.classNum);
            cell.setProfessor(item.professor);
            cell.setColorOfCell(item.colorOfCell);
        }
        updateLayoutWithCell();
    }

    public boolean updateInsertDataInCell(ScheduleItem item){
        int position = item.startTime;

        //병합 가능한지 여부 확인
        for(int i=position; i<=item.endTime; ++i){
            if(sourceList.get(i).getIsUsed()){
                return false;
            }
        }

        SelectedCell selectedCell = sourceList.get(position);
        selectedCell.setIsUsed(true);
        selectedCell.setEndTime(item.endTime);
        selectedCell.setSubjectName(item.subjectName);
        selectedCell.setClassNum(item.classNum);
        selectedCell.setProfessor(item.professor);
        selectedCell.setColorOfCell(item.colorOfCell);
        for(int i=position+1; i<=item.endTime; ++i){
            int startPosition = sourceList.get(i).getPosition();
            sourceList.get(i).setEndTime(startPosition-1);
            sourceList.get(i).setIsUsed(true);
        }

        //DB Data link 부분
        ArrayList<ScheduleItem> items = new ArrayList<>();
        for(SelectedCell cell : sourceList){
            items.add(cell.getScheduleItem());
        }
        DBHelper.getInstance().insertScheduleItemsOfDay(items);

        return true;
    }

    public boolean updateDeleteDataInCell(ScheduleItem item){
        int position = item.startTime;
        for(int i= position; i<=item.endTime; ++i){
            sourceList.get(i).reset();
        }

        //DB Data link 부분
        ArrayList<ScheduleItem> items = new ArrayList<>();
        for(SelectedCell cell : sourceList){
            items.add(cell.getScheduleItem());
        }
        DBHelper.getInstance().insertScheduleItemsOfDay(items);

        return true;
    }

    public boolean updateEditDataInCell(ScheduleItem sourceItem, ScheduleItem updateItem){
        boolean bReturn = false;
        //수정이 불가능할 경우, 데이터 복원을 위해서 데이터 복사
        ScheduleItem tempItem = new ScheduleItem(SchedulerUtils.convertStringToDAY_TAG(sourceItem.day), sourceItem.startTime, sourceItem.endTime,
                sourceItem.subjectName, sourceItem.classNum, sourceItem.professor, sourceItem.colorOfCell);

        //원본데이터 삭제
        updateDeleteDataInCell(sourceItem);

        if(updateInsertDataInCell(updateItem)){
            //수정된 데이터가 삽입이 가능하고 그것이 성공했을 경우
            bReturn = true;
        }else{
            //수정된 데이터가 삽입이 불가능하여 그것이 실패했을 경우
            updateInsertDataInCell(tempItem);
        }

        return bReturn;
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
