package cnu.mobilesoftware.smartscheduler.Dialog;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.concurrent.ExecutionException;

import cnu.mobilesoftware.smartscheduler.DBHelper;
import cnu.mobilesoftware.smartscheduler.GroupItem;
import cnu.mobilesoftware.smartscheduler.R;
import cnu.mobilesoftware.smartscheduler.SmartSchedulerApplication;
import cnu.mobilesoftware.smartscheduler.WebDBHelper;

public class EnterGroupDialog extends AppCompatDialogFragment implements View.OnClickListener{

    private enum STAGE{
        INPUT_CODE,
        INPUT_GROUP_NAME,
    }

    LinearLayout linear_code, linear_name;
    TextInputLayout til_group_code, til_group_name;
    TextInputEditText tie_group_code, tie_group_name;

    public EnterGroupDialog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dialog_enter_group, container, false);
        initializeWidget(view);
        changeInputMode(STAGE.INPUT_CODE);
        return view;
    }

    private void initializeWidget(View view){

        linear_code = (LinearLayout)view.findViewById(R.id.linear_input_code);
        til_group_code = (TextInputLayout)view.findViewById(R.id.til_group_code);
        tie_group_code = (TextInputEditText)view.findViewById(R.id.tie_group_code);
        Button checkBtnCode = (Button)view.findViewById(R.id.btn_check_code);
        checkBtnCode.setOnClickListener(this);

        linear_name = (LinearLayout)view.findViewById(R.id.linear_input_name);
        til_group_name = (TextInputLayout)view.findViewById(R.id.til_group_title);
        tie_group_name = (TextInputEditText)view.findViewById(R.id.tie_group_title);
        Button checkBtnName = (Button)view.findViewById(R.id.btn_check_name);
        checkBtnName.setOnClickListener(this);
    }

    private void changeInputMode(STAGE stage){
        switch (stage){
            case INPUT_CODE:
                linear_code.setVisibility(View.VISIBLE);
                linear_name.setVisibility(View.GONE);
                break;
            case INPUT_GROUP_NAME:
                linear_code.setVisibility(View.GONE);
                linear_name.setVisibility(View.VISIBLE);
                break;
        }
    }

    private boolean ValidateGroupCode(){
        Boolean bReturn = true;
        final String code = tie_group_code.getText().toString();

        try {
            Boolean value = new AsyncTask<Void, Void, Boolean>(){
                @Override
                protected Boolean doInBackground(Void... strings) {
                    Boolean bool = true;
                    if(WebDBHelper.SELECTWEBDB("SELECTGROUPINFO", code).length() == 0) {
                        bool = false;
                    }
                    return bool;
                }
            }.execute().get();
            bReturn = value;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bReturn;
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_check_code: {
                boolean bValue = true;

                bValue = ValidateGroupCode();
                if(bValue) {
                    changeInputMode(STAGE.INPUT_GROUP_NAME);
                }
            }
                break;

            case R.id.btn_check_name: {
                boolean bValue = true;
                //GroupName 20글자 미만이 아닐 경우,
                bValue = ValidateGroupName();
                if (bValue) {
                    final String uuid = SmartSchedulerApplication.getUUID();
                    final String groupdId = tie_group_code.getText().toString();
                    final String groupTitle = tie_group_name.getText().toString();

                    //Http 그룹 참여
                    new AsyncTask<Void, Void, Void>(){

                        ProgressDialog progressDialog = null;

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            if(progressDialog == null) progressDialog = new ProgressDialog(getContext());
                            progressDialog.show();
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            if(progressDialog != null) progressDialog.dismiss();
                            progressDialog = null;
                        }

                        @Override
                        protected Void doInBackground(Void... voids) {
                            WebDBHelper.INSERTUSERGROUP(uuid, groupdId);
                            DBHelper.getInstance().insertGroupItemInDB(new GroupItem(groupdId, groupTitle));
                            return null;
                        }
                    }.execute();
                }
            }
                break;
        }
    }
}
