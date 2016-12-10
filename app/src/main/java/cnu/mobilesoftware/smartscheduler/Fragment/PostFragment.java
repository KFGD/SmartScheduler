package cnu.mobilesoftware.smartscheduler.Fragment;
import cnu.mobilesoftware.smartscheduler.GroupDetailActivity;
import cnu.mobilesoftware.smartscheduler.SmartSchedulerApplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
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
    private EditText chatinput;
    private Button sendchat;
    private ListView showchat;
    private ChatAdapter chatAdapter;
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
                        Thread.sleep(3000);
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
        chatinput = (EditText) view.findViewById(R.id.chatinput);
        sendchat = (Button) view.findViewById(R.id.sendChat);
        showchat = (ListView) view.findViewById(R.id.showchat);
        sendchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String uuid = SmartSchedulerApplication.getUUID();
                final String content = chatinput.getText().toString();
                if(content.equals(""))
                    return;
                new AsyncTask<Void, Void, String>(){
                    ProgressDialog pd = new ProgressDialog(getContext());
                    @Override
                    protected String doInBackground(Void... voids) {
                        StringBuilder stringBuilder = webdb.INSERTBOARD("groupid", uuid, content);
                        String text = "";
                        if(stringBuilder != null)
                            text = stringBuilder.toString();
                        return text;
                    }
                }.execute();
                chatinput.setText("");
            }
        });
        return view;
    }

    private void UpdateChat(){
        new AsyncTask<Void, Void, String>(){
            @Override
            protected String doInBackground(Void... voids) {
                StringBuilder stringBuilder = webdb.SELECTWEBDB("SELECTBOARD", "groupid");
                String text = "";
                if(stringBuilder != null)
                    text = stringBuilder.toString();
                return text;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                ArrayList<String> namearray = new ArrayList<String>();
                ArrayList<String> contentarray = new ArrayList<String>();
                try {
                    JSONArray chatArray = null;
                    JSONObject jsonObj = new JSONObject(s);
                    chatArray = jsonObj.getJSONArray("result");
                    for(int i=0; i<chatArray.length(); i++){
                        JSONObject j = chatArray.getJSONObject(i);
                        namearray.add(j.getString("name"));
                        contentarray.add(j.getString("content"));
                    }

                    ChatAdapter oldchat = (ChatAdapter)showchat.getAdapter();
                    ChatAdapter chatchat = new ChatAdapter(getContext(), 0, namearray, contentarray);
                    //if(!oldchat.equals(chatchat)) {
                        showchat.setAdapter(chatchat);
                        showchat.setSelection(showchat.getAdapter().getCount() - 1);
                    //}
                }catch (Exception e){}
            }
        }.execute();

    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    public static class ChatAdapter extends ArrayAdapter<String> {
        private ArrayList<String> namearray;
        private ArrayList<String> contentarray;
        private Context context;

        public ChatAdapter(Context context, int textViewResourceID, ArrayList<String> names,  ArrayList<String> contents){
            super(context, textViewResourceID, names);
            namearray = names;
            contentarray = contents;
            this.context = context;
       }
        public View getView(int position, View convertView, ViewGroup parent){
            View v = convertView;
            if(v==null){
                LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.activity_chat_adapter, null);
            }
            TextView name = (TextView)v.findViewById(R.id.username);
            TextView content = (TextView)v.findViewById(R.id.usercontent);
            name.setText(namearray.get(position));
            content.setText(contentarray.get(position));
            return v;
        }
    }
}

