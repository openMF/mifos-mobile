package org.mifos.selfserviceapp.models.client;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.mifos.selfserviceapp.api.ApiEndPoints;

public class DepositType implements Parcelable {

    @SerializedName("id")
    Integer id;

    @SerializedName("code")
    String code;

    @SerializedName("value")
    String value;

    public Integer getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public boolean isRecurring() {
        return ServerTypes.RECURRING.getId().equals(this.getId());
    }

    public String getEndpoint() {
        return ServerTypes.fromId(getId()).getEndpoint();
    }

    public ServerTypes getServerType() {
        return ServerTypes.fromId(getId());
    }

    @Override
    public String toString() {
        return "DepositType{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public DepositType() {
    }

    private DepositType(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.code = in.readString();
        this.value = in.readString();
    }

    public static final Parcelable.Creator<DepositType> CREATOR = new Parcelable
            .Creator<DepositType>() {
        public DepositType createFromParcel(Parcel source) {
            return new DepositType(source);
        }

        public DepositType[] newArray(int size) {
            return new DepositType[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.code);
        dest.writeString(this.value);
    }

    private enum ServerTypes {
        SAVINGS(100, "depositAccountType.savingsDeposit", ApiEndPoints.SAVINGS_ACCOUNTS),
        FIXED(200, "depositAccountType.fixedDeposit", ApiEndPoints.SAVINGS_ACCOUNTS),
        RECURRING(300, "depositAccountType.recurringDeposit", ApiEndPoints.RECURRING_ACCOUNTS);

        private Integer id;
        private String code;
        private String endpoint;

        ServerTypes(Integer id, String code, String endpoint) {
            this.id = id;
            this.code = code;
            this.endpoint = endpoint;
        }

        public static ServerTypes fromId(int id) {
            for (ServerTypes type : ServerTypes.values()) {
                if (type.getId().equals(id)) {
                    return type;
                }
            }
            return SAVINGS;
        }

        public Integer getId() {
            return id;
        }

        public String getCode() {
            return code;
        }

        public String getEndpoint() {
            return endpoint;
        }
    }
}