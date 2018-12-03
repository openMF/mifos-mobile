package org.mifos.mobilebanking.ui.views;

import org.mifos.mobilebanking.models.beneficiary.Beneficiary;
import org.mifos.mobilebanking.ui.views.base.MVPView;

import java.util.List;

/**
 * Created by dilpreet on 14/6/17.
 */

public interface BeneficiariesView extends MVPView {

    void showUserInterface();

    void showError(String msg);

    void showBeneficiaryList(List<Beneficiary> beneficiaryList);
}
