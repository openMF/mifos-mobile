package org.mifos.mobilebanking.models.templates.loans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajan Maurya on 16/07/16.
 */
public class LoanCollateralOptions implements Parcelable {

    @SerializedName("id")
    Integer id;

    @SerializedName("name")
    String name;

    @SerializedName("position")
    Integer position;

    @SerializedName("description")
    String description;

    @SerializedName("isActive")
    Boolean isActive;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "LoanCollateralOptions{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", position=" + position +
                ", description='" + description + '\'' +
                ", isActive=" + isActive +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeValue(this.position);
        dest.writeString(this.description);
        dest.writeValue(this.isActive);
    }

    public LoanCollateralOptions() {
    }

    protected LoanCollateralOptions(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.position = (Integer) in.readValue(Integer.class.getClassLoader());
        this.description = in.readString();
        this.isActive = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Creator<LoanCollateralOptions> CREATOR =
            new Creator<LoanCollateralOptions>() {
        @Override
        public LoanCollateralOptions createFromParcel(Parcel source) {
            return new LoanCollateralOptions(source);
        }

        @Override
        public LoanCollateralOptions[] newArray(int size) {
            return new LoanCollateralOptions[size];
        }
    };
}
