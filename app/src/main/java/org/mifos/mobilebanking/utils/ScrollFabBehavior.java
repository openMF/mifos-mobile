package org.mifos.mobilebanking.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by dilpreet on 23/8/17.
 */

public class ScrollFabBehavior extends FloatingActionButton.Behavior {

    private CountDownTimer timer;
    private boolean isScrollValid;

    public ScrollFabBehavior(Context context, AttributeSet attrs) {
        super();
    }

    private CountDownTimer getTimer(final FloatingActionButton fab) {
        return new CountDownTimer(1000, 100) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {

                fab.show();
            }
        };
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,
                                       FloatingActionButton child, View directTargetChild,
                                       View target, int nestedScrollAxes) {

        if (timer == null) {

            timer = getTimer(child);
        }

        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL ||
                super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target,
                        nestedScrollAxes);

    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child,
                               View target, int dxConsumed, int dyConsumed, int dxUnconsumed,
                               int dyUnconsumed) {
        /*
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed,
                dyUnconsumed);
        if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
            // Reason to set fab visiblity as INVISIBLE :
            // https://stackoverflow.com/a/41386278/4672688
            child.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                @Override
                public void onHidden(FloatingActionButton fab) {
                    super.onHidden(fab);
                    fab.setVisibility(View.INVISIBLE);
                }
            });
        } else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
            child.show();
        }
        */

        //checking whether scroll is valid or not (top and bottom of list scrolls are invalid)
        if (dyUnconsumed == 0) {
            //hiding the FAB on scrolling
            child.setVisibility(View.INVISIBLE);
            isScrollValid = true;
        } else {
            isScrollValid = false;
        }
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout,
                                   final FloatingActionButton child, View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);

        if (isScrollValid) {

            //cancelling the previous timer as new scroll detected
            timer.cancel();
            //start new 1 second after the new scroll and show FAB after 1 second
            timer.start();

            isScrollValid = false;
        } else {

            child.setVisibility(View.VISIBLE);
            timer.cancel();
        }
    }
}