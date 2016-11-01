package cnu.mobilesoftware.smartscheduler.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import cnu.mobilesoftware.smartscheduler.InsertScheduleActivity;
import cnu.mobilesoftware.smartscheduler.Interface.ITitle;
import cnu.mobilesoftware.smartscheduler.KFGD_SchedulerUI.KFGD_Scheduler;
import cnu.mobilesoftware.smartscheduler.KFGD_SchedulerUI.OnObservedSelectedLinearLayout;
import cnu.mobilesoftware.smartscheduler.KFGD_SchedulerUI.SelectedCell;
import cnu.mobilesoftware.smartscheduler.KFGD_SchedulerUI.SelectedLinearLayout;
import cnu.mobilesoftware.smartscheduler.R;

public class SchedulerFragment extends Fragment implements ITitle, OnObservedSelectedLinearLayout{

    //OnActivityResult
    public static final int REQUEST_INSERT_SCHEDULE = 100;
    public static final int RESULT_INSERT_SCHEDULE_SUCC = 101;
    public static final int RESULT_INSERST_SCHEDULE_CANCEL = 99;

    private final String mTitle = "Scheduler";

    KFGD_Scheduler scheduler;

    public static SchedulerFragment newInstance() {
        SchedulerFragment fragment = new SchedulerFragment();
        return fragment;
    }

    public SchedulerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scheduler, container, false);
        scheduler = (KFGD_Scheduler)view.findViewById(R.id.scheduler);
        scheduler.setOnObservedSelectedLinearLayoutList(this);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public void selectedNormalCellList(SelectedLinearLayout selectedLinearLayout, ArrayList<SelectedCell> sourceList, ArrayList<SelectedCell> selectedList) {
        startActivityForResult(new Intent(getContext(), InsertScheduleActivity.class), REQUEST_INSERT_SCHEDULE);
        //scheduler.mergeCellList(selectedLinearLayout, sourceList, selectedList);
    }

    @Override
    public void selectedMergedCell(SelectedLinearLayout selectedLinearLayout, ArrayList<SelectedCell> sourceList, SelectedCell selectedCell) {
        scheduler.divideCell(selectedLinearLayout, sourceList, selectedCell);
    }

    @Override
    public void error() {
        Toast.makeText(getContext(), "스케쥴을 입력할 수 없습니다.", Toast.LENGTH_SHORT).show();
    }
}
