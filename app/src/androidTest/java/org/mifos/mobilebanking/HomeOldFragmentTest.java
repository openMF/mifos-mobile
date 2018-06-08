package org.mifos.mobilebanking;

/*
 * Created by saksham on 13/June/2018
 */

import android.support.test.espresso.intent.Intents;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mifos.mobilebanking.ui.activities.HomeActivity;
import org.mifos.mobilebanking.ui.activities.LoanApplicationActivity;
import org.mifos.mobilebanking.ui.activities.NotificationActivity;
import org.mifos.mobilebanking.ui.activities.UserProfileActivity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.times;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.mifos.mobilebanking.utils.EspressoHelper.childAtPosition;
import static org.mifos.mobilebanking.utils.EspressoHelper.viewActionClick;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class HomeOldFragmentTest {


    @Rule
    public ActivityTestRule<HomeActivity> homeActivityActivityTestRule =
            new ActivityTestRule<>(HomeActivity.class);

    @Before
    public void beforeSetup() {
        Intents.init();
    }

    @Test
    public void testUserImageClicked() {
        onView(Matchers.allOf(withId(R.id.iv_user_image),
                childAtPosition(childAtPosition(
                        withClassName(is("android.widget.LinearLayout")), 0),
                        0), isDisplayed())).perform(click());

        intended(hasComponent(UserProfileActivity.class.getName()), times(1));
    }

    @Test
    public void testNotificationIconClicked() {
        onView(
                Matchers.allOf(withId(R.id.menu_notifications),
                        childAtPosition(childAtPosition(withId(R.id.toolbar), 2), 0),
                        isDisplayed())).perform(click());

        intended(hasComponent(NotificationActivity.class.getName()), times(1));
    }

    @Test
    public void testAccountsClicked() {
        onView(withId(R.id.ll_accounts)).perform(viewActionClick());

        onView(withId(R.id.viewpager)).check(matches(isDisplayed()));
    }

    @Test
    public void testTransferClicked() {
        onView(withId(R.id.ll_transfer)).perform(viewActionClick());

        onData(anything())
                .inAdapterView(Matchers.allOf(withId(R.id.select_dialog_listview),
                        childAtPosition(withId(R.id.contentPanel), 0)))
                .atPosition(0).perform(click());

        onView(withId(R.id.view_flipper)).check(matches(isDisplayed()));
    }

    @Test
    public void testTPTClicked() {
        onView(withId(R.id.ll_transfer)).perform(viewActionClick());

        onData(anything())
                .inAdapterView(Matchers.allOf(withId(R.id.select_dialog_listview),
                        childAtPosition(withId(R.id.contentPanel), 0)))
                .atPosition(1).perform(click());

        onView(withId(R.id.view_flipper)).check(matches(isDisplayed()));
    }

    @Test
    public void testChargesClicked() {
        onView(withId(R.id.ll_charges)).perform(viewActionClick());

        onView(withId(R.id.swipe_charge_container)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoanApplicationClicked() {
        onView(withId(R.id.ll_apply_for_loan)).perform(viewActionClick());

        intended(hasComponent(LoanApplicationActivity.class.getName()), times(1));
    }

    @Test
    public void testBeneficiaryClick() {
        onView(withId(R.id.ll_beneficiaries)).perform(viewActionClick());

        onView(withId(R.id.swipe_container)).check(matches(isDisplayed()));
    }

    /*
     * needs to be added after Survey Functionality is added
     * */
    @Test
    public void testSurveyClicked() {

    }

    @After
    public void tearOf() {
        Intents.release();
    }

}
