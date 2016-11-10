package cnu.mobilesoftware.smartscheduler.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;

import cnu.mobilesoftware.smartscheduler.DBHelper;
import cnu.mobilesoftware.smartscheduler.Interface.ITitle;
import cnu.mobilesoftware.smartscheduler.KFGD_SchedulerUI.ScheduleItem;
import cnu.mobilesoftware.smartscheduler.KFGD_SchedulerUI.SchedulerUtils;
import cnu.mobilesoftware.smartscheduler.R;

public class TodayFragment extends Fragment implements ITitle{

    private final String mTitle = "Today";
    private ArrayList<ScheduleItem> todaySchedule = new ArrayList<>();

    public static TodayFragment newInstance() {
        TodayFragment fragment = new TodayFragment();
        return fragment;
    }

    public TodayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_today, container, false);

        //여기가 Activity onCreate 부분입니다.

        refreshScheduleData();

        return view;
    }


    //이 메소드를 호출할 때마다 멤버변수인 todaySchedule이 갱신됩니다.
    private void refreshScheduleData(){
        Calendar cal = Calendar.getInstance();
        int nWeek = cal.get(Calendar.DAY_OF_WEEK);  //1:일요일, 2: 월요일 ~ 7: 토요일

        //평일이 아닐 경우, 리턴
        if(2>nWeek || 6<nWeek)
            return;

        SchedulerUtils.DAY_TAG day_tag = SchedulerUtils.DAY_TAG.NONE;
        switch (nWeek){
            case 2: day_tag = SchedulerUtils.DAY_TAG.MONDAY; break;
            case 3: day_tag = SchedulerUtils.DAY_TAG.TUESDAY; break;
            case 4: day_tag = SchedulerUtils.DAY_TAG.WEDNESDAY; break;
            case 5: day_tag = SchedulerUtils.DAY_TAG.THURSDAY; break;
            case 6: day_tag = SchedulerUtils.DAY_TAG.FRIDAY; break;
        }

        ArrayList<ScheduleItem> dbData = DBHelper.getInstance().getScheduleItemWithDay_Tag(day_tag);
        ArrayList<ScheduleItem> tempData = new ArrayList<>();
        for(int i=0; i<dbData.size(); ++i){
            ScheduleItem item = dbData.get(i);

            if(item.startTime > item.endTime)
                continue;
            if(item.subjectName == null || item.subjectName.length() == 0)
                continue;
            tempData.add(item);
        }
        todaySchedule = tempData;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }
}
