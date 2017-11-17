package org.mifos.mobilebanking.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dilpreet on 12/8/17.
 */

public class FAQ implements Parcelable {

    private String question;

    private String answer;

    private boolean selected;

    public FAQ(String question, String answer) {
        this.question = question;
        this.answer = answer;
        selected = false;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.question);
        dest.writeString(this.answer);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
    }

    protected FAQ(Parcel in) {
        this.question = in.readString();
        this.answer = in.readString();
        this.selected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<FAQ> CREATOR = new Parcelable.Creator<FAQ>() {
        @Override
        public FAQ createFromParcel(Parcel source) {
            return new FAQ(source);
        }

        @Override
        public FAQ[] newArray(int size) {
            return new FAQ[size];
        }
    };
}
