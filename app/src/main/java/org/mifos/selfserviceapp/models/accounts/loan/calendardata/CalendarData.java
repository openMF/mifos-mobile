package org.mifos.selfserviceapp.models.accounts.loan.calendardata;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rajan Maurya on 04/03/17.
 */

public class CalendarData implements Parcelable {

    @SerializedName("id")
    Integer id;

    @SerializedName("calendarInstanceId")
    Integer calendarInstanceId;

    @SerializedName("entityId")
    Integer entityId;

    @SerializedName("entityType")
    EntityType entityType;

    @SerializedName("title")
    String title;

    @SerializedName("startDate")
    List<Integer> startDate = new ArrayList<>();

    @SerializedName("endDate")
    List<Integer> endDate = new ArrayList<>();

    @SerializedName("duration")
    Double duration;

    @SerializedName("type")
    Type type;

    @SerializedName("repeating")
    Boolean repeating;

    @SerializedName("recurrence")
    String recurrence;

    @SerializedName("frequency")
    Frequency frequency;

    @SerializedName("interval")
    Double interval;

    @SerializedName("repeatsOnNthDayOfMonth")
    RepeatsOnNthDayOfMonth repeatsOnNthDayOfMonth;

    @SerializedName("firstReminder")
    Integer firstReminder;

    @SerializedName("secondReminder")
    Integer secondReminder;

    @SerializedName("humanReadable")
    String humanReadable;

    @SerializedName("createdDate")
    List<Integer> createdDate = new ArrayList<>();

    @SerializedName("lastUpdatedDate")
    List<Integer> lastUpdatedDate = new ArrayList<>();

    @SerializedName("createdByUserId")
    Integer createdByUserId;

    @SerializedName("createdByUsername")
    String createdByUsername;

    @SerializedName("lastUpdatedByUserId")
    Integer lastUpdatedByUserId;

    @SerializedName("lastUpdatedByUsername")
    String lastUpdatedByUsername;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCalendarInstanceId() {
        return calendarInstanceId;
    }

    public void setCalendarInstanceId(Integer calendarInstanceId) {
        this.calendarInstanceId = calendarInstanceId;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(
            EntityType entityType) {
        this.entityType = entityType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Integer> getStartDate() {
        return startDate;
    }

    public void setStartDate(List<Integer> startDate) {
        this.startDate = startDate;
    }

    public List<Integer> getEndDate() {
        return endDate;
    }

    public void setEndDate(List<Integer> endDate) {
        this.endDate = endDate;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Boolean getRepeating() {
        return repeating;
    }

    public void setRepeating(Boolean repeating) {
        this.repeating = repeating;
    }

    public String getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(String recurrence) {
        this.recurrence = recurrence;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public Double getInterval() {
        return interval;
    }

    public void setInterval(Double interval) {
        this.interval = interval;
    }

    public RepeatsOnNthDayOfMonth getRepeatsOnNthDayOfMonth() {
        return repeatsOnNthDayOfMonth;
    }

    public void setRepeatsOnNthDayOfMonth(
            RepeatsOnNthDayOfMonth repeatsOnNthDayOfMonth) {
        this.repeatsOnNthDayOfMonth = repeatsOnNthDayOfMonth;
    }

    public Integer getFirstReminder() {
        return firstReminder;
    }

    public void setFirstReminder(Integer firstReminder) {
        this.firstReminder = firstReminder;
    }

    public Integer getSecondReminder() {
        return secondReminder;
    }

    public void setSecondReminder(Integer secondReminder) {
        this.secondReminder = secondReminder;
    }

    public String getHumanReadable() {
        return humanReadable;
    }

    public void setHumanReadable(String humanReadable) {
        this.humanReadable = humanReadable;
    }

    public List<Integer> getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(List<Integer> createdDate) {
        this.createdDate = createdDate;
    }

    public List<Integer> getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(List<Integer> lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Integer getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(Integer createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public String getCreatedByUsername() {
        return createdByUsername;
    }

    public void setCreatedByUsername(String createdByUsername) {
        this.createdByUsername = createdByUsername;
    }

    public Integer getLastUpdatedByUserId() {
        return lastUpdatedByUserId;
    }

    public void setLastUpdatedByUserId(Integer lastUpdatedByUserId) {
        this.lastUpdatedByUserId = lastUpdatedByUserId;
    }

    public String getLastUpdatedByUsername() {
        return lastUpdatedByUsername;
    }

    public void setLastUpdatedByUsername(String lastUpdatedByUsername) {
        this.lastUpdatedByUsername = lastUpdatedByUsername;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.calendarInstanceId);
        dest.writeValue(this.entityId);
        dest.writeParcelable(this.entityType, flags);
        dest.writeString(this.title);
        dest.writeList(this.startDate);
        dest.writeList(this.endDate);
        dest.writeValue(this.duration);
        dest.writeParcelable(this.type, flags);
        dest.writeValue(this.repeating);
        dest.writeString(this.recurrence);
        dest.writeParcelable(this.frequency, flags);
        dest.writeValue(this.interval);
        dest.writeParcelable(this.repeatsOnNthDayOfMonth, flags);
        dest.writeValue(this.firstReminder);
        dest.writeValue(this.secondReminder);
        dest.writeString(this.humanReadable);
        dest.writeList(this.createdDate);
        dest.writeList(this.lastUpdatedDate);
        dest.writeValue(this.createdByUserId);
        dest.writeString(this.createdByUsername);
        dest.writeValue(this.lastUpdatedByUserId);
        dest.writeString(this.lastUpdatedByUsername);
    }

    public CalendarData() {
    }

    protected CalendarData(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.calendarInstanceId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.entityId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.entityType = in.readParcelable(EntityType.class.getClassLoader());
        this.title = in.readString();
        this.startDate = new ArrayList<Integer>();
        in.readList(this.startDate, Integer.class.getClassLoader());
        this.endDate = new ArrayList<Integer>();
        in.readList(this.endDate, Integer.class.getClassLoader());
        this.duration = (Double) in.readValue(Double.class.getClassLoader());
        this.type = in.readParcelable(Type.class.getClassLoader());
        this.repeating = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.recurrence = in.readString();
        this.frequency = in.readParcelable(Frequency.class.getClassLoader());
        this.interval = (Double) in.readValue(Double.class.getClassLoader());
        this.repeatsOnNthDayOfMonth = in.readParcelable(
                RepeatsOnNthDayOfMonth.class.getClassLoader());
        this.firstReminder = (Integer) in.readValue(Integer.class.getClassLoader());
        this.secondReminder = (Integer) in.readValue(Integer.class.getClassLoader());
        this.humanReadable = in.readString();
        this.createdDate = new ArrayList<Integer>();
        in.readList(this.createdDate, Integer.class.getClassLoader());
        this.lastUpdatedDate = new ArrayList<Integer>();
        in.readList(this.lastUpdatedDate, Integer.class.getClassLoader());
        this.createdByUserId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.createdByUsername = in.readString();
        this.lastUpdatedByUserId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.lastUpdatedByUsername = in.readString();
    }

    public static final Parcelable.Creator<CalendarData> CREATOR =
            new Parcelable.Creator<CalendarData>() {
                @Override
                public CalendarData createFromParcel(Parcel source) {
                    return new CalendarData(source);
                }

                @Override
                public CalendarData[] newArray(int size) {
                    return new CalendarData[size];
                }
            };
}