package cnu.mobilesoftware.smartscheduler.Dialog;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cnu.mobilesoftware.smartscheduler.R;

public class EnterGroupDialog extends AppCompatDialogFragment {

    TextInputLayout til_group_name;
    TextInputEditText tie_group_name;

    public EnterGroupDialog() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dialog_enter_group, container, false);
        return view;
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

}
