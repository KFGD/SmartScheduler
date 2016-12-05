package cnu.mobilesoftware.smartscheduler.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import cnu.mobilesoftware.smartscheduler.CardItem;
import cnu.mobilesoftware.smartscheduler.Interface.ITitle;
import cnu.mobilesoftware.smartscheduler.R;

public class NoticeFragment extends Fragment implements ITitle{

    private final String mTtitle = "Notice";

    RecyclerView recyclerView;

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
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    private class NoticeViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private ArrayList<CardItem.BasicCardItem> resources = new ArrayList<>();

        public NoticeViewAdapter(@NonNull ArrayList resources){
            this.resources = resources;
        }

        @Override
        public int getItemViewType(int position) {
            //return super.getItemViewType(position);
            return resources.get(position).tag.ordinal();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

    private class PeopleCardHolder extends RecyclerView.ViewHolder{

        public PeopleCardHolder(View itemView) {
            super(itemView);
        }
    }

    private class NoticeCardHolder extends RecyclerView.ViewHolder{

        public NoticeCardHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public String getTitle() {
        return mTtitle;
    }
}
