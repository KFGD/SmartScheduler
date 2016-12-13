package cnu.mobilesoftware.smartscheduler.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import cnu.mobilesoftware.smartscheduler.EditScheduleActivity;
import cnu.mobilesoftware.smartscheduler.InsertScheduleActivity;
import cnu.mobilesoftware.smartscheduler.Interface.ITitle;
import cnu.mobilesoftware.smartscheduler.KFGD_MemoUI.IRefresh;
import cnu.mobilesoftware.smartscheduler.KFGD_SchedulerUI.KFGD_Scheduler;
import cnu.mobilesoftware.smartscheduler.KFGD_SchedulerUI.OnObservedSelectedLinearLayout;
import cnu.mobilesoftware.smartscheduler.KFGD_SchedulerUI.ScheduleItem;
import cnu.mobilesoftware.smartscheduler.KFGD_SchedulerUI.SelectedCell;
import cnu.mobilesoftware.smartscheduler.KFGD_SchedulerUI.SelectedLinearLayout;
import cnu.mobilesoftware.smartscheduler.MainActivity;
import cnu.mobilesoftware.smartscheduler.R;

public class SchedulerFragment extends Fragment implements ITitle, OnObservedSelectedLinearLayout,IRefresh{

    //OnActivityResult
    public static final int REQUEST_INSERT_SCHEDULE = 100;
    public static final int RESULT_INSERT_SCHEDULE_SUCC = 101;

    public static final int REQUEST_EDIT_SCHEDULE = 200;
    public static final int RESULT_DELETE_SCHEDULE = 199;
    public static final int RESULT_EDIT_SCHEDULE = 201;

    private final String mTitle = "Scheduler";
    private MainActivity ownerActivity;
    //MemberVariable
    KFGD_Scheduler scheduler;

    public static SchedulerFragment newInstance() {
        SchedulerFragment fragment = new SchedulerFragment();
        return fragment;
    }

    public static SchedulerFragment newInstance(MainActivity ownerActivity) {
        SchedulerFragment fragment = new SchedulerFragment();
        fragment.ownerActivity = ownerActivity;
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
        scheduler.linkDB();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_INSERT_SCHEDULE:{
                if(resultCode == RESULT_INSERT_SCHEDULE_SUCC){
                    ScheduleItem item = data.getParcelableExtra("ITEM");
                    if( !scheduler.insertCellDataWithScheduleItem(item)){
                        Toast.makeText(getContext(), "시간표 추가가 불가능합니다. ", Toast.LENGTH_SHORT).show();
                    }
                    Refresh();
                }
            }
            case REQUEST_EDIT_SCHEDULE:{
                if(resultCode == RESULT_DELETE_SCHEDULE){
                    ScheduleItem item = data.getParcelableExtra("ITEM");
                    scheduler.deleteCellDataWithScheduleItem(item);
                    Refresh();
                }else if(resultCode == RESULT_EDIT_SCHEDULE){
                    ScheduleItem sourceItem = data.getParcelableExtra("SOURCE_ITEM");
                    ScheduleItem updateItem = data.getParcelableExtra("UPDATE_ITEM");
                    Refresh();
                    if(!scheduler.editCellDataWithScheduleItem(sourceItem, updateItem)){
                        Toast.makeText(getContext(), "시간표 수정이 불가능합니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public void selectedNormalCellList(SelectedLinearLayout selectedLinearLayout, ArrayList<SelectedCell> sourceList, ArrayList<SelectedCell> selectedList) {
        int startTime = selectedList.get(0).getPosition();
        int endTime = selectedList.get(selectedList.size()-1).getPosition();
        ScheduleItem item = new ScheduleItem(selectedLinearLayout.getDay_tag(), startTime, endTime);
        Intent intent = new Intent(getContext(), InsertScheduleActivity.class);
        intent.putExtra("ITEM", item);
        startActivityForResult(intent, REQUEST_INSERT_SCHEDULE);
    }

    @Override
    public void selectedMergedCell(SelectedLinearLayout selectedLinearLayout, ArrayList<SelectedCell> sourceList, SelectedCell selectedCell) {
        //scheduler.divideCell(selectedLinearLayout, sourceList, selectedCell);
        Intent intent = new Intent(getContext(), EditScheduleActivity.class);
        intent.putExtra("ITEM", selectedCell.getScheduleItem());
        startActivityForResult(intent, REQUEST_EDIT_SCHEDULE);
    }

    @Override
    public void error() {
        Toast.makeText(getContext(), "사용 중인 셀이 있습니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void Refresh() {
        ownerActivity.refreshToday();
    }
}
