package cnu.mobilesoftware.smartscheduler.Dialog;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
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
        RadioGroup radioGroup = (RadioGroup)view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb_mon:adapter.changeDayList(0);break;
                    case R.id.rb_tue:adapter.changeDayList(1);break;
                    case R.id.rb_wed:adapter.changeDayList(2);break;
                    case R.id.rb_thu:adapter.changeDayList(3);break;
                    case R.id.rb_fri:adapter.changeDayList(4);break;
                }
            }
        });

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
            adapter.updateResources(GrouopBlankFormat(str));
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

        private ArrayList<String>[] resources = new ArrayList[5];
        private int currentDayIndex = 0;

        public TimeItemAdapter(){
            for(int i=0; i<5; ++i)
                resources[i] = new ArrayList<>();
        }

        public void updateResources(ArrayList<String>[] resources){
            this.resources = resources;
            notifyDataSetChanged();
        }

        public void changeDayList(int i){
            currentDayIndex = i;
            notifyDataSetChanged();
        }

        @Override
        public TimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_time_item, parent, false);
            return new TimeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TimeViewHolder holder, int position) {
            final String item = resources[currentDayIndex].get(position);
            holder.linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String[] split = item.split(":");
                    int hour = Integer.parseInt(split[0]);
                    int min = Integer.parseInt(split[1]);
                    onSelectTimeItem.onSelectTimeItem(hour, min);
                    dismiss();
                }
            });
            holder.tv_time.setText(item);
        }

        @Override
        public int getItemCount() {
            return resources[currentDayIndex].size();
        }
    }

    public class TimeViewHolder extends RecyclerView.ViewHolder{

        private final LinearLayout linear;
        private final TextView tv_time;

        public TimeViewHolder(View itemView) {
            super(itemView);
            linear = (LinearLayout)itemView.findViewById(R.id.linear_time);
            tv_time = (TextView)itemView.findViewById(R.id.tv_time);
        }
    }

    public interface OnSelectTimeItem{
        public void onSelectTimeItem(int hour, int min);
    }
    private ArrayList<String>[] GrouopBlankFormat(String s){
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
        ArrayList<String>[] data = new ArrayList[5];
        for(int i=0; i<5; ++i){
            data[i] = new ArrayList<>();
            for(int j=0; j<12; ++j){
                if("1".equals(result[i][j]))
                    data[i].add(String.valueOf(j+9)+":00");
            }
        }
        return data;
    }

}
