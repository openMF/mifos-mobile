package org.mifos.mobilebanking.ui.views;

import org.mifos.mobilebanking.models.FAQ;
import org.mifos.mobilebanking.ui.views.base.MVPView;

import java.util.ArrayList;

/**
 * Created by dilpreet on 12/8/17.
 */

public interface HelpView extends MVPView {

    void showFaq(ArrayList<FAQ> faqArrayList);

}
