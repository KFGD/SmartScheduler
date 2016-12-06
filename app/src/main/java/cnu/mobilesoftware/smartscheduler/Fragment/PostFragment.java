package cnu.mobilesoftware.smartscheduler.Fragment;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import cnu.mobilesoftware.smartscheduler.Dialog.AddGroupDialog;
import cnu.mobilesoftware.smartscheduler.Interface.ITitle;
import cnu.mobilesoftware.smartscheduler.KFGD_SchedulerUI.KFGD_Scheduler;
import cnu.mobilesoftware.smartscheduler.R;
import cnu.mobilesoftware.smartscheduler.WebDBHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment implements ITitle{

    private final String mTitle = "Post";
    private WebDBHelper webdb;
    private TextView showchat;
    private EditText chatinput;
    private Button sendchat;
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
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        // Inflate the layout for this fragment
        webdb = new WebDBHelper();
        showchat = (TextView) view.findViewById(R.id.showchat);
        chatinput = (EditText) view.findViewById(R.id.chatinput);
        sendchat = (Button) view.findViewById(R.id.sendChat);
        sendchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String content = chatinput.getText().toString();
                new AsyncTask<Void, Void, String>(){
                    ProgressDialog pd = new ProgressDialog(getContext());
                    @Override
                    protected String doInBackground(Void... voids) {
                        StringBuilder stringBuilder = webdb.INSERTBOARD("asd", "asdf", "asdfg", content);
                        String text = "";
                        if(stringBuilder != null)
                            text = stringBuilder.toString();
                        Log.d("fuck", content);
                        return text;
                    }
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        pd.setMessage("기다려");
                        pd.show();
                    }
                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        pd.dismiss();
                    }
                }.execute();
                chatinput.setText("");
                UpdateChat();
            }
        });
        UpdateChat();
        return view;
    }

    private void UpdateChat(){
        new AsyncTask<Void, Void, String>(){
            ProgressDialog pd = new ProgressDialog(getContext());
            @Override
            protected String doInBackground(Void... voids) {
                StringBuilder stringBuilder = webdb.SELECTWEBDB("SELECTBOARD", "asd");
                String text = "";
                if(stringBuilder != null)
                    text = stringBuilder.toString();
                return text;
            }
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd.setMessage("기다려");
                pd.show();
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                String out = null;
                try {
                    JSONArray chatArray = null;
                    JSONObject jsonObj = new JSONObject(s);
                    chatArray = jsonObj.getJSONArray("result");
                    for(int i=0; i<chatArray.length(); i++){
                        JSONObject j = chatArray.getJSONObject(i);
                        out += j.getString("name");
                        out += " : ";
                        out += j.getString("content");
                        out += "\n";
                    }
                }catch (Exception e){}
                showchat.setText(out);
                pd.dismiss();
            }
        }.execute();
    }

    @Override
    public String getTitle() {
        return mTitle;
    }
}
