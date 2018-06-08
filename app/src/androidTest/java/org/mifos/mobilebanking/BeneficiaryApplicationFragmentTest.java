package org.mifos.mobilebanking;

/*
 * Created by saksham on 15/June/2018
 */

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mifos.mobilebanking.utils.EspressoHelper.getNestedEditTest;
import static org.mifos.mobilebanking.utils.EspressoHelper.getRandomString;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mifos.mobilebanking.ui.activities.AddBeneficiaryActivity;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class BeneficiaryApplicationFragmentTest {

    @Rule
    public ActivityTestRule<AddBeneficiaryActivity> addBeneficiaryActivityActivityTestRule =
            new ActivityTestRule<>(AddBeneficiaryActivity.class);

    String beneficiaryName = getRandomString();
    String officeName = "Head Office";
    String correctAccountNumber = "000000001";
    String incorrectAccountNumber = "111111111";
    String transferLimit = "1000";

    @Before
    public void setUp() {

        onView(withId(R.id.ll_add_beneficiary_manually)).perform(click());
    }

    @Test
    public void testAddBeneficiarySuccessfully() {

        onView(getNestedEditTest(R.id.til_account_number)).perform(typeText(correctAccountNumber),
                closeSoftKeyboard());
        onView(getNestedEditTest(R.id.til_office_name)).perform(typeText(officeName),
                closeSoftKeyboard());
        onView(getNestedEditTest(R.id.til_transfer_limit)).perform(typeText(transferLimit),
                closeSoftKeyboard());
        onView(getNestedEditTest(R.id.til_beneficiary_name)).perform(typeText(beneficiaryName),
                closeSoftKeyboard());

        onView(withId(R.id.btn_beneficiary_submit)).perform(click());

        onView(withId(android.support.design.R.id.snackbar_text))
                .check(matches(withText(R.string.beneficiary_created_successfully)));
    }

    @Test
    public void testAddBeneficiaryFailed() {

        onView(getNestedEditTest(R.id.til_account_number)).perform(typeText(incorrectAccountNumber),
                closeSoftKeyboard());
        onView(getNestedEditTest(R.id.til_office_name)).perform(typeText(officeName),
                closeSoftKeyboard());
        onView(getNestedEditTest(R.id.til_transfer_limit)).perform(typeText(transferLimit),
                closeSoftKeyboard());
        onView(getNestedEditTest(R.id.til_beneficiary_name)).perform(typeText(beneficiaryName),
                closeSoftKeyboard());

        onView(withId(R.id.btn_beneficiary_submit)).perform(click());

        onView(withId(android.support.design.R.id.snackbar_text))
                .check(matches(withText(R.string.error_creating_beneficiary)));
    }

    @After
    public void tearDown() {

    }
}
