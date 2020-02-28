package org.mifos.mobile.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
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

        setContentView(R.layout.activity_intro_splash_screen);

        btnNext = findViewById(R.id.btnNext);
        btnGetStarted = findViewById(R.id.getStarted);
        tabIndicator = findViewById(R.id.tabLayout);

        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Bank Account Management",
                String.valueOf(R.string.management), R.drawable.management));
        mList.add(new ScreenItem("Tracking Spending Habits",
                String.valueOf(R.string.tracking), R.drawable.track_money));
        mList.add(new ScreenItem("Customer Support",
                String.valueOf(R.string.support), R.drawable.customer_support));

        viewPager = findViewById(R.id.screenViewPager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this, mList);
        viewPager.setAdapter(introViewPagerAdapter);
        tabIndicator.setupWithViewPager(viewPager);
        
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position = viewPager.getCurrentItem();
                if (position < mList.size()) {

                    position++;
                    viewPager.setCurrentItem(position);
                }

                if (position == mList.size() - 1) {

                    loaddLastScreen();
                }
            }
        });

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


        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivity =
                        new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(mainActivity);

                finish();
            }
        });
    }

    private void loaddLastScreen() {
        btnNext.setVisibility(View.GONE);
        btnGetStarted.setVisibility(View.VISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);

    }
}
