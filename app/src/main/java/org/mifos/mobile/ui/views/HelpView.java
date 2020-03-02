package org.mifos.mobile.ui.views;

import org.mifos.mobile.models.FAQ;
import org.mifos.mobile.ui.views.base.MVPView;

import java.util.ArrayList;

/**
 * Created by dilpreet on 12/8/17.
 */

public interface HelpView extends MVPView {

    void showFaq(ArrayList<FAQ> faqArrayList);

}
