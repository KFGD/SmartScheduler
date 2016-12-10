package cnu.mobilesoftware.smartscheduler;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import cnu.mobilesoftware.smartscheduler.Fragment.MemoFragment;
import cnu.mobilesoftware.smartscheduler.Fragment.SchedulerFragment;
import cnu.mobilesoftware.smartscheduler.Fragment.TodayFragment;
import cnu.mobilesoftware.smartscheduler.Interface.ITitle;
import cnu.mobilesoftware.smartscheduler.KFGD_MemoUI.IRefresh;
import cnu.mobilesoftware.smartscheduler.KFGD_MemoUI.Memo;

public class MainActivity extends AppCompatActivity implements IRefresh {

    //Member of Data
    Calendar currentCalendar;
    HashMap<String, Memo> memoList;
    List decorators;

    //Member of Widgets
    //CustomCalendarView calendarView;
    TextView memoText;
    ImageButton memoBtn;
    Memo selectedMemo;
    String[] colorOfArray = new String[]{"#7784C2", "#C784C2", "#C7EBA8", "#71EBA8", "#7130A8", "#6D8FF5"};
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        //Initialize CustomCalendarView from layout
    }

    public void onClickShowCalendar(View target){

    }
    //Activity 생성 기본 과정
    private void init(){
        ViewPager viewPager = (ViewPager)findViewById(R.id.viewPager);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        setUpViewPagerAndTabLayout(viewPager, tabLayout);
/*        calendarView = (CustomCalendarView) findViewById(R.id.calendar_view);
        InitializeCalender();*/


    }

    private void setUpViewPagerAndTabLayout(ViewPager viewPager, TabLayout tabLayout){
        //Setting ViewPager
        SectionsPagerAdapter sectionPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        sectionPagerAdapter.appendFragment(MemoFragment.newInstance());
        sectionPagerAdapter.appendFragment(TodayFragment.newInstance());
        sectionPagerAdapter.appendFragment(SchedulerFragment.newInstance());
        viewPager.setAdapter(sectionPagerAdapter);
        viewPager.setCurrentItem(1);

        //Setting TabLayout
        tabLayout.setupWithViewPager(viewPager);
        for(int i=0; i<sectionPagerAdapter.getCount(); ++i){
            if(null == tabLayout.getTabAt(i))
                break;
            if(sectionPagerAdapter.getItem(i) instanceof ITitle)
                tabLayout.getTabAt(i).setText(((ITitle) sectionPagerAdapter.getItem(i)).getTitle());
        }
    }

    @Override
    public void Refresh() {

    }


    public final class SectionsPagerAdapter extends FragmentPagerAdapter{

        private final List<Fragment> pageFragment = new ArrayList<>(3);

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void appendFragment(Fragment fragment){
            pageFragment.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return pageFragment.get(position);
        }

        @Override
        public int getCount() {
            return pageFragment.size();
        }
    }

}
