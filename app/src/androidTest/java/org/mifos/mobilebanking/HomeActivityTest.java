package org.mifos.mobilebanking;

/*
 * Created by saksham on 10/June/2018
 */

import android.app.Activity;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.intent.Intents.times;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mifos.mobilebanking.utils.EspressoHelper.childAtPosition;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mifos.mobilebanking.ui.activities.HomeActivity;
import org.mifos.mobilebanking.ui.activities.LoginActivity;
import org.mifos.mobilebanking.ui.activities.SettingsActivity;
import org.mifos.mobilebanking.ui.activities.UserProfileActivity;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class HomeActivityTest {

    Context context;
    Activity activity;

    @Rule
    public ActivityTestRule<HomeActivity> homeActivityActivityTestRule =
            new ActivityTestRule<>(HomeActivity.class);

    @Before
    public void setUp() {
        Intents.init();
        context = InstrumentationRegistry.getTargetContext();
        activity = homeActivityActivityTestRule.getActivity();
    }

    @Test
    public void testNavigationUserImageClicked() {
        openNavigationDrawer();

        onView(Matchers.allOf(withId(R.id.iv_user_image),
                        childAtPosition(childAtPosition(withId(R.id.navigation_header_container),
                                         0), 0), isDisplayed())).perform(click());

        intended(hasComponent(UserProfileActivity.class.getName()), times(1));
    }

    @Test
    public void testNavigationHomeItemClicked() {
        openNavigationDrawer();

        onView(withId(R.id.navigation_view))
                .perform(NavigationViewActions.navigateTo(R.id.item_home));

        onView(withId(R.id.swipe_home_container)).check(matches(isDisplayed()));
    }

    @Test
    public void testNavigationAccountItemClicked() {
        openNavigationDrawer();

        onView(withId(R.id.navigation_view))
                .perform(NavigationViewActions.navigateTo(R.id.item_accounts));

        onView(withId(R.id.tabs)).check(matches(isDisplayed()));

    }

    @Test
    public void testNavigationRecentTransactionItemClicked() {
        openNavigationDrawer();

        onView(withId(R.id.navigation_view))
                .perform(NavigationViewActions.navigateTo(R.id.item_recent_transactions));

        onView(withId(R.id.swipe_transaction_container)).check(matches(isDisplayed()));
    }

    @Test
    public void testNavigationChargesItemClicked() {
        openNavigationDrawer();

        onView(withId(R.id.navigation_view))
                .perform(NavigationViewActions.navigateTo(R.id.item_charges));

        onView(withId(R.id.swipe_charge_container)).check(matches(isDisplayed()));
    }

    @Test
    public void testNavigationTPTItemClicked() {
        openNavigationDrawer();

        onView(withId(R.id.navigation_view))
                .perform(NavigationViewActions.navigateTo(R.id.item_third_party_transfer));

        onView(withId(R.id.view_flipper)).check(matches(isDisplayed()));
    }

    @Test
    public void testNavigationBeneficiaryItemClicked() {
        openNavigationDrawer();

        onView(withId(R.id.navigation_view))
                .perform(NavigationViewActions.navigateTo(R.id.item_beneficiaries));

        onView(withId(R.id.swipe_container)).check(matches(isDisplayed()));
    }

    @Test
    public void testNavigationSettingsItemClicked() {
        openNavigationDrawer();

        onView(withId(R.id.navigation_view))
                .perform(NavigationViewActions.navigateTo(R.id.item_settings));

        intended(hasComponent(SettingsActivity.class.getName()), times(1));
    }

    @Test
    public void testNavigationAboutItemClicked() {
        openNavigationDrawer();

        onView(withId(R.id.navigation_view))
                .perform(NavigationViewActions.navigateTo(R.id.item_about_us));

        onView(withId(R.id.tv_app_version)).check(matches(isDisplayed()));
    }

    @Test
    public void testNavigationHelpItemClicked() {
        openNavigationDrawer();

        onView(withId(R.id.navigation_view))
                .perform(NavigationViewActions.navigateTo(R.id.item_help));

        onView(withId(R.id.rv_faq)).check(matches(isDisplayed()));
    }

    @Test
    public void testNavigationShareItemClicked() {
        openNavigationDrawer();
        /*onView(withId(R.id.navigation_view))
                .perform(NavigationViewActions.navigateTo(R.id.item_share));*/
    }

    @Test
    public void testNavigationLogoutItemClicked() {
        openNavigationDrawer();

        onView(withId(R.id.navigation_view))
                .perform(NavigationViewActions.navigateTo(R.id.item_logout));

        onView(withText(R.string.dialog_logout)).check(matches(isDisplayed()));
        onView(withId(android.R.id.button1)).perform(click());

        intended(hasComponent(LoginActivity.class.getName()));
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    private void openNavigationDrawer() {
        onView(withId(R.id.drawer))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());
    }
}
