package cnu.mobilesoftware.smartscheduler.Dialog;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import cnu.mobilesoftware.smartscheduler.R;
import cnu.mobilesoftware.smartscheduler.SmartSchedulerApplication;
import cnu.mobilesoftware.smartscheduler.WebDBHelper;

import static cnu.mobilesoftware.smartscheduler.R.id.tie_group_nickName;
import static cnu.mobilesoftware.smartscheduler.R.id.til_group_nickName;

/**
 * Created by 박종형 on 2016-12-10.
 */

public class NickNameDialog extends AppCompatDialogFragment implements View.OnClickListener {


    private LinearLayout linear_code, linear_name;
    private TextInputLayout textInputLayout;
    private TextInputEditText textInputNickName;
    private WebDBHelper webdb;
    private String nickName;

    public NickNameDialog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dialog_nickname, container, false);
        webdb = new WebDBHelper();
        initializeWidget(view);
        return view;
    }

    private void initializeWidget(View view) {

        linear_code = (LinearLayout) view.findViewById(R.id.linear_input_code);
        textInputLayout = (TextInputLayout) view.findViewById(til_group_nickName);
        textInputNickName = (TextInputEditText) view.findViewById(tie_group_nickName);
        Button checkNikCode = (Button) view.findViewById(R.id.btn_nick_check);
        checkNikCode.setOnClickListener(this);

    }

    private boolean ValidateGroupName() {
        nickName = textInputNickName.getText().toString();
        boolean bReturn = true;
        if (nickName.length() == 0) {
            textInputNickName.setError("닉네입을 입력해주시기 바랍니다");
            bReturn = false;
        } else if (20 < nickName.length()) {
            textInputNickName.setError("닉네임은 20글자 이하로 작성해주시기 바랍니다");
            bReturn = false;
        }
        return bReturn;
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_nick_check) {
            boolean bValue = true;
            //GroupName 20글자 미만이 아닐 경우,
            bValue = ValidateGroupName();
            if (bValue) {
                new AsyncTask<Void, Void, String>(){
                    @Override
                    protected String doInBackground(Void... voids) {
                        String uuid = SmartSchedulerApplication.getUUID();
                        StringBuilder stringBuilder = webdb.INSERTUSERINFO(uuid, nickName);
                        String text = "";
                        if(stringBuilder != null)
                            text = stringBuilder.toString();
                        return text;
                    }
                }.execute();
                dismiss();
            }
        }

    }
}