package org.mifos.mobile.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import org.mifos.mobile.R;
import org.mifos.mobile.utils.IntroViewPagerAdapter;
import org.mifos.mobile.utils.ScreenItem;

import java.util.ArrayList;
import java.util.List;

public class IntroSplashScreen extends AppCompatActivity {

    private ViewPager viewPager;
    IntroViewPagerAdapter introViewPagerAdapter;
    TabLayout tabIndicator;
    TextView btnNext;
    int position = 0;
    TextView btnGetStarted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // make the activity on full screen
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_intro_splash_screen);


        //If organisation want this feature to show only once to new user

//        if (restorePrefData()) {
//
//            Intent mainActivity =
//            new Intent(getApplicationContext(), LoginActivity.class);
//            startActivity(mainActivity);
//            finish();
//        }

        // hide the action bar

        //getSupportActionBar().hide();

        // ini views
        btnNext = findViewById(R.id.btnNext);
        btnGetStarted = findViewById(R.id.getStarted);
        tabIndicator = findViewById(R.id.tabLayout);
//        btnAnim =
//        AnimationUtils.loadAnimation(getApplicationContext(),
//        R.anim.button_animation);
//        tvSkip = findViewById(R.id.tv_skip);

        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Bank Account Management",
                String.valueOf(R.string.management), R.drawable.management));
        mList.add(new ScreenItem("Tracking Spending Habits",
                String.valueOf(R.string.tracking), R.drawable.track_money));
        mList.add(new ScreenItem("Customer Support",
                String.valueOf(R.string.support), R.drawable.customer_support));


        // setup viewpager
        viewPager = findViewById(R.id.screenViewPager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this, mList);
        viewPager.setAdapter(introViewPagerAdapter);

        // setup tablayout with viewpager

        tabIndicator.setupWithViewPager(viewPager);


        // next button click Listner

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position = viewPager.getCurrentItem();
                if (position < mList.size()) {

                    position++;
                    viewPager.setCurrentItem(position);


                }

                if (position == mList.size() - 1) { // when we rech to the last screen

                    loaddLastScreen();
                }
            }
        });

        // tablayout add change listener


        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == mList.size() - 1) {

                    loaddLastScreen();

                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        // Get Started button click listener

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //open main activity

                Intent mainActivity =
                        new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(mainActivity);

                savePrefsData();
                finish();


            }
        });

    }

    private boolean restorePrefData() {


        SharedPreferences pref =
                getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore =
                pref.getBoolean("isIntroOpnend", false);
        return isIntroActivityOpnendBefore;


    }

    private void savePrefsData() {

        SharedPreferences pref =
                getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnend", true);
        editor.commit();


    }

    // show the GETSTARTED Button and hide the indicator and the next button
    private void loaddLastScreen() {

        btnNext.setVisibility(View.GONE);
        btnGetStarted.setVisibility(View.VISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);

    }
}




