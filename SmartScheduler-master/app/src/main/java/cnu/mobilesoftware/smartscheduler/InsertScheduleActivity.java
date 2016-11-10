package cnu.mobilesoftware.smartscheduler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cnu.mobilesoftware.smartscheduler.Fragment.SchedulerFragment;
import cnu.mobilesoftware.smartscheduler.KFGD_SchedulerUI.ScheduleItem;
import cnu.mobilesoftware.smartscheduler.KFGD_SchedulerUI.SchedulerUtils;

public class InsertScheduleActivity extends AppCompatActivity {

    TextView tv_day;
    EditText et_startTime, et_endTime, et_subjectName, et_classNum, et_professor;   //Data
    EditText et_cellOfColor;    //Cell

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_schedule);
        ScheduleItem item = getIntent().getParcelableExtra("ITEM");
        tv_day = (TextView)findViewById(R.id.tv_day);
        tv_day.setText(item.day);

        et_startTime = (EditText)findViewById(R.id.et_startTime);
        et_startTime.setText(String.valueOf(item.startTime));

        et_endTime = (EditText)findViewById(R.id.et_endTime);
        et_endTime.setText(String.valueOf(item.endTime));

        et_subjectName = (EditText)findViewById(R.id.et_subjectName);
        et_classNum = (EditText)findViewById(R.id.et_classNum);
        et_professor = (EditText)findViewById(R.id.et_professor);

        et_cellOfColor = (EditText)findViewById(R.id.et_colorOfCell);
        et_cellOfColor.setText(item.colorOfCell);
    }

    public void onClickFinishBtn(View v){
        ScheduleItem item = new ScheduleItem(SchedulerUtils.convertStringToDAY_TAG(tv_day.getText().toString()), Integer.parseInt(et_startTime.getText().toString()), Integer.parseInt(et_endTime.getText().toString()),
                et_subjectName.getText().toString(), et_classNum.getText().toString(), et_professor.getText().toString());
        item.colorOfCell = et_cellOfColor.getText().toString();
        Intent intent = new Intent();
        intent.putExtra("ITEM", item);
        setResult(SchedulerFragment.RESULT_INSERT_SCHEDULE_SUCC, intent);
        finish();
    }
}
