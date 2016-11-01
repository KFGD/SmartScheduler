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
    public SchedulerUtils.DAY_TAG day_tag;

    //내부에서 사용
    private int CELL_COUNT = 13;    //12에서 1 더 추가된것은 dummyCell을 위해서
    private final Context mContext;
    private boolean isDivideFlag = false;
    private float start_y, end_y;


    //내부외부 둘다
    private ArrayList<SelectedCell> sourceList = new ArrayList<>(CELL_COUNT);
    private ArrayList<ScheduleItem> scheduleItems = new ArrayList<>();

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
                    if (selectedcell.getIsMerged()) {
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
        dummy.setWeight(0);
        dummy.setPosition(0);
        dummy.refreshSelectedCell();
        sourceList.add(dummy);

        for (int i = 1; i < CELL_COUNT; ++i) {
            SelectedCell cell = new SelectedCell(mContext);
            cell.setWeight(1);
            cell.setPosition(i);
            cell.refreshSelectedCell();
            sourceList.add(cell);
        }
        refreshSelectedLinearLayout();
        this.setOnTouchListener(this);
    }

    private void mappingItemAndCell(){

    }

    public void refreshSelectedLinearLayout() {
        this.removeAllViews();
        for (SelectedCell cell : sourceList) {
            this.addView(cell);
        }
    }
    private boolean selectedListCanMerged(ArrayList<SelectedCell> selectedList){
        boolean isCanMerged = true;
        for(SelectedCell cell : selectedList){
            if(cell.getIsMerged()){
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
}
