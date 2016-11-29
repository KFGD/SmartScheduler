package cnu.mobilesoftware.smartscheduler.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cnu.mobilesoftware.smartscheduler.Interface.ITitle;
import cnu.mobilesoftware.smartscheduler.R;

public class NoticeFragment extends Fragment implements ITitle{

    private final String mTtitle = "Notice";

    public static NoticeFragment newInstance(){
        NoticeFragment fragment = new NoticeFragment();
        return fragment;
    }

    public NoticeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notice, container, false);
        return view;
    }

    @Override
    public String getTitle() {
        return mTtitle;
    }
}
