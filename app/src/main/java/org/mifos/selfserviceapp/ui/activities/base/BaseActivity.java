package org.mifos.selfserviceapp.ui.activities.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import org.mifos.selfserviceapp.MifosSelfServiceApp;
import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.injection.component.ActivityComponent;
import org.mifos.selfserviceapp.injection.component.DaggerActivityComponent;
import org.mifos.selfserviceapp.injection.module.ActivityModule;
import org.mifos.selfserviceapp.ui.activities.PassCodeActivity;
import org.mifos.selfserviceapp.ui.views.BaseActivityCallback;
import org.mifos.selfserviceapp.utils.Constants;
import org.mifos.selfserviceapp.utils.ForegroundChecker;
import org.mifos.selfserviceapp.utils.LanguageHelper;

/**
 * @author ishan
 * @since 08/07/16
 */
public class BaseActivity extends AppCompatActivity implements BaseActivityCallback,
        ForegroundChecker.Listener {

    protected Toolbar toolbar;
    private ActivityComponent activityComponent;
    private ProgressDialog progress;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            setToolbarElevation();
        }
    }

    /**
     * Used for removing elevation from toolbar
     */
    public void hideToolbarElevation() {
        ViewCompat.setElevation(toolbar, 0);
    }

    /**
     * Used for setting toolbar elevation
     */
    public void setToolbarElevation() {
        ViewCompat.setElevation(toolbar, 8f);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Used for dependency injection
     * @return {@link ActivityComponent} which is used for injection
     */
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

    /**
     * Calls a method {@code showProgressDialog("Working")} which displays ProgressDialog
     */
    public void showProgressDialog() {
        showProgressDialog(getString(R.string.working));
    }

    /**
     * Displays a ProgressDialog
     * @param message Message you want to display in Progress Dialog
     */
    @Override
    public void showProgressDialog(String message) {
        if (progress == null) {
            progress = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
            progress.setCancelable(false);
        }
        progress.setMessage(message);
        progress.show();

    }

    /**
     * Hides the progress dialog if it is currently being shown
     */
    @Override
    public void hideProgressDialog() {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
            progress = null;
        }
    }

    /**
     * Used for setting title of Toolbar
     * @param title String you want to display as title
     */
    public void setActionBarTitle(String title) {
        if (getSupportActionBar() != null && getTitle() != null) {
            setTitle(title);
        }
    }

    protected void setActionBarTitle(int title) {
        setActionBarTitle(getResources().getString(title));
    }

    /**
     * @return Returns toolbar linked with current activity
     */
    public Toolbar getToolbar() {
        return toolbar;
    }

    /**
     * Calls {@code setActionBarTitle()} to set Toolbar title
     * @param title String you want to set as title
     */
    @Override
    public void setToolbarTitle(String title) {
        setActionBarTitle(title);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ForegroundChecker.get().addListener(this);
        ForegroundChecker.get().onActivityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ForegroundChecker.get().onActivityPaused();
    }

    @Override
    public void onBecameForeground() {
        Intent intent = new Intent(this, PassCodeActivity.class);
        intent.putExtra(Constants.INTIAL_LOGIN, false);
        startActivity(intent);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageHelper.onAttach(base));
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

    /**
     * It pops all the fragments which are currently in the backStack
     */
    public void clearFragmentBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
        for (int i = 0; i < backStackCount; i++) {
            int backStackId = getSupportFragmentManager().getBackStackEntryAt(i).getId();
            fm.popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public int stackCount() {
        return getSupportFragmentManager().getBackStackEntryCount();
    }

}
