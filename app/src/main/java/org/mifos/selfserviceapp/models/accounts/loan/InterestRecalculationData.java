package org.mifos.selfserviceapp.models.accounts.loan;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.mifos.selfserviceapp.models.accounts.loan.calendardata.CalendarData;

/**
 * Created by Rajan Maurya on 04/03/17.
 */

public class InterestRecalculationData implements Parcelable {

    @SerializedName("id")
    Integer id;

    @SerializedName("loanId")
    Integer loanId;

    @SerializedName("interestRecalculationCompoundingType")
    InterestRecalculationCompoundingType interestRecalculationCompoundingType;

    @SerializedName("rescheduleStrategyType")
    RescheduleStrategyType rescheduleStrategyType;

    @SerializedName("calendarData")
    CalendarData calendarData;

    @SerializedName("recalculationRestFrequencyType")
    RecalculationRestFrequencyType recalculationRestFrequencyType;

    @SerializedName("recalculationRestFrequencyInterval")
    Double recalculationRestFrequencyInterval;

    @SerializedName("recalculationCompoundingFrequencyType")
    RecalculationCompoundingFrequencyType recalculationCompoundingFrequencyType;

    @SerializedName("isCompoundingToBePostedAsTransaction")
    Boolean isCompoundingToBePostedAsTransaction;

    @SerializedName("allowCompoundingOnEod")
    Boolean allowCompoundingOnEod;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLoanId() {
        return loanId;
    }

    public void setLoanId(Integer loanId) {
        this.loanId = loanId;
    }

    public InterestRecalculationCompoundingType getInterestRecalculationCompoundingType() {
        return interestRecalculationCompoundingType;
    }

    public void setInterestRecalculationCompoundingType(
            InterestRecalculationCompoundingType interestRecalculationCompoundingType) {
        this.interestRecalculationCompoundingType = interestRecalculationCompoundingType;
    }

    public RescheduleStrategyType getRescheduleStrategyType() {
        return rescheduleStrategyType;
    }

    public void setRescheduleStrategyType(
            RescheduleStrategyType rescheduleStrategyType) {
        this.rescheduleStrategyType = rescheduleStrategyType;
    }

    public CalendarData getCalendarData() {
        return calendarData;
    }

    public void setCalendarData(
            CalendarData calendarData) {
        this.calendarData = calendarData;
    }

    public RecalculationRestFrequencyType getRecalculationRestFrequencyType() {
        return recalculationRestFrequencyType;
    }

    public void setRecalculationRestFrequencyType(
            RecalculationRestFrequencyType recalculationRestFrequencyType) {
        this.recalculationRestFrequencyType = recalculationRestFrequencyType;
    }

    public Double getRecalculationRestFrequencyInterval() {
        return recalculationRestFrequencyInterval;
    }

    public void setRecalculationRestFrequencyInterval(Double recalculationRestFrequencyInterval) {
        this.recalculationRestFrequencyInterval = recalculationRestFrequencyInterval;
    }

    public RecalculationCompoundingFrequencyType getRecalculationCompoundingFrequencyType() {
        return recalculationCompoundingFrequencyType;
    }

    public void setRecalculationCompoundingFrequencyType(
            RecalculationCompoundingFrequencyType recalculationCompoundingFrequencyType) {
        this.recalculationCompoundingFrequencyType = recalculationCompoundingFrequencyType;
    }

    public Boolean getCompoundingToBePostedAsTransaction() {
        return isCompoundingToBePostedAsTransaction;
    }

    public void setCompoundingToBePostedAsTransaction(Boolean compoundingToBePostedAsTransaction) {
        isCompoundingToBePostedAsTransaction = compoundingToBePostedAsTransaction;
    }

    public Boolean getAllowCompoundingOnEod() {
        return allowCompoundingOnEod;
    }

    public void setAllowCompoundingOnEod(Boolean allowCompoundingOnEod) {
        this.allowCompoundingOnEod = allowCompoundingOnEod;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.loanId);
        dest.writeParcelable(this.interestRecalculationCompoundingType, flags);
        dest.writeParcelable(this.rescheduleStrategyType, flags);
        dest.writeParcelable(this.calendarData, flags);
        dest.writeParcelable(this.recalculationRestFrequencyType, flags);
        dest.writeValue(this.recalculationRestFrequencyInterval);
        dest.writeParcelable(this.recalculationCompoundingFrequencyType, flags);
        dest.writeValue(this.isCompoundingToBePostedAsTransaction);
        dest.writeValue(this.allowCompoundingOnEod);
    }

    public InterestRecalculationData() {
    }

    protected InterestRecalculationData(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.loanId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.interestRecalculationCompoundingType = in.readParcelable(
                InterestRecalculationCompoundingType.class.getClassLoader());
        this.rescheduleStrategyType = in.readParcelable(
                RescheduleStrategyType.class.getClassLoader());
        this.calendarData = in.readParcelable(CalendarData.class.getClassLoader());
        this.recalculationRestFrequencyType = in.readParcelable(
                RecalculationRestFrequencyType.class.getClassLoader());
        this.recalculationRestFrequencyInterval = (Double) in.readValue(
                Double.class.getClassLoader());
        this.recalculationCompoundingFrequencyType = in.readParcelable(
                RecalculationCompoundingFrequencyType.class.getClassLoader());
        this.isCompoundingToBePostedAsTransaction = (Boolean) in.readValue(
                Boolean.class.getClassLoader());
        this.allowCompoundingOnEod = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Parcelable.Creator<InterestRecalculationData> CREATOR =
            new Parcelable.Creator<InterestRecalculationData>() {
                @Override
                public InterestRecalculationData createFromParcel(Parcel source) {
                    return new InterestRecalculationData(source);
                }

                @Override
                public InterestRecalculationData[] newArray(int size) {
                    return new InterestRecalculationData[size];
                }
            };
}