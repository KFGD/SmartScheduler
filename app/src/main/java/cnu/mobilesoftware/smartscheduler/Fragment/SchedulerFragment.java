package cnu.mobilesoftware.smartscheduler.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import cnu.mobilesoftware.smartscheduler.Interface.ITitle;
import cnu.mobilesoftware.smartscheduler.KFGD_SchedulerUI.KFGD_Scheduler;
import cnu.mobilesoftware.smartscheduler.KFGD_SchedulerUI.OnObservedSelectedLinearLayout;
import cnu.mobilesoftware.smartscheduler.KFGD_SchedulerUI.SelectedCell;
import cnu.mobilesoftware.smartscheduler.KFGD_SchedulerUI.SelectedLinearLayout;
import cnu.mobilesoftware.smartscheduler.R;

public class SchedulerFragment extends Fragment implements ITitle{

    private final String mTitle = "Scheduler";

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
        final KFGD_Scheduler scheduler = (KFGD_Scheduler)view.findViewById(R.id.scheduler);
        scheduler.setOnObservedSelecteLinearyLayoutList(new OnObservedSelectedLinearLayout() {
            @Override
            public void selectedNormalCellList(SelectedLinearLayout selectedLinearLayout, ArrayList<SelectedCell> sourceList, ArrayList<SelectedCell> selectedList) {
                scheduler.mergeCellList(selectedLinearLayout, sourceList, selectedList);
            }

            @Override
            public void selectedMergedCell(SelectedLinearLayout selectedLinearLayout, ArrayList<SelectedCell> sourceList, SelectedCell selectedCell) {
                scheduler.divideCell(selectedLinearLayout, sourceList, selectedCell);
            }

            @Override
            public void error() {
                Toast.makeText(getContext(), "스케쥴을 입력할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }
}
