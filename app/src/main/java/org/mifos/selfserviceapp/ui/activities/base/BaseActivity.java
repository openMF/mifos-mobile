package org.mifos.selfserviceapp.ui.activities.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import org.mifos.selfserviceapp.MifosSelfServiceApp;
import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.injection.component.ActivityComponent;
import org.mifos.selfserviceapp.injection.component.DaggerActivityComponent;
import org.mifos.selfserviceapp.injection.module.ActivityModule;
import org.mifos.selfserviceapp.ui.views.BaseActivityCallback;

/**
 * @author ishan
 * @since 08/07/16
 */
public class BaseActivity extends AppCompatActivity implements BaseActivityCallback {

    protected Toolbar toolbar;
    private ActivityComponent activityComponent;
    private ProgressDialog progress;


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(MifosSelfServiceApp.get(this).component())
                    .build();
        }
        return activityComponent;
    }

    /**
     * This method is use to provide back button feature in the toolbar of activities
     */
    protected void showBackButton() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Displays a toast in current activity. In this method the duration
     * supplied is Short by default. If you want to specify duration
     * use {@link BaseActivity#showToast(String, int)} method.
     *
     * @param message Message that the toast must show.
     */
    public void showToast(String message) {
        showToast(message, Toast.LENGTH_SHORT);
    }

    /**
     * Displays a toast in current activity. The duration can of two types:
     * <ul>
     * <li>SHORT</li>
     * <li>LONG</li>
     * </ul>
     *
     * @param message   Message that the toast must show.
     * @param toastType Duration for which the toast must be visible.
     */
    public void showToast(@NonNull String message, @NonNull int toastType) {
        Toast.makeText(BaseActivity.this, message, toastType).show();
    }

    public void showProgressDialog() {
        showProgressDialog(getString(R.string.working));
    }

    @Override
    public void showProgressDialog(String message) {
        if (progress == null) {
            progress = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
            progress.setCancelable(false);
        }
        progress.setMessage(message);
        progress.show();

    }

    @Override
    public void hideProgressDialog() {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
            progress = null;
        }
    }

    public void setActionBarTitle(String title) {
        if (getSupportActionBar() != null && getTitle() != null) {
            setTitle(title);
        }
    }

    protected void setActionBarTitle(int title) {
        setActionBarTitle(getResources().getString(title));
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public void setToolbarTitle(String title) {
        setActionBarTitle(title);
    }

    /**
     * Replace Fragment in FrameLayout Container.
     *
     * @param fragment Fragment
     * @param addToBackStack Add to BackStack
     * @param containerId Container Id
     */
    public void replaceFragment(Fragment fragment, boolean addToBackStack, int containerId) {
        invalidateOptionsMenu();
        String backStateName = fragment.getClass().getName();
        boolean fragmentPopped = getSupportFragmentManager().popBackStackImmediate(backStateName,
                0);

        if (!fragmentPopped && getSupportFragmentManager().findFragmentByTag(backStateName) ==
                null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(containerId, fragment, backStateName);
            if (addToBackStack) {
                transaction.addToBackStack(backStateName);
            }
            transaction.commit();
        }
    }

    public void clearFragmentBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
        for (int i = 0; i < backStackCount; i++) {
            int backStackId = getSupportFragmentManager().getBackStackEntryAt(i).getId();
            fm.popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }
    public int stackCount(){
        return getSupportFragmentManager().getBackStackEntryCount();
    }
}
