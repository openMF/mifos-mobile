package org.mifos.selfserviceapp.presenters;

import android.content.Context;
import android.content.res.Resources;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.BaseApiManager;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.api.local.PreferencesHelper;
import org.mifos.selfserviceapp.injection.ActivityContext;
import org.mifos.selfserviceapp.models.Page;
import org.mifos.selfserviceapp.models.User;
import org.mifos.selfserviceapp.models.client.Client;
import org.mifos.selfserviceapp.presenters.base.BasePresenter;
import org.mifos.selfserviceapp.ui.views.LoginView;
import org.mifos.selfserviceapp.utils.Constants;
import org.mifos.selfserviceapp.utils.MFErrorParser;

import javax.inject.Inject;

import retrofit2.HttpException;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.plugins.RxJavaPlugins;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Vishwajeet
 * @since 05/06/16
 */
public class LoginPresenter extends BasePresenter<LoginView> {

    private final DataManager dataManager;
    private PreferencesHelper preferencesHelper;
    private CompositeSubscription subscriptions;

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
    public LoginPresenter(DataManager dataManager, @ActivityContext Context context) {
        super(context);
        this.dataManager = dataManager;
        preferencesHelper = this.dataManager.getPreferencesHelper();
        subscriptions = new CompositeSubscription();
    }

    @Override
    public void attachView(LoginView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        subscriptions.unsubscribe();
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
            subscriptions.add(dataManager.login(username, password)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<User>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            getMvpView().hideProgress();
                            String errorMessage;
                            try {
                                if (e instanceof HttpException) {
                                    errorMessage =
                                            ((HttpException) e).response().errorBody().string();
                                    getMvpView().showMessage(MFErrorParser.parseError(errorMessage)
                                            .getDeveloperMessage());
                                }
                            } catch (Throwable throwable) {
                                RxJavaPlugins.getInstance().getErrorHandler().handleError(
                                        throwable);
                            }
                        }

                        @Override
                        public void onNext(User user) {
                            getMvpView().hideProgress();
                            if (user != null) {
                                final String userName = user.getUserName();
                                final long userID = user.getUserId();
                                final String authToken = Constants.BASIC +
                                        user.getBase64EncodedAuthenticationKey();
                                saveAuthenticationTokenForSession(userID, authToken);
                                getMvpView().onLoginSuccess(userName);
                            }
                        }
                    })
            );
        } else {
            getMvpView().hideProgress();
        }
    }

    /**
     * This method fetching the Client, associated with current Access Token.
     */
    public void loadClient() {
        checkViewAttached();
        getMvpView().showProgress();
        subscriptions.add(dataManager.getClients()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Page<Client>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showMessage(context.getString(R.string.error_fetching_client));
                        getMvpView().hideProgress();
                    }

                    @Override
                    public void onNext(Page<Client> clientPage) {
                        getMvpView().hideProgress();
                        if (clientPage.getPageItems().size() != 0) {
                            long clientId = clientPage.getPageItems().get(0).getId();
                            getMvpView().showClient(clientId);
                            preferencesHelper.setClientId(clientId);
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

        if (username == null || username.trim().isEmpty()) {
            showEmptyInputError(context.getString(R.string.username));
            return false;
        } else if (username.length() < 5) {
            showMinimumInputLengthNotAchievedError("Username",
                    resources.getInteger(R.integer.username_minimum_length));
            return false;
        } else if (username.contains(" ")) {
            getMvpView().showMessage("Username cannot contain white spaces");
            return false;
        } else if (password == null || password.trim().isEmpty()) {
            showEmptyInputError("Password cannot be empty");
            return false;
        } else if (password.length() < 6) {
            showMinimumInputLengthNotAchievedError("Password",
                    resources.getInteger(R.integer.password_minimum_length));
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
        BaseApiManager.createService(preferencesHelper.getToken());
    }

    /**
     * Notifies the view about an empty input in the given field name.
     *
     * @param fieldName Field name of the input that was empty
     */
    private void showEmptyInputError(String fieldName) {
        getMvpView().showMessage(context.getString(
                R.string.error_validation_blank, fieldName));
    }

    /**
     * Notifies the view that the user has not entered the minimum number of
     * characters the input requires.
     *
     * @param fieldName     Field name of the input.
     * @param minimumLength Minimum number of characters the field requires.
     */
    private void showMinimumInputLengthNotAchievedError(String fieldName, int minimumLength) {
        getMvpView().showMessage(context.getString(
                R.string.error_validation_minimum_chars,
                fieldName, minimumLength));
    }

}
