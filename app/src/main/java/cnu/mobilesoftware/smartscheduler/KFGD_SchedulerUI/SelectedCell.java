package cnu.mobilesoftware.smartscheduler.KFGD_SchedulerUI;

import android.content.Context;
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

    private TextView tv_subjectName;
    private TextView tv_classNum;

    private boolean isMerge = false;
    private int position;   //1: 8시, 2: 9시...
    private int weight;

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
        View v = LayoutInflater.from(context).inflate(R.layout.ui_selected_cell, null, false);
        addView(v);
        tv_subjectName = (TextView)v.findViewById(R.id.tv_subjectName);
        tv_classNum = (TextView)v.findViewById(R.id.tv_classNum);
        refreshSelectedCell();
    }

    public void refreshSelectedCell(){
        LinearLayout.LayoutParams LP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0, weight);
        this.setLayoutParams(LP);
    }

    public void setIsMerged(boolean isMerged){this.isMerge = isMerged;}
    public boolean getIsMerged(){return this.isMerge;}
    public int getWeight() {
        return weight;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }
    public int getPosition() {
        return position;
    }
    public void setPosition(int position) {
        this.position = position;
    }
}
