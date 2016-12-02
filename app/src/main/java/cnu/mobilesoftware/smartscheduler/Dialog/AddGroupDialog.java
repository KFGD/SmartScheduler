package cnu.mobilesoftware.smartscheduler.Dialog;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import cnu.mobilesoftware.smartscheduler.R;
import cnu.mobilesoftware.smartscheduler.SmartSchedulerApplication;
import cnu.mobilesoftware.smartscheduler.WebDBHelper;

/**
 * Created by GwanYongKim on 2016-11-11.
 */

public class AddGroupDialog extends AppCompatDialogFragment implements View.OnClickListener, View.OnFocusChangeListener{

    TextInputLayout til_end_day, til_group_name;
    TextInputEditText tie_end_day, tie_group_name;

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    WebDBHelper webdb = new WebDBHelper();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_group_add, container, false);
        initializeWidget(view);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //case R.id.ib_start_day: onClickCalendarImgBtn(tie_start_day); break;
            case R.id.ib_end_day: onClickCalendarImgBtn(tie_end_day); break;
            case R.id.btn_check:
                //확인 버튼 눌렀을 때,
                boolean bValue = true;
                if(!ValidateDate(tie_end_day)){
                    til_end_day.setError("날짜 형식에 맞춰주시기 바랍니다.");
                    bValue = false;
                }
                if(!ValidateGroupName()){
                    bValue = false;
                }

                if(bValue) {
                    //모든 조건이 클리어 될 때,
                    final String uuid = SmartSchedulerApplication.getUUID();
                    final String group_endDay = tie_end_day.getText().toString();
                    final String group_title = tie_group_name.getText().toString();
                    Log.i("info", "UUID: " + uuid + " / GROUP_END_DAY: " + group_endDay + " / GROUP_TITLE: " + group_title);
                    new AsyncTask<Void, Void, String>(){
                        @Override
                        protected String doInBackground(Void... voids) {
                            webdb.MAKEGROUP(group_endDay, uuid);
                            return null;
                        }
                        @Override
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);
                        }
                    }.execute();
                    dismiss();
                }
                break;
        }
    }

    private boolean ValidateDate(TextInputEditText textInputEditText) {
        String str = textInputEditText.getText().toString();
        str = str.replaceAll("-", "");

        if(8!=str.length())
            return false;

        boolean bReturn = true;
        int year = Integer.parseInt(str.substring(0,4));
        int month = Integer.parseInt(str.substring(4, 6));
        int day = Integer.parseInt(str.substring(6, 8));
        Calendar date = Calendar.getInstance();
        date.set(year, month, day);
        try {
            str = format.format(date.getTime());
        }catch (Exception e){
            bReturn = false;
        }
        return bReturn;
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

    private boolean ValidateGroupName() {
        String groupName = tie_group_name.getText().toString();
        boolean bReturn = true;
        if(1>groupName.length()){
            til_group_name.setError("그룹 이름을 입력해주시기 바랍니다");
            bReturn = false;
        }else if(20<groupName.length()){
            til_group_name.setError("그룹 이름은 20글자 이하로 작성해주시기 바랍니다");
            bReturn = false;
        }
        return bReturn;
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()){
            case R.id.tie_end_day:
                if(b) convertDateToNumber(tie_end_day);
                else convertNumberToDate(tie_end_day);
                break;
        }
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

    private void initializeWidget(View view) {
        Calendar calendar = Calendar.getInstance();
        String date = format.format(calendar.getTime());
        til_end_day = (TextInputLayout)view.findViewById(R.id.til_end_day);
        til_end_day.setHint("종료일(형식: "+date+")");
        til_group_name = (TextInputLayout)view.findViewById(R.id.til_group_title);

        tie_end_day = (TextInputEditText)view.findViewById(R.id.tie_end_day);
        tie_end_day.setText(date);
        tie_end_day.setOnFocusChangeListener(this);
        tie_group_name = (TextInputEditText)view.findViewById(R.id.tie_group_title);

        ImageButton ib_end_day = (ImageButton)view.findViewById(R.id.ib_end_day);
        Button checkBtn = (Button)view.findViewById(R.id.btn_check);

        ib_end_day.setOnClickListener(this);
        checkBtn.setOnClickListener(this);
    }

}
