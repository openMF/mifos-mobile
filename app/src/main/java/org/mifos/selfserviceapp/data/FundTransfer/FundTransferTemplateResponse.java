package org.mifos.selfserviceapp.data.FundTransfer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vishwajeet
 * @since 23/8/16.
 */

public class FundTransferTemplateResponse {
    private List<FundTransferTemplate> fromAccountOptions = new ArrayList<>();
    private List<FundTransferTemplate> toAccountOptions = new ArrayList<>();

    public List<FundTransferTemplate> getFromAccountOptions() {
        return fromAccountOptions;
    }

    public void setFromAccountOptions(List<FundTransferTemplate> fromAccountOptions) {
        this.fromAccountOptions = fromAccountOptions;
    }

    public List<FundTransferTemplate> getToAccountOptions() {
        return toAccountOptions;
    }

    public void setToAccountOptions(List<FundTransferTemplate> toAccountOptions) {
        this.toAccountOptions = toAccountOptions;
    }
}
