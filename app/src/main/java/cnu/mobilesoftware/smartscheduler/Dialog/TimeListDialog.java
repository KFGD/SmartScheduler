package cnu.mobilesoftware.smartscheduler.Dialog;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import cnu.mobilesoftware.smartscheduler.Fragment.ChatItem;
import cnu.mobilesoftware.smartscheduler.Fragment.PostFragment;
import cnu.mobilesoftware.smartscheduler.GroupDetailActivity;
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
    private WebDBHelper webdb = null;

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

        webdb = new WebDBHelper();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            String str = new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... voids) {
                    StringBuilder stringBuilder = webdb.SELECTWEBDB("SELECTGROUPBLACK", groupItem.group_id);
                    String text = "";
                    if(stringBuilder != null)
                        text = stringBuilder.toString();
                    return text;
                }
            }.execute().get();
            GrouopBlankFormat(str);
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
    private void GrouopBlankFormat(String s){
        String[][] result = new String[5][12];
        String[] tempArray = null;
        String tempString = "";
        try {
            JSONArray chatArray = null;
            JSONObject jsonObj = new JSONObject(s);
            chatArray = jsonObj.getJSONArray("result");
            for(int i=0; i<chatArray.length(); i++){
                JSONObject j = chatArray.getJSONObject(i);
                tempString = j.getString("plan");
                while(tempString.length()<12){
                    tempString = "0"+tempString;
                }
                tempArray = tempString.split("");
                for(int k=0; k<12; k++){
                    result[i][k] = tempArray[k+1];
                }
            }
        }catch (Exception e){}
        //결과는 result 이차원배열에 담긴다.
    }
}
