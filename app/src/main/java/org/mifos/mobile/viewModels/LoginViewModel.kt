package org.mifos.mobile.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.mifos.mobile.MifosSelfServiceApp.Companion.context
import org.mifos.mobile.R
import org.mifos.mobile.api.BaseApiManager
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.api.local.PreferencesHelper
import org.mifos.mobile.models.Page
import org.mifos.mobile.models.User
import org.mifos.mobile.models.client.Client
import org.mifos.mobile.models.payload.LoginPayload
import org.mifos.mobile.utils.Constants
import org.mifos.mobile.utils.MFErrorParser
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val dataManager: DataManager?) : ViewModel() {
    private val preferencesHelper: PreferencesHelper? = dataManager?.preferencesHelper
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    var error = MutableLiveData<String>()
    var success = MutableLiveData<String>()


    fun login(loginPayload: LoginPayload?) {
        if (isCredentialsValid(loginPayload)) {
            compositeDisposable.add(
                dataManager?.login(loginPayload)
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribeOn(Schedulers.io())!!
                    .subscribeWith(object : DisposableObserver<User?>() {
                        override fun onComplete() {}
                        override fun onError(e: Throwable) {
                            val errorMessage: String
                            try {
                                if (e is HttpException) {
                                    if (e.code() == 503) {
                                        error.value = "server down"
                                    } else {
                                        errorMessage = e.response().errorBody().string()
//                                        mvpView
//                                            ?.showMessage(
//                                                MFErrorParser.parseError(errorMessage)
//                                                    .developerMessage,
//                                            )
                                        error.value = errorMessage
                                    }
                                }
                                if (e is UnknownHostException) {
                                    error.value = "error during sign in"
                                }
                            } catch (throwable: Throwable) {
                                RxJavaPlugins.getErrorHandler()
                            }
                        }

                        override fun onNext(user: User) {
                            val userName = user.username
                            val userID = user.userId
                            val authToken = Constants.BASIC +
                                    user.base64EncodedAuthenticationKey
                            saveAuthenticationTokenForSession(userName, userID, authToken)
                            success.value = "success"
                        }
                    }),
            )
        }
    }

    fun loadClient() {
        dataManager?.clients
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<Page<Client?>?>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    if ((e as HttpException).code() == 401) {
                        error.value = "not authorized"
                    } else {
                        error.value = "error_fetching_client"
                    }

                    preferencesHelper?.clear()
                    reInitializeService()
                }

                override fun onNext(clientPage: Page<Client?>) {
                    if (clientPage.pageItems.isNotEmpty()) {
                        val clientId = clientPage.pageItems[0]?.id?.toLong()
                        val clientName = clientPage.pageItems[0]?.displayName
                        preferencesHelper?.clientId = clientId
                        dataManager.clientId = clientId
                        reInitializeService()
//                        mvpView?.showPassCodeActivity(clientName)
                    } else {
                        error.value = "error_client_not_found"
                    }
                }
            })?.let {
                compositeDisposable.add(
                    it,
                )
            }
    }

    private fun isCredentialsValid(loginPayload: LoginPayload?): Boolean {
        val username: String = loginPayload?.username.toString()
        val password: String = loginPayload?.password.toString()
        var credentialValid = true
        val correctUsername = username.replaceFirst("\\s++$".toRegex(), "").trim { it <= ' ' }
        when {
            username.isEmpty() -> {
//                mvpView?.showUsernameError(
//                    context?.getString(
//                        R.string.error_validation_blank,
//                        context?.getString(R.string.username),
//                    ),
//                )
                error.value = "error_validation_blank"
                credentialValid = false
            }

            username.length < 5 -> {
//                mvpView?.showUsernameError(
//                    context?.getString(
//                        R.string.error_validation_minimum_chars,
//                        resources?.getString(R.string.username),
//                        resources?.getInteger(R.integer.username_minimum_length),
//                    ),
//                )
                error.value = "error_validation_minimum_chars"
                credentialValid = false
            }

            correctUsername.contains(" ") -> {
//                mvpView?.showUsernameError(
//                    context?.getString(
//                        R.string.error_validation_cannot_contain_spaces,
//                        resources?.getString(R.string.username),
//                        context?.getString(R.string.not_contain_username),
//                    ),
//                )
                error.value = "error_validation_cannot_contain_spaces"
                credentialValid = false
            }

            else -> {
//                mvpView?.clearUsernameError()
            }
        }
        when {
            password.isEmpty() -> {
//                mvpView?.showPasswordError(
//                    context?.getString(
//                        R.string.error_validation_blank,
//                        context?.getString(R.string.password),
//                    ),
//                )
                error.value = "error_validation_blank"
                credentialValid = false
            }

            password.length < 6 -> {
//                mvpView?.showPasswordError(
//                    context?.getString(
//                        R.string.error_validation_minimum_chars,
//                        resources?.getString(R.string.password),
//                        resources?.getInteger(R.integer.password_minimum_length),
//                    ),
//                )
                error.value = "error_validation_minimum_chars"
                credentialValid = false
            }

            else -> {
//                mvpView?.clearPasswordError()
            }
        }
        return credentialValid
    }

    private fun saveAuthenticationTokenForSession(
        userName: String?,
        userID: Long,
        authToken: String,
    ) {
        preferencesHelper?.userName = userName
        preferencesHelper?.userId = userID
        preferencesHelper?.saveToken(authToken)
        reInitializeService()
    }

    private fun reInitializeService() {
        BaseApiManager.createService(
            preferencesHelper?.baseUrl,
            preferencesHelper?.tenant,
            preferencesHelper?.token,
        )
    }
}