package cnu.mobilesoftware.smartscheduler.Fragment;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import cnu.mobilesoftware.smartscheduler.DBHelper;
import cnu.mobilesoftware.smartscheduler.Interface.ITitle;
import cnu.mobilesoftware.smartscheduler.KFGD_MemoUI.IRefresh;
import cnu.mobilesoftware.smartscheduler.KFGD_MemoUI.Memo;
import cnu.mobilesoftware.smartscheduler.KFGD_SchedulerUI.ScheduleItem;
import cnu.mobilesoftware.smartscheduler.KFGD_SchedulerUI.SchedulerUtils;
import cnu.mobilesoftware.smartscheduler.R;

public class TodayFragment extends Fragment implements ITitle, IRefresh {

    private final String mTitle = "Today";
    private ArrayList<ScheduleItem> todaySchedule = new ArrayList<>();
    TextView todayText,famousSaying;
    LinearLayout linearLayout;
    ScrollView scrollView;

    ArrayList<String> scheudleList = new ArrayList<String>();      // 배열리스트(스트링)
    ArrayList<String> todayMemoList = new ArrayList<String>();
    ListView listView;
    ArrayAdapter<String> adapter;

    Memo selectedMemo;
    HashMap<String, Memo> memoList;

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
        View view = inflater.inflate(R.layout.fragment_today, container, false);
        famousSaying = (TextView) view.findViewById(R.id.todayFamoueSaying);
        todayText = (TextView) view.findViewById(R.id.memo_text);
        linearLayout = (LinearLayout) view.findViewById(R.id.showSchedule);
        scrollView = (ScrollView) view.findViewById(R.id.schedulelistlayout);

       listView = (ListView)view.findViewById(R.id.schedulelist);


        //getListView();
        getfamousSaying();
        Refresh();

        return view;
    }


    //이 메소드를 호출할 때마다 멤버변수인 todaySchedule이 갱신됩니다.
    private void refreshScheduleData() {
        Calendar cal = Calendar.getInstance();//현재 날짜와 시간정보를 가져온다.
        int nWeek = cal.get(Calendar.DAY_OF_WEEK);  //1:일요일, 2: 월요일 ~ 7: 토요일

        //평일이 아닐 경우, 리턴
        if (2 > nWeek || 6 < nWeek)
            return;

        SchedulerUtils.DAY_TAG day_tag = SchedulerUtils.DAY_TAG.NONE;
        switch (nWeek) {
            case 2:
                day_tag = SchedulerUtils.DAY_TAG.MONDAY;
                break;
            case 3:
                day_tag = SchedulerUtils.DAY_TAG.TUESDAY;
                break;
            case 4:
                day_tag = SchedulerUtils.DAY_TAG.WEDNESDAY;
                break;
            case 5:
                day_tag = SchedulerUtils.DAY_TAG.THURSDAY;
                break;
            case 6:
                day_tag = SchedulerUtils.DAY_TAG.FRIDAY;
                break;
        }
        //Schduelt list
        ArrayList<ScheduleItem> dbData = DBHelper.getInstance().getScheduleItemWithDay_Tag(day_tag);
        ArrayList<ScheduleItem> tempData = new ArrayList<>();

        for (int i = 0; i < dbData.size(); ++i) {
            ScheduleItem item = dbData.get(i);

            if (item.startTime > item.endTime)
                continue;
            if (item.subjectName == null || item.subjectName.length() == 0)
                continue;
            tempData.add(item);
        }
        todaySchedule = tempData;
    }

    private void showScheudler() {
        if(selectedMemo == null && !todaySchedule.isEmpty()){
            scheudleList.clear();
            todayMemoList.clear();
            todayMemoList.add("\n" + "달력에 기록된 일정이 없습니다.");
            showSchedule();
            getListView();
            getTextList();
        }
        if(selectedMemo !=null && todaySchedule.isEmpty()){
            todayMemoList.clear();
            scheudleList.clear();
            scheudleList.add("\n" + "시간표에 기록된 시간표가 없습니다.");
            todayMemoList.add("\n" + "오늘의 일정:" + selectedMemo.getContent());
            getListView();
            getTextList();          //  nameList.clear();
        }
        if(selectedMemo ==null && todaySchedule.isEmpty()){
            todayMemoList.clear();
            scheudleList.clear();
            todayMemoList.add("\n" + "달력에 기록된 일정이 없습니다.");
            scheudleList.add("\n" + "시간표에 기록된 시간표가 없습니다.");
            getListView();
            getTextList();         //  nameList.clear();
        }
        if(selectedMemo !=null && !todaySchedule.isEmpty()){
            todayMemoList.clear();
            scheudleList.clear();
            showSchedule();
            todayMemoList.add("\n" + "오늘의 일정:" + selectedMemo.getContent());
            getListView();
            getTextList();
        }
    }

    private void showSchedule(){
        for (int i = 0; i < todaySchedule.size(); i++) {
            int startTime = todaySchedule.get(i).startTime;
            int endTime = todaySchedule.get(i).endTime;
            String subjectName = todaySchedule.get(i).subjectName;
            String professor = todaySchedule.get(i).professor;
            String show = subjectName + ":" + startTime + "시~" + endTime + "시 " + professor + "교수님";
            scheudleList.add(show);
        }

    }

    private void refreshScheduleMemo() {
        Date cal = Calendar.getInstance().getTime();//현재 날짜와 시간정보를 가져온다.
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        selectedMemo = memoList.get(df.format(cal));

    }

    private void refreshMemoList() {
        memoList = DBHelper.getInstance().getMemoListFromDB();
        if (null == memoList)
            memoList = new HashMap<>();
    }

    private void getListView(){
        scrollView.removeAllViews();
        listView = new ListView(getActivity());
        scrollView.addView(listView);  //linearLayout01 위에 생성
        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,scheudleList);
        listView.setAdapter(adapter);
    }

    //배열 리스트 가져오기
    private void getTextList() {
        linearLayout.removeAllViews();  //기존 모든 뷰를 모두 지운다

        DisplayMetrics dm = getResources().getDisplayMetrics();
        int left = Math.round(5 * dm.density);
        int right = Math.round(20 * dm.density);
        int top = Math.round(120 * dm.density);

        for (int i = 0; i < todayMemoList.size(); i++) {

            todayText.setText(todayMemoList.get(i));  //배열리스트 이용
            todayText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
            todayText.setTextColor(Color.parseColor("#D84315"));
            todayText.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(left,top,right,0);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.addView(todayText,params);
        }



    }

    private void getfamousSaying(){
        int day = Calendar.getInstance().get(Calendar.DATE);

        String[] famousString = getResources().getStringArray(R.array.FamousSaying);
        famousSaying.setText(famousString[day]);

    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public void Refresh() {
        refreshMemoList();
        refreshScheduleData();
        refreshScheduleMemo();
        showScheudler();
    }


}
