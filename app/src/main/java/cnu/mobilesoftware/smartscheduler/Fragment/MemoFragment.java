package cnu.mobilesoftware.smartscheduler.Fragment;


import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.stacktips.view.CalendarListener;
import com.stacktips.view.CustomCalendarView;
import com.stacktips.view.DayDecorator;
import com.stacktips.view.DayView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cnu.mobilesoftware.smartscheduler.DBHelper;
import cnu.mobilesoftware.smartscheduler.Interface.ITitle;
import cnu.mobilesoftware.smartscheduler.KFGD_MemoUI.EditMemoDialog;
import cnu.mobilesoftware.smartscheduler.KFGD_MemoUI.IRefresh;
import cnu.mobilesoftware.smartscheduler.KFGD_MemoUI.Memo;
import cnu.mobilesoftware.smartscheduler.MainActivity;
import cnu.mobilesoftware.smartscheduler.R;

public class MemoFragment extends Fragment implements ITitle, CalendarListener, IRefresh {

    private final String mTitle = "Memo";
    private CustomCalendarView calendarView;

    //Member of Data
    Calendar currentCalendar;
    HashMap<String, Memo> memoList;
    List decorators;

    //Member of Widgets
    //CustomCalendarView calendarView;
    TextView memoText;
    ImageButton memoBtn,memo_deleteBtn;
    Memo selectedMemo;
    String[] colorOfArray = new String[]{"#7784C2", "#C784C2", "#C7EBA8", "#71EBA8", "#7130A8", "#6D8FF5"};
    // String[] colorOfArray = new String[]{"#FFFFFF", "#FFFFFF", "#FFFFFF", "#FFFFFF", "#FFFFFF", "#FFFFFF"};
    private MainActivity ownerActivity;

    int count = 0;


//    public static MemoFragment newInstance() {
//        MemoFragment fragment = new MemoFragment();
//        return fragment;
//    }

    public static MemoFragment newInstance(MainActivity ownerActivity) {
        MemoFragment fragment = new MemoFragment();
        fragment.ownerActivity = ownerActivity;
        return fragment;
    }

    public MemoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memo, container, false);
        //여기가 Activity onCreate 부분입니다.

        //Initialize CustomCalendarView from layout
        calendarView = (CustomCalendarView) view.findViewById(R.id.calendar_view);
        initializeCalendar(view);

        memoText = (TextView)view.findViewById(R.id.memo_text);
        memoBtn = (ImageButton)view.findViewById(R.id.memo_btn);
        memo_deleteBtn = (ImageButton)view.findViewById(R.id.memo_delete_btn);

        memoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null != selectedMemo) {
                    EditMemoDialog memo = new EditMemoDialog(getContext(), selectedMemo, MemoFragment.this);
                    memo.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    memo.show();
                }
            }
        });

        memo_deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alt_bld = new AlertDialog.Builder(getActivity());


                alt_bld.setMessage("정말로 삭제하시겠습니까?").setNegativeButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                EditMemoDialog memo = new EditMemoDialog(getContext(), selectedMemo, MemoFragment.this);
                                memo.onDeleteMemoButton();
                            }
                        })
                        .setPositiveButton("아니오",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // Action for 'Yes' Button
                                        dialog.cancel();
                                    }
                                })
                ;
                AlertDialog alert = alt_bld.create();
                alert.setTitle("삭제 버튼");
                alert.show();
            }
        });


        refreshMemoList();
        decorators = new ArrayList();
        decorators.add(new ColorDecorator());
        //Set Decorators
        calendarView.setDecorators(decorators);
        calendarView.refreshCalendar(currentCalendar);
        resetSelectedMemo();



        return view;
    }

    private void initializeCalendar(View view) {
        //Initialize calendar with date
        currentCalendar = Calendar.getInstance(Locale.getDefault());
        //Show Monday as first date of week
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);
        //Show/hide overflow days of a month
        calendarView.setShowOverflowDate(false);
        //call refreshCalendar to update calendar the view
        calendarView.refreshCalendar(currentCalendar);
        //Handling custom calendar events
        calendarView.setCalendarListener(this);
    }

    private void resetSelectedMemo(){
        selectedMemo = null;
        memoText.setText("");
        memoBtn.setVisibility(View.GONE);
        memo_deleteBtn.setVisibility(View.GONE);
    }

    private void refreshMemoList(){
        memoList = DBHelper.getInstance().getMemoListFromDB();
        if(null == memoList)
            memoList = new HashMap<>();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onDateSelected(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        memoBtn.setVisibility(View.VISIBLE);
        selectedMemo = memoList.get(df.format(date));

        if (null != selectedMemo) {
            memoText.setText(selectedMemo.getContent());
            memo_deleteBtn.setVisibility(View.VISIBLE);
        }
        else{
            memo_deleteBtn.setVisibility(View.INVISIBLE);
            memoText.setText("일정없음");
            selectedMemo = new Memo(df.format(date));
        }

    }

    @Override
    public void onMonthChanged(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");
        Toast.makeText(getContext(), df.format(date), Toast.LENGTH_SHORT).show();
        resetSelectedMemo();
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public void Refresh() {

        refreshMemoList();
        resetSelectedMemo();
        ownerActivity.Refresh();

    }

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
}
