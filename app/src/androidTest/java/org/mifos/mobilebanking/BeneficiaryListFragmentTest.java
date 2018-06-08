package org.mifos.mobilebanking;

/*
 * Created by saksham on 12/June/2018
 */

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.times;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.action.ViewActions.click;
import static org.mifos.mobilebanking.utils.EspressoHelper.viewActionClick;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mifos.mobilebanking.ui.activities.AddBeneficiaryActivity;
import org.mifos.mobilebanking.ui.activities.HomeActivity;
import android.support.test.espresso.intent.Intents;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class BeneficiaryListFragmentTest {

    @Rule
    public ActivityTestRule<HomeActivity> homeActivityActivityTestRule =
            new ActivityTestRule<>(HomeActivity.class);

    @Before
    public void setUp() {
        Intents.init();
        onView(withId(R.id.ll_beneficiaries)).perform(viewActionClick());
    }

    @Test
    public void testGotoAddBeneficiary() {
        onView(withId(R.id.fab_add_beneficiary)).perform(click());

        intended(hasComponent(AddBeneficiaryActivity.class.getName()), times(1));
    }

    @Test
    public void testBeneficiaryList() {
        onView(withId(R.id.rv_beneficiaries)).perform(RecyclerViewActions
                .actionOnItemAtPosition(0, click()));

        onView(withId(R.id.tv_beneficiary_name)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() {
        Intents.release();
    }
}
