package cnu.mobilesoftware.smartscheduler;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class GroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        GroupViewAdapter groupViewAdapter = new GroupViewAdapter();
        recyclerView.setAdapter(groupViewAdapter);
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
