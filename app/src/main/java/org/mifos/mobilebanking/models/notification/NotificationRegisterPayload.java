package org.mifos.mobilebanking.models.notification;

/**
 * Created by dilpreet on 26/09/17.
 */

public class NotificationRegisterPayload {

    long clientId;

    String registrationId;

    public NotificationRegisterPayload(long clientId, String registrationId) {
        this.clientId = clientId;
        this.registrationId = registrationId;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }
}
