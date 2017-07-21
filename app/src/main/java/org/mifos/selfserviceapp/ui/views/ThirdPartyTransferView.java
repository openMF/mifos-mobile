package org.mifos.selfserviceapp.ui.views;

import org.mifos.selfserviceapp.models.beneficary.Beneficiary;
import org.mifos.selfserviceapp.models.templates.account.AccountOptionsTemplate;
import org.mifos.selfserviceapp.ui.views.base.MVPView;

import java.util.List;

/**
 * Created by dilpreet on 21/6/17.
 */

public interface ThirdPartyTransferView extends MVPView {

    void showUserInterface();

    void showToaster(String msg);

    void showThirdPartyTransferTemplate(AccountOptionsTemplate accountOptionsTemplate);

    void showBeneficiaryList(List<Beneficiary> beneficiaries);

    void showError(String msg);
}
