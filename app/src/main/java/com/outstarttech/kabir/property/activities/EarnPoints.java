package com.outstarttech.kabir.property.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.codextech.kabir.bills_app.SessionManager;
import com.inventioncore.kabir.sopfia.R;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.integration.IntegrationHelper;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.model.Placement;
import com.ironsource.mediationsdk.sdk.RewardedVideoListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EarnPoints extends AppCompatActivity {

    LinearLayout buyCreditsButton, watchAdsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earn_points);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // App Key
        String appKey="bad0dfcd";
        IntegrationHelper.validateIntegration(EarnPoints.this);
        //Rewarded Video
        IronSource.init(this, appKey, IronSource.AD_UNIT.REWARDED_VIDEO);


        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            ActionBar bar = getSupportActionBar();
            bar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));


//        getSupportActionBar().setLogo(R.drawable.logoactionbar);
            getSupportActionBar().setTitle("Earn Credits");
        }

        buyCreditsButton = (LinearLayout) findViewById(R.id.cardBuyCredits);
        watchAdsButton = (LinearLayout) findViewById(R.id.cardWatchAds);

        buyCreditsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EarnPoints.this, BuySopfia.class));
            }
        });

        watchAdsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IronSource.isRewardedVideoAvailable()){
                    IronSource.showRewardedVideo("DefaultRewardedVideo");
                }
                else {
                    Toast.makeText(EarnPoints.this, "Ad Not available at the moment, Please try again later", Toast.LENGTH_SHORT).show();
                }
            }
        });




        IronSource.setRewardedVideoListener(new RewardedVideoListener() {
            /**
             * Invoked when the RewardedVideo ad view has opened.
             * Your Activity will lose focus. Please avoid performing heavy
             * tasks till the video ad will be closed.
             */
            @Override
            public void onRewardedVideoAdOpened() {
            }
            /*Invoked when the RewardedVideo ad view is about to be closed.
            Your activity will now regain its focus.*/
            @Override
            public void onRewardedVideoAdClosed() {
            }
            /**
             * Invoked when there is a change in the ad availability status.
             *
             * @param - available - value will change to true when rewarded videos are *available.
             *          You can then show the video by calling showRewardedVideo().
             *          Value will change to false when no videos are available.
             */
            @Override
            public void onRewardedVideoAvailabilityChanged(boolean available) {
                if(available == false){
                    //  Toast.makeText(getContext(), "Ad Not available at the moment, Please try again later", Toast.LENGTH_SHORT).show();
                }
                //Change the in-app 'Traffic Driver' state according to availability.
            }
            /**
             /**
             * Invoked when the user completed the video and should be rewarded.
             * If using server-to-server callbacks you may ignore this events and wait *for the callback from the ironSource server.
             *
             * @param - placement - the Placement the user completed a video from.
             */
            @Override
            public void onRewardedVideoAdRewarded(Placement placement) {
                String rewardName = placement.getRewardName();
                int rewardAmount = placement.getRewardAmount();
                updatepoints(rewardAmount,"WatchedAds");

            }
            /* Invoked when RewardedVideo call to show a rewarded video has failed
             * IronSourceError contains the reason for the failure.
             */
            @Override
            public void onRewardedVideoAdShowFailed(IronSourceError error) {
            }
            /*Invoked when the end user clicked on the RewardedVideo ad
             */
            @Override
            public void onRewardedVideoAdClicked(Placement placement){
            }

            @Override
            public void onRewardedVideoAdStarted(){
            }
            /* Invoked when the video ad finishes plating. */
            @Override
            public void onRewardedVideoAdEnded(){
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
            finish();
        } else {
//            createInformationDialog();
        }
        return super.onOptionsItemSelected(item);
    }



    protected void onResume() {
        super.onResume();
        IronSource.onResume(this);
    }
    protected void onPause() {
        super.onPause();
        IronSource.onPause(this);
    }






    public void updatepoints(final int points, final String type){
        SessionManager sessionManager = new SessionManager(this);

        final boolean check;
        if (sessionManager.isUserSignedIn()) {
            final String uid = sessionManager.getKeyLoginId();
            Toast.makeText(EarnPoints.this, "Ad watched Successfuly", Toast.LENGTH_SHORT).show();
            // Need API To Update User Credits ONline


        }

    }
}
