package cnu.mobilesoftware.smartscheduler.KFGD_MemoUI;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import cnu.mobilesoftware.smartscheduler.R;

/**
 * Created by 박종형 on 2016-11-10.
 */

public class MemoActivity extends AppCompatActivity implements IRefresh {

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
        //Initialize CustomCalendarView from layout

        //Memo
        memoText = (TextView) findViewById(R.id.memo_text);
        memoBtn = (ImageButton)findViewById(R.id.memo_btn);
        memoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != selectedMemo) {
                    EditMemoDialog memo = new EditMemoDialog(MemoActivity.this, selectedMemo, MemoActivity.this);
                    memo.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    memo.show();
                }
            }
        });
      //  refreshMemoList();
        decorators = new ArrayList();
  /*      decorators.add(new ColorDecorator());*/
        //Set Decorators
/*        calendarView.setDecorators(decorators);
        calendarView.refreshCalendar(currentCalendar);*/
        resetSelectedMemo();

    }


    //--------------------------Memo-----------------------------------
    @Override
    public void onResume(){
        super.onResume();
        Refresh();
    }
    @Override
    public void Refresh() {
      // refreshMemoList();
        resetSelectedMemo();
    }

 /*   private void InitializeCalender() {

        //Initialize calendar with date
        currentCalendar = Calendar.getInstance(Locale.getDefault());

        //Show monday as first date of week
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);

        //Show/hide overflow days of a month
        calendarView.setShowOverflowDate(false);

        //call refreshCalendar to update calendar the view
        calendarView.refreshCalendar(currentCalendar);

        //Handling custom calendar events
        calendarView.setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {
                //SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                //Toast.makeText(MainActivity.this, df.format(date), Toast.LENGTH_SHORT).show();
                memoBtn.setVisibility(View.VISIBLE);
                selectedMemo = memoList.get(df.format(date));
                if (null != selectedMemo)
                    memoText.setText(selectedMemo.getContent());
                else {
                    memoText.setText("일정없음");
                    selectedMemo = new Memo(df.format(date));
                }
            }

            @Override
            public void onMonthChanged(Date date) {
                SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");
                //Toast.makeText(MainActivity.this, df.format(date), Toast.LENGTH_SHORT).show();
                resetSelectedMemo();
            }
        });
    }*/

    private void resetSelectedMemo(){
        selectedMemo = null;
        memoText.setText("");
        memoBtn.setVisibility(View.GONE);
    }
/*

    private void refreshMemoList(){
        memoList = DBHelper.getInstance().getMemoListFromDB();
        if(null == memoList)
            memoList = new HashMap<>();
    }
*/

/*
    public class ColorDecorator implements DayDecorator {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        @Override
        public void decorate(DayView cell) {

            String day = format.format(cell.getDate());
            if(null != memoList.get(day)){
                int color = Color.parseColor(colorOfArray[count % colorOfArray.length]);
                cell.setBackgroundColor(color);
                count++;
            }else{
                int color = Color.parseColor("#D2D4E1");
                cell.setBackgroundColor(color);
            }
        }
    }
*/
}
