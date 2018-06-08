package org.mifos.mobilebanking;

/*
 * Created by saksham on 15/June/2018
 */

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mifos.mobilebanking.ui.activities.LoanApplicationActivity;
import org.mifos.mobilebanking.utils.EspressoHelper;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoanApplicationFragmentTest {

    @Rule
    public ActivityTestRule<LoanApplicationActivity> loanApplicationActivityActivityTestRule =
            new ActivityTestRule<>(LoanApplicationActivity.class);

    @Before
    public void setUp() {

    }

    @Test
    public void testApplyForLoan() {
        onView(withId(R.id.btn_loan_submit)).perform(EspressoHelper.viewActionClick());
        onView(withId(android.support.design.R.id.snackbar_text))
                .check(matches(withText(R.string.loan_application_submitted_successfully)));
    }

    @After
    public void tearDown() {

    }

}
