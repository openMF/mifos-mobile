package org.mifos.mobilebanking;

/*
 * Created by saksham on 15/June/2018
 */

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mifos.mobilebanking.ui.activities.HomeActivity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.mifos.mobilebanking.utils.EspressoHelper.childAtPosition;
import static org.mifos.mobilebanking.utils.EspressoHelper.viewActionClick;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ThirdPartyTransferFragmentTest {

    String transferAmount = "1";
    String remark = "test";

    @Rule
    public ActivityTestRule<HomeActivity> activityActivityTestRule =
            new ActivityTestRule<>(HomeActivity.class);

    @Before
    public void setUp() {
        onView(allOf(withId(R.id.ll_transfer),
                        childAtPosition(childAtPosition(
                                withClassName(is("android.widget.LinearLayout")), 1), 0),
                isDisplayed())).perform(click());

        onData(anything()).inAdapterView(allOf(withId(R.id.select_dialog_listview),
                        childAtPosition(withId(R.id.contentPanel), 0)))
                .atPosition(1).perform(click());
    }

    @Test
    public void testTPTTransfer() {
        onView(withId(R.id.btn_pay_from)).perform(click());
        onView(withId(R.id.btn_pay_to)).perform(click());
        onView(withId(R.id.et_amount)).perform(replaceText(transferAmount), closeSoftKeyboard());
        onView(withId(R.id.btn_amount)).perform(click());
        onView(withId(R.id.et_remark)).perform(replaceText(remark), closeSoftKeyboard());
        onView(withId(R.id.btn_review_transfer)).perform(viewActionClick());

        onView(withId(R.id.tv_amount)).check(matches(isDisplayed()));

    }

    @Test
    public void tearDown() {

    }
}
