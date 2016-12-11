package cnu.mobilesoftware.smartscheduler.Dialog;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import cnu.mobilesoftware.smartscheduler.GroupDetailActivity;
import cnu.mobilesoftware.smartscheduler.R;
import cnu.mobilesoftware.smartscheduler.WebDBHelper;

/**
 * Created by GwanYongKim on 2016-12-10.
 */

public class AddNoticeDialog extends AppCompatDialogFragment implements View.OnClickListener, View.OnFocusChangeListener{

    TextInputLayout til_meeting_day, til_meeting_time_hour, til_meeting_time_min, til_meeting_topic;
    TextInputEditText tie_meeting_day, tie_meeting_time_hour, tie_meeting_time_min, tie_meeting_topic;

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private GroupDetailActivity ownerActivity;
    private WebDBHelper webdb;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_notice_add, container, false);
        initializeWidget(view);
        ownerActivity = (GroupDetailActivity)getActivity();
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ib_meeting_day: onClickCalendarImgBtn(tie_meeting_day); break;
            case R.id.ib_meeting_time: onClickTimeImgBtn(tie_meeting_time_hour, tie_meeting_time_min); break;
            case R.id.btn_check:onClickCheckBtn();break;
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()){
            case R.id.tie_meeting_day:
                if(b) convertDateToNumber(tie_meeting_day);
                else convertNumberToDate(tie_meeting_day);
                break;
        }
    }

    private void onClickTimeImgBtn(final TextInputEditText editTextHour, final TextInputEditText editTextMin){
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                editTextHour.setText(String.valueOf(hour));
                editTextMin.setText(String.valueOf(min));
            }
        }, 0, 0, false);
        timePickerDialog.show();
    }

    private void onClickCalendarImgBtn(final TextInputEditText editText) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Calendar date = Calendar.getInstance();
                date.set(i, i1, i2);
                editText.setText(format.format(date.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    private void onClickCheckBtn(){
        final String groupid = ownerActivity.getGroupItem().group_id;
        final String date = tie_meeting_day.getText().toString();
        final String time = tie_meeting_time_hour.getText().toString() +" : "+ tie_meeting_time_min.getText().toString();
        final String topic = tie_meeting_topic.getText().toString();
        Log.d("fuck", date+", "+time+", "+topic);
        new AsyncTask<Void, Void, String>(){
            @Override
            protected String doInBackground(Void... voids) {
                StringBuilder stringBuilder = webdb.INSERTNOTICE(groupid, date, time, topic);
                String text = "";
                if(stringBuilder != null)
                    text = stringBuilder.toString();
                return text;
            }
        }.execute();
        dismiss();
    }
    private void convertNumberToDate(TextInputEditText textInputEditText) {
        String str = textInputEditText.getText().toString();
        if(8!=str.length())
            return;
        int year = Integer.parseInt(str.substring(0,4));
        int month = Integer.parseInt(str.substring(4, 6))-1;
        int day = Integer.parseInt(str.substring(6, 8));
        Calendar date = Calendar.getInstance();
        date.set(year, month, day);
        str = format.format(date.getTime());
        textInputEditText.setText(str);
    }

    private void convertDateToNumber(TextInputEditText textInputEditText) {
        String str = textInputEditText.getText().toString();
        str = str.replaceAll("-","");
        textInputEditText.setText(str);
    }

    private void initializeWidget(View view){
        Calendar calendar = Calendar.getInstance();
        String date = format.format(calendar.getTime());
        til_meeting_day = (TextInputLayout)view.findViewById(R.id.til_meeting_day);
        til_meeting_day.setHint("모임날짜(형식: " + date +")");
        til_meeting_time_hour = (TextInputLayout)view.findViewById(R.id.til_meeting_time_hour);
        til_meeting_time_hour.setHint("시");
        til_meeting_time_min = (TextInputLayout)view.findViewById(R.id.til_meeting_time_min);
        til_meeting_time_min.setHint("분");
        til_meeting_topic = (TextInputLayout)view.findViewById(R.id.til_meeting_topic);

        tie_meeting_day = (TextInputEditText)view.findViewById(R.id.tie_meeting_day);
        tie_meeting_day.setText(date);
        tie_meeting_day.setOnFocusChangeListener(this);
        tie_meeting_time_hour = (TextInputEditText)view.findViewById(R.id.tie_meeting_time_hour);
        tie_meeting_time_min = (TextInputEditText)view.findViewById(R.id.tie_meeting_time_min);
        tie_meeting_topic = (TextInputEditText)view.findViewById(R.id.tie_meeting_topic);

        view.findViewById(R.id.ib_meeting_day).setOnClickListener(this);
        view.findViewById(R.id.ib_meeting_time).setOnClickListener(this);
        view.findViewById(R.id.btn_check).setOnClickListener(this);

        webdb = new WebDBHelper();
    }

}
