package org.mifos.mobilebanking.presenters;

import android.content.Context;
import android.content.res.Resources;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.api.BaseApiManager;
import org.mifos.mobilebanking.api.DataManager;
import org.mifos.mobilebanking.api.local.PreferencesHelper;
import org.mifos.mobilebanking.injection.ApplicationContext;
import org.mifos.mobilebanking.models.Page;
import org.mifos.mobilebanking.models.User;
import org.mifos.mobilebanking.models.client.Client;
import org.mifos.mobilebanking.presenters.base.BasePresenter;
import org.mifos.mobilebanking.ui.views.LoginView;
import org.mifos.mobilebanking.utils.Constants;
import org.mifos.mobilebanking.utils.MFErrorParser;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

/**
 * @author Vishwajeet
 * @since 05/06/16
 */
public class LoginPresenter extends BasePresenter<LoginView> {

    private final DataManager dataManager;
    private PreferencesHelper preferencesHelper;
    private CompositeDisposable compositeDisposable;

    /**
     * Initialises the LoginPresenter by automatically injecting an instance of
     * {@link DataManager} and {@link Context}.
     *
     * @param dataManager DataManager class that provides access to the data
     *                    via the API.
     * @param context     Context of the view attached to the presenter. In this case
     *                    it is that of an {@link android.support.v7.app.AppCompatActivity}
     */
    @Inject
    public LoginPresenter(DataManager dataManager, @ApplicationContext Context context) {
        super(context);
        this.dataManager = dataManager;
        preferencesHelper = this.dataManager.getPreferencesHelper();
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(LoginView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }

    /**
     * This method validates the username and password entered by the user
     * and reports any errors that might exists in any of the inputs.
     * If there are no errors, then we attempt to authenticate the user from
     * the server and then persist the authentication data if we successfully
     * authenticate the credentials or notify the view about any errors.
     *
     * @param username Username of the user trying to login.
     * @param password Password of the user trying to login.
     */
    public void login(final String username, final String password) {
        checkViewAttached();
        if (isCredentialsValid(username, password)) {
            getMvpView().showProgress();
            compositeDisposable.add(dataManager.login(username, password)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(new DisposableObserver<User>() {
                        @Override
                        public void onComplete() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            getMvpView().hideProgress();
                            String errorMessage;
                            try {
                                if (e instanceof HttpException) {
                                    if (((HttpException) e).code() == 503) {
                                        getMvpView().
                                                showMessage(context.
                                                        getString(R.string.error_server_down));
                                    } else {
                                        errorMessage =
                                                ((HttpException) e).response().errorBody().string();
                                        getMvpView()
                                                .showMessage(MFErrorParser.parseError(errorMessage)
                                                .getDeveloperMessage());
                                    }
                                }
                            } catch (Throwable throwable) {
                                RxJavaPlugins.getErrorHandler();
                            }
                        }

                        @Override
                        public void onNext(User user) {
                            if (user != null) {
                                final String userName = user.getUsername();
                                final long userID = user.getUserId();
                                final String authToken = Constants.BASIC +
                                        user.getBase64EncodedAuthenticationKey();
                                saveAuthenticationTokenForSession(userID, authToken);
                                getMvpView().onLoginSuccess(userName);
                            } else {
                                getMvpView().hideProgress();
                            }
                        }
                    })
            );
        }
    }

    /**
     * This method fetching the Client, associated with current Access Token.
     */
    public void loadClient() {
        checkViewAttached();
        compositeDisposable.add(dataManager.getClients()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<Page<Client>>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgress();
                        if (((HttpException) e).code() == 401) {
                            getMvpView().showMessage(context.getString(R.string.
                                    unauthorized_client));
                        } else {
                            getMvpView().showMessage(context.getString(R.string.
                                    error_fetching_client));
                        }
                        preferencesHelper.clear();
                        reInitializeService();
                    }

                    @Override
                    public void onNext(Page<Client> clientPage) {
                        getMvpView().hideProgress();
                        if (clientPage.getPageItems().size() != 0) {
                            long clientId = clientPage.getPageItems().get(0).getId();
                            preferencesHelper.setClientId(clientId);
                            dataManager.setClientId(clientId);
                            reInitializeService();
                            getMvpView().showPassCodeActivity();
                        } else {
                            getMvpView().showMessage(context
                                    .getString(R.string.error_client_not_found));
                        }
                    }
                })
        );
    }


    private boolean isCredentialsValid(final String username, final String password) {

        final Resources resources = context.getResources();
        final String correctUsername = username.replaceFirst("\\s++$", "").trim();
        if (username == null || username.matches("\\s*") || username.isEmpty()) {
            getMvpView().showUsernameError(context.getString(R.string.error_validation_blank,
                    context.getString(R.string.username)));
            return false;
        } else if (username.length() < 5) {
            getMvpView().showUsernameError(context.getString(R.string.error_validation_minimum_chars
                    , resources.getString(R.string.username), resources.getInteger(R.integer.
                            username_minimum_length)));
            return false;
        } else if (correctUsername.contains(" ")) {
            getMvpView().showUsernameError(context.getString(
                    R.string.error_validation_cannot_contain_spaces,
                    resources.getString(R.string.username),
                    context.getString(R.string.not_contain_username)));
            return false;
        } else if (password == null || password.isEmpty()) {
            getMvpView().showPasswordError(context.getString(R.string.error_validation_blank,
                    context.getString(R.string.password)));
            return false;
        } else if (password.length() < 6) {
            getMvpView().showPasswordError(context.getString(R.string.error_validation_minimum_chars
                    , resources.getString(R.string.password), resources.getInteger(R.integer.
                            password_minimum_length)));
            return false;
        }

        return true;
    }

    /**
     * Save the authentication token from the server and the user ID.
     * The authentication token would be used for accessing the authenticated
     * APIs.
     *
     * @param userID    - The userID of the user to be saved.
     * @param authToken - The authentication token to be saved.
     */
    private void saveAuthenticationTokenForSession(long userID, String authToken) {
        preferencesHelper.setUserId(userID);
        preferencesHelper.saveToken(authToken);
        reInitializeService();
    }

    private void reInitializeService() {
        BaseApiManager.createService(preferencesHelper.getBaseUrl(), preferencesHelper.getTenant(),
                preferencesHelper.getToken());
    }
}
