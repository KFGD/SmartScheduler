package cnu.mobilesoftware.smartscheduler.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cnu.mobilesoftware.smartscheduler.Interface.ITitle;
import cnu.mobilesoftware.smartscheduler.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment implements ITitle{

    private final String mTitle = "Post";

    public static PostFragment newInstance(){
        PostFragment fragment = new PostFragment();
        return fragment;
    }

    public PostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public String getTitle() {
        return mTitle;
    }
}
