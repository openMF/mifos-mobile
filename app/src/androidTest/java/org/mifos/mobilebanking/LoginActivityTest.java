package org.mifos.mobilebanking;

/*
 * Created by saksham on 10/June/2018
 */

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import android.support.test.espresso.intent.Intents;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mifos.mobilebanking.ui.activities.LoginActivity;
import org.mifos.mobilebanking.ui.activities.PassCodeActivity;
import org.mifos.mobilebanking.ui.activities.RegistrationActivity;
import static org.mifos.mobilebanking.utils.EspressoHelper.getNestedEditTest;
import static org.mifos.mobilebanking.utils.EspressoHelper.viewActionClick;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> loginActivityActivityTestRule =
            new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @Test
    public void testLoginSuccessfully() {
        String username = "selfservice";
        String password = "654321";

        onView(getNestedEditTest(R.id.til_username))
                .perform(typeText(username), closeSoftKeyboard());
        onView(getNestedEditTest(R.id.til_password))
                .perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(viewActionClick());

        intended(hasComponent(PassCodeActivity.class.getName()));
    }

    @Test
    public void testLoginFailed() {
        String username = "selfservice";
        String password = "failed";

        onView(getNestedEditTest(R.id.til_username))
                .perform(typeText(username), closeSoftKeyboard());
        onView(getNestedEditTest(R.id.til_password))
                .perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(viewActionClick());

        onView(getNestedEditTest(R.id.til_username))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testRegistrationClick() {

        onView(withId(R.id.btn_register)).perform(closeSoftKeyboard(), viewActionClick());

        intended(hasComponent(RegistrationActivity.class.getName()));
    }

    @After
    public void tearDown() {
        Intents.release();
    }
}
