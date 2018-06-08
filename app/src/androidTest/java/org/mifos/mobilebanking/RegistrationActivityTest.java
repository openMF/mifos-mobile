package org.mifos.mobilebanking;

/*
 * Created by saksham on 12/June/2018
 */

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.mifos.mobilebanking.utils.EspressoHelper.viewActionClick;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mifos.mobilebanking.ui.activities.RegistrationActivity;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RegistrationActivityTest {

    String accountNumber = "1";
    String userName = "12345";
    String firstName = "mifos";
    String lastName = "initiative";
    String phoneNumber = "8000000000";
    String email = "selfservice@mifos.org";
    String password = "123456";
    String confirmPassword = "123456";

    @Rule
    public ActivityTestRule<RegistrationActivity> registrationActivityActivityTestRule =
            new ActivityTestRule<>(RegistrationActivity.class);

    @Before
    public void setUp() {
        completeForm();
    }

    @Test
    public void testRegisterNewUserWithEmailFailed() {

        onView(withId(R.id.rb_email)).perform(viewActionClick());
        onView(withId(R.id.btn_register)).perform(viewActionClick());
        onView(withId(R.id.btn_register)).check(matches(isDisplayed()));
    }

    @Test
    public void testRegisterNewUserWithMobileFailed() {

        onView(withId(R.id.rb_mobile)).perform(viewActionClick());
        onView(withId(R.id.btn_register)).perform(viewActionClick());
        onView(withId(R.id.btn_register)).check(matches(isDisplayed()));
    }

    private void completeForm() {
        onView(withId(R.id.et_account_number)).perform(typeText(accountNumber),
                closeSoftKeyboard());
        onView(withId(R.id.et_username)).perform(typeText(userName), closeSoftKeyboard());
        onView(withId(R.id.et_first_name)).perform(typeText(firstName), closeSoftKeyboard());
        onView(withId(R.id.et_last_name)).perform(typeText(lastName), closeSoftKeyboard());
        onView(withId(R.id.et_phone_number)).perform(typeText(phoneNumber), closeSoftKeyboard());
        onView(withId(R.id.et_email)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.et_password)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.et_confirm_password)).perform(typeText(confirmPassword),
                closeSoftKeyboard());
    }
}
