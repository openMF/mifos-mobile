package org.mifos.selfserviceapp.ui.views;

import org.mifos.selfserviceapp.data.FundTransfer.FundTransferResponse;
import org.mifos.selfserviceapp.data.FundTransfer.FundTransferTemplateResponse;
import org.mifos.selfserviceapp.ui.views.base.MVPView;

/**
 * @author Vishwajeet
 * @since 22/8/16.
 */

public interface FundTransferView extends MVPView {

    void showFundTransferTemplate(FundTransferTemplateResponse fundTransferTemplate);

    void submitPayment(FundTransferResponse fundTransferResponse);

    void showErrorFetchingFundTransfers(String message);
}
