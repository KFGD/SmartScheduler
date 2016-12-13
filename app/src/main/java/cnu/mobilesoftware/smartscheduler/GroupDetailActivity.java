package cnu.mobilesoftware.smartscheduler;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Transition;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

import cnu.mobilesoftware.smartscheduler.Dialog.AddNoticeDialog;
import cnu.mobilesoftware.smartscheduler.Fragment.NoticeFragment;
import cnu.mobilesoftware.smartscheduler.Fragment.PostFragment;
import cnu.mobilesoftware.smartscheduler.Interface.ITitle;

public class GroupDetailActivity extends AppCompatActivity{

    GroupItem groupItem;
    private WebDBHelper webdb;
    private String name;

    //NoticeFragment
    FloatingActionMenu fabMenu;

    //PostFragment
    LinearLayout linear_chat;
    EditText chatinput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        final ImageView memberImage = (ImageView)findViewById(R.id.group_image);

        Intent intent = getIntent();
        groupItem = intent.getParcelableExtra("GROUP_ITEM");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            Transition exitTrans = new Explode();
            //Transition exitTrans = new Fade();
            //Transition exitTrans = new Slide();

            Transition reenterTrans = new Explode();
            //Transition reenterTrans = new Fade();
            //Transition reenterTrans = new Slide();

            window.setExitTransition(exitTrans);
            window.setEnterTransition(reenterTrans);
            window.setReenterTransition(reenterTrans);
            //window.setTransitionBackgroundFadeDuration(2000);
        }
        final Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(groupItem.group_title);
        memberImage.setImageResource(groupItem.img_res);

        //fabMenu(NoticeFragment)
        fabMenu = (FloatingActionMenu)findViewById(R.id.fab_menu);
        //linear_chat(LinearLayout)
        linear_chat = (LinearLayout)findViewById(R.id.linear_chat);
        chatinput = (EditText)findViewById(R.id.chatinput);

       //ViewPager
        ViewPager viewPager = (ViewPager)findViewById(R.id.viewPager);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        setUpViewPagerAndTabLayout(viewPager, tabLayout);
       //get name by uuid
        webdb = new WebDBHelper();
        final String uuid = SmartSchedulerApplication.getUUID();
        new AsyncTask<Void, Void, String>(){
            ProgressDialog pd = new ProgressDialog(getApplicationContext());
            @Override
            protected String doInBackground(Void... voids) {
                StringBuilder stringBuilder = webdb.SELECTWEBDB("SELECTUSERINFO", uuid);
                String text = "";
                if(stringBuilder != null)
                    text = stringBuilder.toString();
                return text;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(s.equals(""))
                    name = "NONAME";
                else
                    name = s;
                Toast.makeText(getApplicationContext(), "Hi "+name, Toast.LENGTH_SHORT).show();
            }
        }.execute();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if(position == 0) {
                    fabMenu.setVisibility(View.VISIBLE);
                    linear_chat.setVisibility(View.GONE);
                }
                else {
                    fabMenu.setVisibility(View.GONE);
                    linear_chat.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void setUpViewPagerAndTabLayout(ViewPager viewPager, TabLayout tabLayout){
        //Setting ViewPager
        SectionsPagerAdapter sectionPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        sectionPagerAdapter.appendFragment(NoticeFragment.newInstance(this));
        sectionPagerAdapter.appendFragment(PostFragment.newInstance(this));
        viewPager.setAdapter(sectionPagerAdapter);
        viewPager.setCurrentItem(0);

        //Setting TabLayout
        tabLayout.setupWithViewPager(viewPager);
        for(int i=0; i<sectionPagerAdapter.getCount(); ++i){
            if(null == tabLayout.getTabAt(i))
                break;
            if(sectionPagerAdapter.getItem(i) instanceof ITitle)
                tabLayout.getTabAt(i).setText(((ITitle) sectionPagerAdapter.getItem(i)).getTitle());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void onClickAddNotice(View view){
        Bundle bundle = new Bundle();
        bundle.putParcelable("GROUP_ITEM", groupItem);
        AddNoticeDialog addNoticeDialog = new AddNoticeDialog();
        addNoticeDialog.setArguments(bundle);
        addNoticeDialog.show(getSupportFragmentManager(), "");
    }

    public void onClickSendChatBtn(View view){
        final String uuid = SmartSchedulerApplication.getUUID();
        final String content = chatinput.getText().toString();
        if(content.equals(""))
            return;
        new AsyncTask<Void, Void, String>(){
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

    public GroupItem getGroupItem(){
        return groupItem;
    }

    public final class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> pageFragment = new ArrayList<>(3);

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void appendFragment(Fragment fragment){
            pageFragment.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return pageFragment.get(position);
        }

        @Override
        public int getCount() {
            return pageFragment.size();
        }
    }
    public String getName(){
        return this.name;
    }
}
