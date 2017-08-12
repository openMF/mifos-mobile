package org.mifos.selfserviceapp.ui.views;

import org.mifos.selfserviceapp.models.FAQ;
import org.mifos.selfserviceapp.ui.views.base.MVPView;

import java.util.ArrayList;

/**
 * Created by dilpreet on 12/8/17.
 */

public interface HelpView extends MVPView {

    void showFaq(ArrayList<FAQ> faqArrayList);

}
