package cnu.mobilesoftware.smartscheduler;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import cnu.mobilesoftware.smartscheduler.Dialog.AddGroupDialog;
import cnu.mobilesoftware.smartscheduler.Dialog.EnterGroupDialog;
import cnu.mobilesoftware.smartscheduler.Dialog.NickNameDialog;
import cnu.mobilesoftware.smartscheduler.KFGD_SchedulerUI.ScheduleItem;
import cnu.mobilesoftware.smartscheduler.KFGD_SchedulerUI.SchedulerUtils;

public class GroupActivity extends AppCompatActivity {

    GroupViewAdapter groupViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("참여 그룹");

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        groupViewAdapter = new GroupViewAdapter();
        recyclerView.setAdapter(groupViewAdapter);
    }

    public void onResume() {
        super.onResume();
        refresh();
    }

    public void onClickAddGroup(View view) {
        FragmentManager fm = getSupportFragmentManager();
        AddGroupDialog dialog = new AddGroupDialog();
        dialog.show(fm, "");
    }

    public void onClickEnterGroup(View view) {
        FragmentManager fm = getSupportFragmentManager();
        EnterGroupDialog dialog = new EnterGroupDialog();
        dialog.show(fm, "");
    }


    public void onClickNick(View view){
        FragmentManager fm = getSupportFragmentManager();
        NickNameDialog dialog = new NickNameDialog();
        dialog.show(fm, "");
    }

    public void onClickSyncSchedule(View view) {

        //시간표 서버에 전송

        final StringBuilder result = getStringScheduler();
        Log.i("info", result.toString());
        new AsyncTask<Void, Void, Void>() {
            ProgressDialog progressDialog = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (progressDialog == null)
                    progressDialog = new ProgressDialog(GroupActivity.this);
                progressDialog.setTitle("서버와 통신중입니다.");
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... voids) {

                WebDBHelper.INSERTUSERPLAN(result.toString());
                Log.i("info", result.toString());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressDialog.dismiss();
                if (progressDialog != null)
                    progressDialog = null;
            }
        }.execute();
    }

    @NonNull
    private StringBuilder getStringScheduler() {
        SchedulerUtils.DAY_TAG day_tags[] = {SchedulerUtils.DAY_TAG.MONDAY,
                SchedulerUtils.DAY_TAG.TUESDAY, SchedulerUtils.DAY_TAG.WEDNESDAY, SchedulerUtils.DAY_TAG.THURSDAY,
                SchedulerUtils.DAY_TAG.FRIDAY};

        final String blank = "000000000000";

        StringBuilder schedulerItems[] = {new StringBuilder(blank), new StringBuilder(blank),
                new StringBuilder(blank), new StringBuilder(blank), new StringBuilder(blank)};

        StringBuilder result = new StringBuilder("");

        try {
            result.append(URLEncoder.encode("uuid", "UTF-8"));

            result.append("=");
            result.append(URLEncoder.encode(SmartSchedulerApplication.getUUID(), "UTF-8"));
            for (int i = 0; i < day_tags.length; ++i) {
                ArrayList<ScheduleItem> activateItems = getScheduleItems(day_tags[i]);
                result.append("&");

                for (ScheduleItem item : activateItems) {
                    //9시는 1교시이며 startTime은 1이다
                    int start = item.startTime - 1;
                    int end = item.endTime;
                    StringBuilder value = new StringBuilder("");
                    for (int j = start; j < end; ++j)
                        value.append("1");
                    schedulerItems[i].replace(start, end, value.toString());
                }
                //Log.i("info", schedulerItems[i].toString());
                result.append(URLEncoder.encode(day_tags[i].name().substring(0, 3).toLowerCase(), "UTF-8"));    //mon
                result.append("=");//=
                result.append(URLEncoder.encode(schedulerItems[i].toString(), "UTF-8"));//000000000000
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    //일정이 있는 cell만 반환
    @NonNull
    private ArrayList<ScheduleItem> getScheduleItems(SchedulerUtils.DAY_TAG day_tag) {
        ArrayList<ScheduleItem> dbData = DBHelper.getInstance().getScheduleItemWithDay_Tag(day_tag);
        ArrayList<ScheduleItem> tempData = new ArrayList<>();

        for (int i = 0; i < dbData.size(); ++i) {
            ScheduleItem item = dbData.get(i);

            if (item.startTime > item.endTime)
                continue;
            if (item.subjectName == null || item.subjectName.length() == 0)
                continue;
            tempData.add(item);
        }
        return tempData;
    }

    private void refresh() {
        new AsyncTask<Void, Void, ArrayList<GroupItem>>() {
            ProgressDialog progressDialog = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (progressDialog == null)
                    progressDialog = new ProgressDialog(GroupActivity.this);
                progressDialog.setTitle("서버와 통신중입니다.");
                progressDialog.show();
            }

            @Override
            protected ArrayList<GroupItem> doInBackground(Void... voids) {
                StringBuilder stringBuilder = WebDBHelper.SELECTWEBDB("SELECTUSERGROUP", SmartSchedulerApplication.getUUID());
                ArrayList<GroupItem> serverItems = new ArrayList<GroupItem>();
                try {
                    JSONObject rootObject = new JSONObject(stringBuilder.toString());
                    JSONArray result = rootObject.getJSONArray("result");
                    for(int i=0; i<result.length(); ++i){
                        JSONObject object = result.getJSONObject(i);
                        String id = object.get("groupid").toString();
                        serverItems.add(new GroupItem(id));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ArrayList<GroupItem> localItems = DBHelper.getInstance().getGroupItemInDB();
                for(GroupItem itemA : serverItems){
                    for(GroupItem itemB : localItems){
                        if(itemA.group_id.equals(itemB.group_id))
                            itemA.group_title = itemB.group_title;
                    }
                }
                return serverItems;
            }

            @Override
            protected void onPostExecute(ArrayList<GroupItem> arrayList) {
                super.onPostExecute(arrayList);
                groupViewAdapter.updateResources(arrayList);
                progressDialog.dismiss();
                if (progressDialog != null)
                    progressDialog = null;
            }
        }.execute();
    }

    private class GroupViewAdapter extends RecyclerView.Adapter<ViewHolder> {

        private ArrayList<GroupItem> resources = new ArrayList<>();

        public GroupViewAdapter() {
        /*    for (int i=0; i<10; ++i)
                resources.add(new GroupItem("Titlte_"+i));*/
        }

        public void updateResources(ArrayList<GroupItem> groupItems) {
            this.resources = groupItems;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), GroupDetailActivity.class);
                    intent.putExtra("GROUP_ITEM", resources.get(position));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                GroupActivity.this, holder.iv, ViewCompat.getTransitionName(holder.iv));
                        startActivity(intent, options.toBundle());
                    } else {
                        startActivity(intent);
                    }
                }
            });

            holder.cv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return false;
                }
            });

            holder.ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GroupActivity.this);
                    builder.setTitle("그룹 나가기");
                    builder.setMessage(resources.get(position).group_title + " 그룹을 정말 나가시겠습니까?\n그룹을 나가시더라도 해당 그룹에 작성하신 글과 댓글은 자동으로 삭제되지 않습니다.");
                    builder.setPositiveButton("나가기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.setNegativeButton("취소", null);
                    builder.show();
                }
            });

            holder.tv.setText(resources.get(position).group_title);
        }

        @Override
        public int getItemCount() {
            return resources.size();
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        private final CardView cv;
        private final ImageView iv;
        private final ImageButton ib;
        private final TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.item_cv);
            iv = (ImageView) itemView.findViewById(R.id.item_iv);
            ib = (ImageButton) itemView.findViewById(R.id.item_ib_clear);
            tv = (TextView) itemView.findViewById(R.id.item_tv_title);
        }
    }
}
