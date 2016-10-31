package cnu.mobilesoftware.smartscheduler.KFGD_SchedulerUI;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by GwanYongKim on 2016-08-31.
 */
public class SelectedCell extends FrameLayout {

    TextView textView;

    private boolean canSelected = true;
    private boolean isUsed = false;

    public int position = 0;
    public int weight = 1;

    public SelectedCell(Context context, int position) {
        super(context);
        Init(context, position);
    }

    private void Init(Context context, int position) {
        this.position = position;
        weight = 1;
        textView = new TextView(context);
        textView.setText("공란"+position);
        this.addView(textView);
        refreshSelectedCell();
    }

    public void refreshSelectedCell(){
        LinearLayout.LayoutParams LP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, weight);
        this.setLayoutParams(LP);
    }

    public void setCanSelected(boolean canSelected){
        this.canSelected = canSelected;
    }

    public boolean getCanSelected(){return this.canSelected;}

    public void setIsUsed(boolean isUsed){this.isUsed = isUsed;}

    public boolean getIsUsed(){return this.isUsed;}

    public void setText(String text){
        textView.setText(text);
    }
}
