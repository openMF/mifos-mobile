package org.mifos.mobile.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import org.mifos.mobile.R
import org.mifos.mobile.databinding.ActivityLoginBinding
import org.mifos.mobile.models.payload.LoginPayload
import org.mifos.mobile.presenters.LoginPresenter
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.views.LoginView
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.Network
import org.mifos.mobile.utils.Toaster
import org.mifos.mobile.viewModels.LoginViewModel
import org.mifos.mobile.viewModels.LoginViewModelFactory
import javax.inject.Inject

/**
 * @author Vishwajeet
 * @since 05/06/16
 */
class LoginActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: LoginViewModelFactory

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        activityComponent?.inject(this)
        setContentView(binding.root)
        dismissSoftKeyboardOnBkgTap(binding.nsvBackground)
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[LoginViewModel::class.java]
        viewModel.success.observe(this) {
            if(it.equals("success")) {
                hideProgressDialog()
                startPassCodeActivity()
                viewModel.loadClient()
            }
        }
        viewModel.error.observe(this) {
            if (it.equals("error_validation_blank")){
                hideProgressDialog()
                Toast.makeText(this, "Blank",Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnLogin.setOnClickListener {
            onLoginClicked()
        }
        binding.btnRegister.setOnClickListener {
            onRegisterClicked()
        }
        binding.etUsername.setOnTouchListener { view, event ->
            onTouch(view)
        }

        binding.etPassword.setOnTouchListener { view, event ->
            onTouch(view)
        }
    }

    private fun dismissSoftKeyboardOnBkgTap(view: View) {
        if (view !is EditText) {
            view.setOnTouchListener { view, event ->
                hideKeyboard(this@LoginActivity)
                false
            }
        }
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                dismissSoftKeyboardOnBkgTap(innerView)
            }
        }
    }

    fun onTouch(v: View): Boolean {
        when (v) {
//            binding.etUsername -> loginPresenter?.mvpView?.clearUsernameError()
//            binding.etPassword -> loginPresenter?.mvpView?.clearPasswordError()
        }
        return false
    }
//    override fun showPassCodeActivity(clientName: String?) {
//        showToast(getString(R.string.toast_welcome, clientName))
//        startPassCodeActivity()
//    }
//    override fun showMessage(errorMessage: String?) {
//        showToast(errorMessage!!, Toast.LENGTH_LONG)
//        binding.llLogin.visibility = View.VISIBLE
//    }
//
//    override fun showUsernameError(error: String?) {
//        binding.tilUsername.error = error
//    }
//
//    override fun showPasswordError(error: String?) {
//        binding.tilPassword.error = error
//    }
//
//    override fun clearUsernameError() {
//        binding.tilUsername.isErrorEnabled = false
//    }
//
//    override fun clearPasswordError() {
//        binding.tilPassword.isErrorEnabled = false
//    }

    /**
     * Called when Login Button is clicked, used for logging in the user
     */

    fun onLoginClicked() {
        val username = binding.tilUsername.editText?.editableText.toString()
        val password = binding.tilPassword.editText?.editableText.toString()
        if (Network.isConnected(this)) {
            val payload = LoginPayload()
            payload.username = username
            payload.password = password
            showProgressDialog()
            viewModel.login(payload)
        } else {
            Toaster.show(binding.llLogin, getString(R.string.no_internet_connection))
        }
    }

    private fun onRegisterClicked() {
        startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
    }

    /**
     * Starts [PassCodeActivity] with `Constans.INTIAL_LOGIN` as true
     */

    private fun startPassCodeActivity() {
        val intent = Intent(this@LoginActivity, PassCodeActivity::class.java)
        intent.putExtra(Constants.INTIAL_LOGIN, true)
        startActivity(intent)
        finish()
    }
}
