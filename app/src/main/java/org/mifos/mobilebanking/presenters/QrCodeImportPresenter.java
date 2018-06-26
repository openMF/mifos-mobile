package org.mifos.mobilebanking.presenters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;


import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.isseiaoki.simplecropview.CropImageView;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.injection.ApplicationContext;
import org.mifos.mobilebanking.presenters.base.BasePresenter;
import org.mifos.mobilebanking.ui.views.QrCodeImportView;

import java.util.EnumMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by manishkumar on 19/05/18.
 */

public class QrCodeImportPresenter extends BasePresenter<QrCodeImportView> {

    private Result result;
    private CompositeDisposable compositeDisposable;

    /**
     * Initialises the LoanAccountDetailsPresenter by automatically injecting an instance of
     * {@link Context}.
     *
     * @param context Context of the view attached to the presenter. In this case
     *                it is that of an {@link android.support.v7.app.AppCompatActivity}
     */
    @Inject
    public QrCodeImportPresenter(@ApplicationContext Context context) {
        super(context);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void attachView(QrCodeImportView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
        context = null;
    }

    /**
     * This method decodes the Qr code from the selected region
     *
     * @param sourceUri     contains the Uri of qr code from gallery
     * @param cropImageView contains cropImageView of layout
     */
    public void getDecodedResult(Uri sourceUri, final CropImageView cropImageView) {
        checkViewAttached();
        getMvpView().showProgress();
        compositeDisposable.add(cropImageView.crop(sourceUri)
                .executeAsSingle()
                .flatMap(new Function<Bitmap, SingleSource<Result>>() {
                    @Override
                    public SingleSource<Result> apply(@NonNull Bitmap bMap)
                            throws Exception {

                        int[] intArray = new int[bMap.getWidth() * bMap.getHeight()];
                        //copy pixel data from the Bitmap into the 'intArray' array
                        bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0,
                                bMap.getWidth(), bMap.getHeight());
                        LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(),
                                bMap.getHeight(), intArray);
                        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                        //flags for decoding since it is large file
                        Map<DecodeHintType, Boolean> tmpHintsMap
                                = new EnumMap<>(DecodeHintType.class);
                        tmpHintsMap.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
                        tmpHintsMap.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);

                        Reader reader = new MultiFormatReader();

                        try {
                            result = reader.decode(bitmap, tmpHintsMap);
                        } catch (Exception e) {
                            getMvpView().hideProgress();
                            getMvpView().showErrorReadingQr(context
                                    .getString(R.string.error_reading_qr));
                        }
                        return Single.just(result);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Result>() {
                    @Override
                    public void accept(Result result) throws Exception {
                        getMvpView().hideProgress();
                        getMvpView().handleDecodedResult(result);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getMvpView().hideProgress();
                        getMvpView()
                                .showErrorReadingQr(context.getString(R.string.error_reading_qr));
                    }
                }));

    }


}
