package cnu.mobilesoftware.smartscheduler.Fragment;
import cnu.mobilesoftware.smartscheduler.GroupDetailActivity;
import cnu.mobilesoftware.smartscheduler.SmartSchedulerApplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import cnu.mobilesoftware.smartscheduler.Interface.ITitle;
import cnu.mobilesoftware.smartscheduler.R;
import cnu.mobilesoftware.smartscheduler.WebDBHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment implements ITitle{

    private final String mTitle = "Post";
    private GroupDetailActivity ownerActivity;

    private WebDBHelper webdb;

    private RecyclerView showchat;
    public static PostFragment newInstance(GroupDetailActivity ownerActivity){
        PostFragment fragment = new PostFragment();
        fragment.ownerActivity = ownerActivity;
        return fragment;
    }
    public PostFragment() {
        // Required empty public constructor
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            UpdateChat();
        }
    };
    @Override
    public void onStart() {
        super.onStart();
        Thread myThread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        handler.sendMessage(handler.obtainMessage());
                        Thread.sleep(1000);
                    } catch (Throwable t) {
                    }
                }
            }
        });
        myThread.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        // Inflate the layout for this fragment
        webdb = new WebDBHelper();
        showchat = (RecyclerView) view.findViewById(R.id.showchat);
        showchat.setLayoutManager(new LinearLayoutManager(ownerActivity));

        return view;
    }

    private void UpdateChat(){
        new AsyncTask<Void, Void, String>(){
            @Override
            protected String doInBackground(Void... voids) {
                StringBuilder stringBuilder = webdb.SELECTWEBDB("SELECTBOARD", ownerActivity.getGroupItem().group_id);
                String text = "";
                if(stringBuilder != null)
                    text = stringBuilder.toString();
                return text;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                ArrayList<ChatItem> chatItems = new ArrayList<ChatItem>();
                try {
                    JSONArray chatArray = null;
                    JSONObject jsonObj = new JSONObject(s);
                    chatArray = jsonObj.getJSONArray("result");
                    for(int i=0; i<chatArray.length(); i++){
                        JSONObject j = chatArray.getJSONObject(i);
                        chatItems.add(new ChatItem(j.getString("name"),j.getString("content")));
                    }
                    ChatAdapter chatAdapter = new ChatAdapter();
                    chatAdapter.updateResources(chatItems);
                    showchat.setAdapter(chatAdapter);
                    showchat.getLayoutManager().scrollToPosition(showchat.getAdapter().getItemCount()-1);
                }catch (Exception e){}
            }
        }.execute();

    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

        private ArrayList<ChatItem> resources;

        public void updateResources(ArrayList<ChatItem> resources){
            this.resources = resources;
            notifyDataSetChanged();
        }

        @Override
        public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_chat_adapter, parent, false);
            return new ChatViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ChatViewHolder holder, int position) {
            ChatItem item = resources.get(position);
            holder.username.setText(item.name);
            holder.usercontent.setText(item.content);
            if(ownerActivity.getName().equals(item.name)) {
                holder.usercontent.setBackgroundResource(R.drawable.mytalk);
                holder.layout.setGravity(Gravity.RIGHT);
            }
        }

        @Override
        public int getItemCount() {
            return resources.size();
        }

        public class ChatViewHolder extends RecyclerView.ViewHolder{

            final TextView username;
            final TextView usercontent;
            final LinearLayout layout;

            public ChatViewHolder(View itemView) {
                super(itemView);

                username = (TextView)itemView.findViewById(R.id.username);
                usercontent = (TextView)itemView.findViewById(R.id.usercontent);
                layout = (LinearLayout)itemView.findViewById(R.id.activity_chat_adapter);
            }
        }
    }
}

