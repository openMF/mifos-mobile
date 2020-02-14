package org.mifos.mobile;

import android.content.Context;

import com.google.zxing.Result;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.decoder.Version;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.QRCode;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.mifos.mobile.api.DataManager;
import org.mifos.mobile.api.local.PreferencesHelper;
import org.mifos.mobile.models.accounts.loan.LoanAccount;
import org.mifos.mobile.models.accounts.savings.SavingAccount;
import org.mifos.mobile.models.client.Client;
import org.mifos.mobile.models.client.ClientAccounts;
import org.mifos.mobile.presenters.QrCodeImportPresenter;
import org.mifos.mobile.ui.views.HomeOldView;
import org.mifos.mobile.util.RxSchedulersOverrideRule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QrCodeImportPresenterTest {

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    Context context;

    @Mock
    DataManager dataManager;

    @Mock
    PreferencesHelper mockHelper;

    public QrCodeImportPresenter presenter;

    @Before
    public void setUp() throws Exception {
        presenter = new QrCodeImportPresenter(dataManager, context);
        presenter.attachView(view);
    }

    @After
    public void tearDown() throws Exception {
        presenter.detachView();
    }

    @Test
    public void getDecodedResult() {
        QRCode.setMode(set.view);
        QRCode.setECLevel(ErrorCorrectionLevel.H);
        QRCode.setVersion(Version.getVersionForNumber(7));
        QRCode.setMaskPattern(3);

        ByteMatrix matrix = new ByteMatrix(45, 45);
        // Just set bogus zero/one values.
        for (int y = 0; y < 45; ++y) {
            for (int x = 0; x < 45; ++x) {
                matrix.set(x, y, (y + x) % 2);
            }
        }

        QRCode.setMatrix(matrix);
        assertSame(matrix, QRCode.getMatrix());
    }
}    