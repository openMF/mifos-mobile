package org.mifos.mobilebanking.utils;

/*
 * Created by saksham on 12/June/2018
 */

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.UUID;

import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;

public class EspressoHelper {

    /*
    * This method helps in traversing the ViewHierarchy
    * */
    public static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    /*
    * This method performs Actions on partially visible views
    * */
    public static ViewAction viewActionClick() {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isEnabled(); // no constraints, they are checked above
            }

            @Override
            public String getDescription() {
                return "click plus button";
            }

            @Override
            public void perform(UiController uiController, View view) {
                view.performClick();
            }
        };
    }

    /*
    * This method helps to get the Nested EditText present inside TextInputLayout
    * */
    public static Matcher<View> getNestedEditTest(int id) {
        return allOf(isDescendantOfA(withId(id)), isAssignableFrom(EditText.class));
    }

    /*
    * This method helps to find <T> at given position from the View Hierarchy
    * */
    public static <T> Matcher<T> getTAtPosition(final Matcher<T> matcher, final int position) {
        return new BaseMatcher<T>() {
            private int resultIndex = -1;

            @Override
            public boolean matches(final Object item) {
                if (matcher.matches(item)) {
                    resultIndex++;
                    return resultIndex == position;
                }
                return false;
            }

            @Override
            public void describeTo(final Description description) {
            }
        };
    }

    /*
    * This method helps to generate String which can be used during testing
    * */

    public static String getRandomString() {
        return UUID.randomUUID().toString().split("-")[0];
    }
}
