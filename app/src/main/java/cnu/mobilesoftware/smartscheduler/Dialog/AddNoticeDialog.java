package cnu.mobilesoftware.smartscheduler.Dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import cnu.mobilesoftware.smartscheduler.R;

/**
 * Created by GwanYongKim on 2016-12-10.
 */

public class AddNoticeDialog extends AppCompatDialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_notice_add, container, false);
        return view;
    }

}
