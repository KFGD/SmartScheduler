package cnu.mobilesoftware.smartscheduler.KFGD_SchedulerUI;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-10-31.
 */

public interface OnCellSelectedListener {
    public void selectedNormalCellList(SelectedLinearLayout selectedLinearLayout, ArrayList<SelectedCell> sourceList, ArrayList<SelectedCell> selectedList);
    public void selectedUsedCell(SelectedLinearLayout selectedLinearLayout, ArrayList<SelectedCell> sourceList, SelectedCell selectedCell);
}
