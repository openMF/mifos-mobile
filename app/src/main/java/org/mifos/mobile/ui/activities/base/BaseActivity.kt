package org.mifos.mobile.ui.activities.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.Toolbar

import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

import com.mifos.mobile.passcode.BasePassCodeActivity
import org.mifos.mobile.MifosSelfServiceApp
import org.mifos.mobile.R
import org.mifos.mobile.injection.component.ActivityComponent
import org.mifos.mobile.injection.component.DaggerActivityComponent
import org.mifos.mobile.injection.module.ActivityModule
import org.mifos.mobile.ui.activities.PassCodeActivity
import org.mifos.mobile.ui.views.BaseActivityCallback
import org.mifos.mobile.utils.LanguageHelper

/**
 * @author ishan
 * @since 08/07/16
 */
@SuppressLint("Registered")
open class BaseActivity : BasePassCodeActivity(), BaseActivityCallback {
    /**
     * @return Returns toolbar linked with current activity
     */
    var toolbar: Toolbar? = null
        protected set

    /**
     * Used for dependency injection
     * @return [ActivityComponent] which is used for injection
     */
    var activityComponent: ActivityComponent? = null
        get() {
            if (field == null) {
                field = DaggerActivityComponent.builder()
                        .activityModule(ActivityModule(this))
                        .applicationComponent(MifosSelfServiceApp.get(this).component())
                        .build()
            }
            return field
        }
        private set
    private var progress: ProgressDialog? = null
    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        toolbar = findViewById(R.id.toolbar)
        if (toolbar != null) {
            setSupportActionBar(toolbar)
            setToolbarElevation()
        }
    }

    /**
     * Used for removing elevation from toolbar
     */
    fun hideToolbarElevation() {
        ViewCompat.setElevation(toolbar!!, 0f)
    }

    /**
     * Used for setting toolbar elevation
     */
    fun setToolbarElevation() {
        ViewCompat.setElevation(toolbar!!, 8f)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
     * This method is use to provide back button feature in the toolbar of activities
     */
    protected fun showBackButton() {
        if (supportActionBar != null) {
            supportActionBar!!.setHomeButtonEnabled(true)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Displays a toast in current activity. In this method the duration
     * supplied is Short by default. If you want to specify duration
     * use [BaseActivity.showToast] method.
     *
     * @param message Message that the toast must show.
     */
    fun showToast(message: String) {
        showToast(message, Toast.LENGTH_SHORT)
    }

    /**
     * Displays a toast in current activity. The duration can of two types:
     *
     *  * SHORT
     *  * LONG
     *
     *
     * @param message   Message that the toast must show.
     * @param toastType Duration for which the toast must be visible.
     */
    fun showToast(message: String, toastType: Int) {
        Toast.makeText(this@BaseActivity, message, toastType).show()
    }

    /**
     * Calls a method `showProgressDialog("Working")` which displays ProgressDialog
     */
    fun showProgressDialog() {
        showProgressDialog(getString(R.string.working))
    }

    /**
     * Displays a ProgressDialog
     * @param message Message you want to display in Progress Dialog
     */
    override fun showProgressDialog(message: String?) {
        if (progress == null) {
            progress = ProgressDialog(this, ProgressDialog.STYLE_SPINNER)
            progress!!.setCancelable(false)
        }
        progress!!.setMessage(message)
        progress!!.show()
    }

    /**
     * Hides the progress dialog if it is currently being shown
     */
    override fun hideProgressDialog() {
        if (progress != null && progress!!.isShowing) {
            progress!!.dismiss()
            progress = null
        }
    }

    /**
     * Used for setting title of Toolbar
     * @param title String you want to display as title
     */
    fun setActionBarTitle(title: String?) {
        if (supportActionBar != null && getTitle() != null) {
            setTitle(title)
        }
    }

    protected fun setActionBarTitle(title: Int) {
        setActionBarTitle(resources.getString(title))
    }

    /**
     * Calls `setActionBarTitle()` to set Toolbar title
     * @param title String you want to set as title
     */
    override fun setToolbarTitle(title: String?) {
        setActionBarTitle(title)
    }

    override fun getPassCodeClass(): Class<*> {
        return PassCodeActivity::class.java
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LanguageHelper.onAttach(base))
    }

    /**
     * Replace Fragment in FrameLayout Container.
     *
     * @param fragment Fragment
     * @param addToBackStack Add to BackStack
     * @param containerId Container Id
     */
    fun replaceFragment(fragment: Fragment, addToBackStack: Boolean, containerId: Int) {
        invalidateOptionsMenu()
        val backStateName = fragment.javaClass.name
        val fragmentPopped = supportFragmentManager.popBackStackImmediate(backStateName,
                0)
        if (!fragmentPopped && supportFragmentManager.findFragmentByTag(backStateName) ==
                null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(containerId, fragment, backStateName)
            if (addToBackStack) {
                transaction.addToBackStack(backStateName)
            }
            transaction.commit()
        }
    }

    /**
     * It pops all the fragments which are currently in the backStack
     */
    fun clearFragmentBackStack() {
        val fm = supportFragmentManager
        val backStackCount = supportFragmentManager.backStackEntryCount
        for (i in 0 until backStackCount) {
            val backStackId = supportFragmentManager.getBackStackEntryAt(i).id
            fm.popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    fun stackCount(): Int {
        return supportFragmentManager.backStackEntryCount
    }

    companion object {
        fun hideKeyboard(context: Context) {
            val activity = context as Activity
            val inputMethodManager = context.getSystemService(
                    Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                    activity.currentFocus.windowToken, 0)
        }
    }
}