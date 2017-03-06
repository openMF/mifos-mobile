package org.mifos.selfserviceapp.models.mifoserror;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class MifosError implements Parcelable {

    private String developerMessage;
    private String httpStatusCode;
    private String defaultUserMessage;
    private String userMessageGlobalisationCode;
    private List<Errors> errors = new ArrayList<>();

    public String getDeveloperMessage() {
        return developerMessage;
    }

    public void setDeveloperMessage(String developerMessage) {
        this.developerMessage = developerMessage;
    }

    public String getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(String httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String getDefaultUserMessage() {
        return defaultUserMessage;
    }

    public void setDefaultUserMessage(String defaultUserMessage) {
        this.defaultUserMessage = defaultUserMessage;
    }

    public String getUserMessageGlobalisationCode() {
        return userMessageGlobalisationCode;
    }

    public void setUserMessageGlobalisationCode(String userMessageGlobalisationCode) {
        this.userMessageGlobalisationCode = userMessageGlobalisationCode;
    }

    public List<Errors> getErrors() {
        return errors;
    }

    public void setErrors(List<Errors> errors) {
        this.errors = errors;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.developerMessage);
        dest.writeString(this.httpStatusCode);
        dest.writeString(this.defaultUserMessage);
        dest.writeString(this.userMessageGlobalisationCode);
        dest.writeTypedList(this.errors);
    }

    public MifosError() {
    }

    protected MifosError(Parcel in) {
        this.developerMessage = in.readString();
        this.httpStatusCode = in.readString();
        this.defaultUserMessage = in.readString();
        this.userMessageGlobalisationCode = in.readString();
        this.errors = in.createTypedArrayList(Errors.CREATOR);
    }

    public static final Creator<MifosError> CREATOR =
            new Creator<MifosError>() {
        @Override
        public MifosError createFromParcel(Parcel source) {
            return new MifosError(source);
        }

        @Override
        public MifosError[] newArray(int size) {
            return new MifosError[size];
        }
    };
}
