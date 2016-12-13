package cnu.mobilesoftware.smartscheduler.Dialog;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import cnu.mobilesoftware.smartscheduler.GroupItem;
import cnu.mobilesoftware.smartscheduler.R;
import cnu.mobilesoftware.smartscheduler.WebDBHelper;

/**
 * Created by GwanYongKim on 2016-12-12.
 */

public class TimeListDialog extends AppCompatDialogFragment {

    private OnSelectTimeItem onSelectTimeItem = null;
    private TimeItemAdapter adapter = null;
    private GroupItem groupItem = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_timelist, container, false);

        groupItem = (GroupItem) getArguments().get("GROUP_ITEM");

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TimeItemAdapter();
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            String str = new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... voids) {

                    return null;
                }
            }.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void setOnSelectTimeItem(OnSelectTimeItem onSelectTimeItem){
        this.onSelectTimeItem = onSelectTimeItem;
    }

    public class TimeItemAdapter extends RecyclerView.Adapter<TimeViewHolder>{

        private ArrayList<Date> resources = new ArrayList<>();

        public void updateResources(ArrayList<Date> resources){
            this.resources = resources;
            notifyDataSetChanged();
        }

        @Override
        public TimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(TimeViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return resources.size();
        }
    }

    public class TimeViewHolder extends RecyclerView.ViewHolder{

        private final TextView tv_time;

        public TimeViewHolder(View itemView) {
            super(itemView);
            tv_time = (TextView)itemView.findViewById(R.id.tv_time);
        }
    }


    public interface OnSelectTimeItem{
        public void onSelectTimeItem(int hour, int min);
    }
}
