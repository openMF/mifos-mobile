package org.mifos.mobilebanking;

/*
 * Created by saksham on 12/June/2018
 */

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.AssertionFailedError;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;
import static org.mifos.mobilebanking.utils.EspressoHelper.childAtPosition;
import static org.mifos.mobilebanking.utils.EspressoHelper.getTAtPosition;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mifos.mobilebanking.ui.activities.HomeActivity;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ClientAccountFragmentTest {

    Context context;

    @Rule
    public ActivityTestRule<HomeActivity> homeActivityActivityTestRule =
            new ActivityTestRule<>(HomeActivity.class);

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        onView(withId(R.id.ll_accounts)).perform(click());
    }

    @Test
    public void testSavingAccountViewPager() {
        onView(allOf(childAtPosition(childAtPosition(withId(R.id.tabs), 0), 0), isDisplayed()))
                .perform(click());
        onView(getTAtPosition(withId(R.id.rv_accounts), 0)).check(matches(isDisplayed()));
        onView(allOf(getTAtPosition(withId(R.id.tv_clientSavingAccountNumber), 0), isDisplayed()))
                .perform(click());
        onView(allOf(withId(R.id.ll_account))).check(matches(isDisplayed()));
    }

    @Test
    public void testLoanAccountViewPager() {
        onView(allOf(childAtPosition(childAtPosition(withId(R.id.tabs), 0), 1), isDisplayed()))
                .perform(click());
        onView(getTAtPosition(withId(R.id.rv_accounts), 1)).check(matches(isDisplayed()));
        onView(allOf(getTAtPosition(withId(R.id.tv_clientLoanAccountNumber), 0), isDisplayed()))
                .perform(click());

        try {
            onView(withId(R.id.ll_account_detail)).check(matches(isDisplayed()));
        } catch (AssertionFailedError error) {
            onView(withId(R.id.layout_error)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void testShareAccountViewPager() {
        onView(allOf(childAtPosition(childAtPosition(withId(R.id.tabs), 0), 2), isDisplayed()))
                .perform(click());
        onView(getTAtPosition(withId(R.id.rv_accounts), 2)).check(matches(isDisplayed()));
        onView(allOf(getTAtPosition(withId(R.id.tv_clientSharingAccountNumber), 0), isDisplayed()))
                .perform(click());
    }

    @After
    public void tearDown() {

    }

}