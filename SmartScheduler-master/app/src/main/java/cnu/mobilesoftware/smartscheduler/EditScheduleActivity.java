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

public class EditScheduleActivity extends AppCompatActivity {

    TextView tv_day;
    EditText et_startTime, et_endTime, et_subjectName, et_classNum, et_professor;   //Data
    EditText et_cellOfColor;    //Cell
    ScheduleItem editItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_schedule);
        editItem = getIntent().getParcelableExtra("ITEM");
        tv_day = (TextView)findViewById(R.id.tv_day);
        tv_day.setText(editItem.day);

        et_startTime = (EditText)findViewById(R.id.et_startTime);
        et_startTime.setText(String.valueOf(editItem.startTime));

        et_endTime = (EditText)findViewById(R.id.et_endTime);
        et_endTime.setText(String.valueOf(editItem.endTime));

        et_subjectName = (EditText)findViewById(R.id.et_subjectName);
        et_subjectName.setText(String.valueOf(editItem.subjectName));
        et_classNum = (EditText)findViewById(R.id.et_classNum);
        et_classNum.setText(String.valueOf(editItem.classNum));
        et_professor = (EditText)findViewById(R.id.et_professor);
        et_classNum.setText(String.valueOf(editItem.professor));

        et_cellOfColor = (EditText)findViewById(R.id.et_colorOfCell);
        et_cellOfColor.setText(editItem.colorOfCell);
    }

    public void onClickDeleteBtn(View view){
        Intent intent = new Intent();
        intent.putExtra("ITEM", editItem);
        setResult(SchedulerFragment.RESULT_DELETE_SCHEDULE, intent);
        finish();
    }

    public void onClickEditBtn(View view){
        int startTime = Integer.parseInt(et_startTime.getText().toString());
        int endTime = Integer.parseInt(et_endTime.getText().toString());

        ScheduleItem item = new ScheduleItem(SchedulerUtils.convertStringToDAY_TAG(editItem.day), startTime, endTime,
                et_subjectName.getText().toString(), et_classNum.getText().toString(),
                et_professor.getText().toString(), et_cellOfColor.getText().toString()
        );
        Intent intent = new Intent();
        intent.putExtra("SOURCE_ITEM", editItem);
        intent.putExtra("UPDATE_ITEM",item);
        setResult(SchedulerFragment.RESULT_EDIT_SCHEDULE, intent);
        finish();
    }
}
