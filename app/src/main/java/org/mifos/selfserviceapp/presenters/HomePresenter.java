package org.mifos.selfserviceapp.presenters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import org.mifos.selfserviceapp.R;
import org.mifos.selfserviceapp.api.DataManager;
import org.mifos.selfserviceapp.injection.ActivityContext;
import org.mifos.selfserviceapp.models.Page;
import org.mifos.selfserviceapp.models.client.Client;
import org.mifos.selfserviceapp.presenters.base.BasePresenter;
import org.mifos.selfserviceapp.ui.views.HomeView;
import org.mifos.selfserviceapp.utils.Base64toBitmap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by f390 on 14/3/17.
 */

public class HomePresenter extends BasePresenter<HomeView> {

    private final DataManager dataManager;
    private CompositeSubscription subscriptions;

    @Inject
    public HomePresenter(DataManager dataManager, @ActivityContext Context context) {
        super(context);
        this.dataManager = dataManager;
        subscriptions = new CompositeSubscription();
    }

    @Override
    public void attachView(HomeView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        subscriptions.unsubscribe();
    }

    public void getImage() throws IOException {
        checkViewAttached();
        getMvpView().showProgress();

        Call<ResponseBody> imageResponse = dataManager.getClientImage();
        imageResponse.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String byte64image = null;
                try {
                    byte64image = response.body().string();
                } catch (Exception e) {
                    Log.e("Error converting", e.getMessage());
                }
                Base64toBitmap base64toBitmap = new Base64toBitmap();
                Bitmap recievedUserImage = base64toBitmap.toBitmap(byte64image);
                if (recievedUserImage == null) {
                    Log.e("Error in base 64 image", base64toBitmap.getError().toString());
                    getMvpView().showMessage("User image not available");
                    recievedUserImage = ((BitmapDrawable) context.getResources().

                            getDrawable(R.drawable.ic_material_user_icon_black_24dp)).getBitmap();
                } else {
                    //Reduce Image size to 50 kb
                    double compressionRatio = (1.0 * 1024 * 50) /
                            (recievedUserImage.getRowBytes() *
                                    recievedUserImage.getHeight());
                    compressionRatio = compressionRatio <= 0.05 ? 0.05
                            : compressionRatio > 0.5 ? 0.5
                            : compressionRatio;
                    recievedUserImage.compress(Bitmap.CompressFormat.JPEG,
                            (int) compressionRatio * 100,
                            new ByteArrayOutputStream());
                }

                getMvpView().setUserImage(recievedUserImage);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                getMvpView().setUserImage(((BitmapDrawable) context.getResources().
                        getDrawable(R.drawable.ic_material_user_icon_black_24dp)).getBitmap());
            }
        });

    }


    public void getUserName() {
        checkViewAttached();
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
                        if (clientPage.getPageItems().size() != 0
                                && clientPage.getPageItems().get(0).getDisplayName() != null) {
                            getMvpView().setUserName(
                                    clientPage.getPageItems().get(0).getDisplayName());
                        } else {
                            getMvpView().showMessage(context
                                    .getString(R.string.error_client_not_found));
                            getMvpView().setUserName("N/A");
                        }
                        getMvpView().hideProgress();
                    }
                })
        );
    }
}



