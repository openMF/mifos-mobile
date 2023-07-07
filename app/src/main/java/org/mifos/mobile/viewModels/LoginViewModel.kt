package org.mifos.mobile.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.mifos.mobile.models.Page
import org.mifos.mobile.models.User
import org.mifos.mobile.models.client.Client
import org.mifos.mobile.repositories.UserAuthRepository
import org.mifos.mobile.utils.LoginUiState
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userAuthRepositoryImp: UserAuthRepository) :
    ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private var _loginUiState = MutableLiveData<LoginUiState>()
    val loginUiState: LiveData<LoginUiState> get() = _loginUiState

    fun isFieldEmpty(fieldText: String): Boolean {
        return fieldText.isEmpty()
    }

    fun isUsernameLengthInadequate(username: String): Boolean {
        return username.length < 5
    }

    fun isPasswordLengthInadequate(password: String): Boolean {
        return password.length < 6
    }

    fun usernameHasSpaces(username: String): Boolean {
        return username.trim().contains(" ")
    }

    /**
     * This method attempts to authenticate the user from
     * the server and then persist the authentication data if we successfully
     * authenticate the credentials or notify about any errors.
     */
    fun login(username: String, password: String) {
        _loginUiState.value = LoginUiState.Loading
        compositeDisposable.add(
            userAuthRepositoryImp.login(username, password)
                ?.observeOn(AndroidSchedulers.mainThread())?.subscribeOn(Schedulers.io())!!
                .subscribeWith(object : DisposableObserver<User?>() {
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        try {
                            if (e is HttpException) {
                                if (e.code() == 503) {
                                    _loginUiState.value = LoginUiState.ServerDownError
                                } else {
                                    _loginUiState.value =
                                        LoginUiState.DynamicError(e.response().errorBody().string())
                                }
                            }
                            if (e is UnknownHostException) {
                                _loginUiState.value = LoginUiState.DuringLoginError
                            }
                        } catch (throwable: Throwable) {
                            RxJavaPlugins.getErrorHandler()
                        }
                    }

                    override fun onNext(user: User) {
                        userAuthRepositoryImp.saveAuthenticationTokenForSession(user)
                        _loginUiState.value = LoginUiState.LoginSuccess
                    }
                }),
        )
    }

    /**
     * This method fetches the Client, associated with current Access Token.
     */
    fun loadClient() {
        userAuthRepositoryImp.loadClient()?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : DisposableObserver<Page<Client?>?>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {
                    if ((e as HttpException).code() == 401) {
                        _loginUiState.value = LoginUiState.UnauthorisedClientError
                    } else {
                        _loginUiState.value = LoginUiState.FetchingClientError
                    }
                    userAuthRepositoryImp.clearPrefHelper()
                    userAuthRepositoryImp.reInitializeService()
                }

                override fun onNext(clientPage: Page<Client?>) {
                    if (clientPage.pageItems.isNotEmpty()) {
                        val clientId = clientPage.pageItems[0]?.id?.toLong()
                        val clientName = clientPage.pageItems[0]?.displayName
                        userAuthRepositoryImp.setClientId(clientId)
                        userAuthRepositoryImp.reInitializeService()
                        _loginUiState.value = LoginUiState.LoadClientSuccess(clientName)
                    } else {
                        _loginUiState.value = LoginUiState.ClientNotFoundError
                    }
                }
            })?.let {
                compositeDisposable.add(
                    it,
                )
            }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}