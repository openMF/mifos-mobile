package org.mifos.mobilebanking.presenters.base;

import android.content.Context;

import org.mifos.mobilebanking.ui.views.base.MVPView;

/**
 * @author ishan
 * @since 19/05/16
 */
public class BasePresenter<T extends MVPView> implements Presenter<T> {

    protected Context context;
    private T mMvpView;

    protected BasePresenter(Context context) {
        this.context = context;
    }

    @Override
    public void attachView(T mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        mMvpView = null;
    }

    public boolean isViewAttached() {
        return mMvpView != null;
    }

    public T getMvpView() {
        return mMvpView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.attachView(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }

}
