package cnu.mobilesoftware.smartscheduler;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cnu.mobilesoftware.smartscheduler.Dialog.AddGroupDialog;
import cnu.mobilesoftware.smartscheduler.Dialog.EnterGroupDialog;
import cnu.mobilesoftware.smartscheduler.Dialog.NickNameDialog;

public class GroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("참여 그룹");

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        GroupViewAdapter groupViewAdapter = new GroupViewAdapter();
        recyclerView.setAdapter(groupViewAdapter);
    }

    public void onClickAddGroup(View view){
        FragmentManager fm = getSupportFragmentManager();
        AddGroupDialog dialog = new AddGroupDialog();
        dialog.show(fm, "");
    }

    public void onClickEnterGroup(View view){
        FragmentManager fm = getSupportFragmentManager();
        EnterGroupDialog dialog = new EnterGroupDialog();
        dialog.show(fm, "");
    }

    public void onClickNickName(View view){
        FragmentManager fm = getSupportFragmentManager();
        NickNameDialog dialog = new NickNameDialog();
        dialog.show(fm, "");
    }

    public void onClickSyncSchedule(View view){
        //시간표 서버에 전송
    }

    private class GroupViewAdapter extends RecyclerView.Adapter<ViewHolder>{

        private ArrayList<GroupItem> resources = new ArrayList<>();

        public GroupViewAdapter(){
            for (int i=0; i<10; ++i)
                resources.add(new GroupItem("Titlte_"+i));
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
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                GroupActivity.this, holder.iv, ViewCompat.getTransitionName(holder.iv));
                        startActivity(intent, options.toBundle());
                    }else{
                        startActivity(intent);
                    }
                }
            });

            holder.ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GroupActivity.this);
                    builder.setTitle("그룹 나가기");
                    builder.setMessage(resources.get(position).group_title+" 그룹을 정말 나가시겠습니까?\n그룹을 나가시더라도 해당 그룹에 작성하신 글과 댓글은 자동으로 삭제되지 않습니다.");
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

    private class ViewHolder extends RecyclerView.ViewHolder{

        private final CardView cv;
        private final ImageView iv;
        private final ImageButton ib;
        private final TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.item_cv);
            iv = (ImageView) itemView.findViewById(R.id.item_iv);
            ib = (ImageButton)itemView.findViewById(R.id.item_ib_clear);
            tv = (TextView)itemView.findViewById(R.id.item_tv_title);
        }
    }
}
