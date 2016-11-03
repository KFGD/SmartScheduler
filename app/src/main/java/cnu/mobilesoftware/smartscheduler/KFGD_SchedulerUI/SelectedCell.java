package cnu.mobilesoftware.smartscheduler.KFGD_SchedulerUI;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import cnu.mobilesoftware.smartscheduler.R;

/**
 * Created by GwanYongKim on 2016-08-31.
 */
public class SelectedCell extends FrameLayout {

    private View cell;
    private TextView tv_subjectName;
    private TextView tv_classNum;

    private boolean isUsed = false;
    ScheduleItem item = new ScheduleItem(SchedulerUtils.DAY_TAG.NONE, 0, 0);

    public SelectedCell(Context context) {
        super(context);
        Init(context);
    }

    public SelectedCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init(context);
    }

    public SelectedCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init(context);
    }

    private void Init(Context context) {
        cell = LayoutInflater.from(context).inflate(R.layout.ui_selected_cell, null, false);
        addView(cell);
        tv_subjectName = (TextView)cell.findViewById(R.id.tv_subjectName);
        tv_classNum = (TextView)cell.findViewById(R.id.tv_classNum);

        int padding = (int)getResources().getDimension(R.dimen.SelectedCell_padding);
        this.setPadding(padding, padding, padding, padding);

        LinearLayout.LayoutParams LP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0, 1);
        this.setLayoutParams(LP);
        cell.setBackgroundColor(Color.parseColor(item.colorOfCell));
    }

    public void updateLayout(){
        if(null == item)
            return;

        //셀 크기 및 색 조정
        int weight = item.endTime - item.startTime+1;
        LinearLayout.LayoutParams LP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0, weight);
        this.setLayoutParams(LP);
        cell.setBackgroundColor(Color.parseColor(item.colorOfCell));

        //텍스트 바꾸기
        tv_subjectName.setText(item.subjectName);
        tv_classNum.setText(item.classNum);
    }

    public ScheduleItem getScheduleItem(){return this.item;}
    public void setDayTag(SchedulerUtils.DAY_TAG dayTag){this.item.day = dayTag.name();}
    public SchedulerUtils.DAY_TAG getDayTag(){return SchedulerUtils.convertStringToDAY_TAG(this.item.day);}
    public void setEndPosition(int endPosition) {this.item.endTime = endPosition;}
    public int getPosition() {return item.startTime;}
    public void setPosition(int position){this.item.startTime = position;}
    public boolean getIsUsed(){return this.isUsed;}
    public void setIsUsed(boolean isUsed){this.isUsed = isUsed;}
    public void setSubjectName(String subjectName){this.item.subjectName = subjectName;}
    public void setClassNum(String classNum){this.item.classNum = classNum;}
    public void setProfessor(String professor){this.item.professor = professor;}
    public void setColorOfCell(String colorOfCell){this.item.colorOfCell = colorOfCell;}
    public void reset() {
        this.isUsed = false;
        this.item = new ScheduleItem(SchedulerUtils.convertStringToDAY_TAG(this.item.day), this.item.startTime, this.item.startTime);
    }
}
