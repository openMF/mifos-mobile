package org.mifos.mobilebanking.models.accounts.savings;

/*
 * Created by saksham on 02/July/2018
 */

public class SavingsAccountWithdrawPayload {

    String locale = "en";
    String dateFormat = "dd MMMM yyyy";
    String withdrawnOnDate;
    String note;

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getWithdrawnOnDate() {
        return withdrawnOnDate;
    }

    public void setWithdrawnOnDate(String withdrawnOnDate) {
        this.withdrawnOnDate = withdrawnOnDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
