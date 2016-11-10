package cnu.mobilesoftware.smartscheduler;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

public class GroupDetailActivity extends AppCompatActivity {

    GroupItem groupItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);


        final ImageView memberImage = (ImageView)findViewById(R.id.member_image);

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
    }
}
