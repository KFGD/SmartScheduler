package cnu.mobilesoftware.smartscheduler.Fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import cnu.mobilesoftware.smartscheduler.CardItem;
import cnu.mobilesoftware.smartscheduler.Interface.ITitle;
import cnu.mobilesoftware.smartscheduler.R;
import cnu.mobilesoftware.smartscheduler.WebDBHelper;

public class NoticeFragment extends Fragment implements ITitle{

    private final String mTtitle = "Notice";
    private WebDBHelper webdb;

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
        NoticeViewAdapter noticeViewAdapter = new NoticeViewAdapter(initCardItem());
        recyclerView.setAdapter(noticeViewAdapter);
        webdb = new WebDBHelper();
        return view;
    }
    //People
    ArrayList<CardItem.PeopleCardItem> peopleCardItems = new ArrayList<>();
    private ArrayList<CardItem.BasicCardItem> initCardItem(){
        ArrayList<CardItem.BasicCardItem> cardItemArrayList = new ArrayList<>();
        peopleCardItems.add(new CardItem.PeopleCardItem(CardItem.TAG.PEOPLE_HEADER));
        try {
            ArrayList<CardItem.PeopleCardItem> serverItems = new AsyncTask<Void, Void, ArrayList<CardItem.PeopleCardItem>>(){
                @Override
                protected ArrayList<CardItem.PeopleCardItem> doInBackground(Void... voids) {
                    StringBuilder stringBuilder = webdb.SELECTWEBDB("SELECTGROUPUSER", "32");
                    String text = "";
                    if(stringBuilder != null)
                        text = stringBuilder.toString();

                    ArrayList<CardItem.PeopleCardItem> peopleCardItems = new ArrayList<CardItem.PeopleCardItem>();
                    try {
                        JSONArray chatArray = null;
                        JSONObject jsonObj = new JSONObject(text);
                        chatArray = jsonObj.getJSONArray("result");
                        peopleCardItems.add(new CardItem.PeopleCardItem(CardItem.TAG.PEOPLE_CONTENT, chatArray.getJSONObject(0).getString("name"), "방장"));
                        for(int i=1; i<chatArray.length(); i++){
                            JSONObject j = chatArray.getJSONObject(i);
                            peopleCardItems.add(new CardItem.PeopleCardItem(CardItem.TAG.PEOPLE_CONTENT, j.getString("name"), "회원나부랭이"));
                        }
                    }catch (Exception e){}
                    return peopleCardItems;
                }
            }.execute().get();
            for(CardItem.PeopleCardItem item : serverItems)
                peopleCardItems.add(item);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Notice
        ArrayList<CardItem.NoticeCardItem> noticeCardItems = new ArrayList<>();
        noticeCardItems.add(new CardItem.NoticeCardItem(CardItem.TAG.NOTICE_HEADER));
        try {
            ArrayList<CardItem.NoticeCardItem> serverItems = new AsyncTask<Void, Void, ArrayList<CardItem.NoticeCardItem>>(){
                @Override
                protected ArrayList<CardItem.NoticeCardItem> doInBackground(Void... voids) {
                    StringBuilder stringBuilder = webdb.SELECTWEBDB("SELECTNOTICE", "32");
                    String text = "";
                    if(stringBuilder != null)
                        text = stringBuilder.toString();

                    ArrayList<CardItem.NoticeCardItem> noticeCardItems = new ArrayList<CardItem.NoticeCardItem>();
                    try {
                        JSONArray noticeArray = null;
                        JSONObject jsonObj = new JSONObject(text);
                        noticeArray = jsonObj.getJSONArray("result");
                        for(int i=0; i<noticeArray.length(); i++){
                            JSONObject j = noticeArray.getJSONObject(i);
                            noticeCardItems.add(new CardItem.NoticeCardItem(CardItem.TAG.NOTICE_CONTENT, j.getString("datetime"), j.getString("topic")));
                        }
                    }catch (Exception e){}
                    return noticeCardItems;
                }
            }.execute().get();
            for(CardItem.NoticeCardItem item : serverItems)
                noticeCardItems.add(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cardItemArrayList.addAll(peopleCardItems);
        cardItemArrayList.addAll(noticeCardItems);

        return cardItemArrayList;
    }

    private class NoticeViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private ArrayList<CardItem.BasicCardItem> resources = new ArrayList<>();

        public NoticeViewAdapter(){}

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

            View view = null;
            switch (CardItem.convertNumToTag(viewType)) {
                case PEOPLE_HEADER:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_people_header, parent, false);
                    return new PeopleCardHolder(view, CardItem.TAG.PEOPLE_HEADER);
                case PEOPLE_CONTENT:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_people_content, parent, false);
                    return new PeopleCardHolder(view, CardItem.TAG.PEOPLE_CONTENT);
                case NOTICE_HEADER:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_notice_header, parent, false);
                    return new NoticeCardHolder(view, CardItem.TAG.NOTICE_HEADER);
                case NOTICE_CONTENT:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_notice_content, parent, false);
                    return new NoticeCardHolder(view, CardItem.TAG.NOTICE_CONTENT);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof PeopleCardHolder){
                PeopleCardHolder peopleCardHolder = (PeopleCardHolder)holder;
                CardItem.PeopleCardItem item = (CardItem.PeopleCardItem)resources.get(position);
                if(item.tag != CardItem.TAG.PEOPLE_HEADER){
                    peopleCardHolder.tvName.setText(item.peopleName);
                    peopleCardHolder.tvRank.setText(item.peopleRank);
                }

            }else{
                NoticeCardHolder noticeCardHolder = (NoticeCardHolder)holder;
                CardItem.NoticeCardItem item = (CardItem.NoticeCardItem)resources.get(position);
                if(item.tag != CardItem.TAG.NOTICE_HEADER){
                    noticeCardHolder.tvDate.setText(item.meetingDate);
                    noticeCardHolder.tvTopic.setText(item.meetingTopic);
                }
            }
        }

        @Override
        public int getItemCount() {
            return resources.size();
        }
    }

    private class PeopleCardHolder extends RecyclerView.ViewHolder{
        private CardItem.TAG tag = null;
        private TextView tvName = null;
        private TextView tvRank = null;

        public PeopleCardHolder(View itemView) {
            super(itemView);
        }
        public PeopleCardHolder(View itemView, CardItem.TAG tag){
            super(itemView);
            this.tag = tag;
            if(tag == CardItem.TAG.PEOPLE_CONTENT){
                tvName = (TextView)itemView.findViewById(R.id.tv_people_name);
                tvRank = (TextView)itemView.findViewById(R.id.tv_people_rank);
            }
        }
    }

    private class NoticeCardHolder extends RecyclerView.ViewHolder{
        private CardItem.TAG tag = null;
        private TextView tvDate = null;
        private TextView tvTopic = null;

        private NoticeCardHolder(View itemView) {
            super(itemView);
        }
        public NoticeCardHolder(View itemView, CardItem.TAG tag){
            super(itemView);
            this.tag = tag;
            if(tag == CardItem.TAG.NOTICE_CONTENT){
                tvDate = (TextView)itemView.findViewById(R.id.tv_meetingDay);
                tvTopic = (TextView)itemView.findViewById(R.id.tv_meetingTopic);
            }
        }
    }

    @Override
    public String getTitle() {
        return mTtitle;
    }
}
