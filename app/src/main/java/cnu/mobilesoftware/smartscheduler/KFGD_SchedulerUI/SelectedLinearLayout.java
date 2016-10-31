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
public class SelectedLinearLayout extends LinearLayout implements View.OnTouchListener {

    private OnCellSelectedListener onCellSelectedListener;

    private int CELL_COUNT = 12;

    private final Context mContext;
    private ArrayList<SelectedCell> sourceList = new ArrayList<>(CELL_COUNT);

    private boolean flag = false;
    private float start_x, start_y;
    private float end_x, end_y;

    public SelectedLinearLayout(Context context) {
        super(context);
        this.mContext = context;
        initVerticalCellList();
        this.setOnTouchListener(this);
    }

    public SelectedLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initVerticalCellList();
        this.setOnTouchListener(this);
    }

    public void setOnCellSelectedListener(OnCellSelectedListener onCellSelectedListener) {
        this.onCellSelectedListener = onCellSelectedListener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                start_x = event.getX();
                start_y = event.getY();
                if (0 < selectedCellList(start_y, start_y).size() && null != onCellSelectedListener) {
                    SelectedCell selectedcell = selectedCellList(start_y, start_y).get(0);
                    if (selectedcell.getIsUsed()) {
                        onCellSelectedListener.selectedUsedCell(this, sourceList, selectedcell);
                        flag = false;
                    } else {
                        flag = true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                end_x = event.getX();
                end_y = event.getY();
                ArrayList<SelectedCell> selectedCellList = selectedCellList(start_y, end_y);
                selectedCellList = removeIsUsedCellInCellList(selectedCellList);
                if (0<selectedCellList.size() && null != onCellSelectedListener) {
                    if (flag) {
                        onCellSelectedListener.selectedNormalCellList(this, sourceList, selectedCellList);
                    }
                }
                break;
        }
        return true;
    }

    private void initVerticalCellList() {
        for (int i = 0; i < CELL_COUNT; ++i) {
            SelectedCell cell = new SelectedCell(mContext, i);
            cell.position = i;
            sourceList.add(cell);
        }
        //sourceList.get(0).setCanSelected(false);
        refreshSelectedLinearLayout();
    }

    public void refreshSelectedLinearLayout() {
        this.removeAllViews();
        for (SelectedCell cell : sourceList) {
            cell.refreshSelectedCell();
            this.addView(cell);
        }
    }
    private ArrayList<SelectedCell> removeIsUsedCellInCellList(ArrayList<SelectedCell> selectedList){
        ArrayList<SelectedCell> newList = new ArrayList<>();
        for(SelectedCell cell : selectedList){
            if(!cell.getIsUsed())
                newList.add(cell);
        }
        return newList;
    }

    private ArrayList<SelectedCell> selectedCellList(float startPoint_y, float endPoint_y) {
        ArrayList<SelectedCell> selectList = new ArrayList<>();
        for (SelectedCell cell : this.sourceList) {
            if (cell.getY() >= startPoint_y - cell.getHeight() && cell.getY() <= endPoint_y)
                if (cell.getCanSelected())
                    selectList.add(cell);
        }
        return selectList;
    }
}
