package org.mifos.mobile.fineract.repository;

import android.database.Observable;

import org.mifos.mobile.models.Page;
import org.mifos.mobile.models.PartyIdentifiers;
import org.mifos.mobile.models.accounts.NewUser;
import org.mifos.mobile.models.accounts.savings.SavingsAccountUpdatePayload;
import org.mifos.mobile.models.accounts.savings.SavingsWithAssociations;
import org.mifos.mobile.models.beneficiary.BeneficiaryUpdatePayload;
import org.mifos.mobile.models.client.Client;
import org.mifos.mobile.models.client.ClientAccounts;
import org.mifos.mobile.models.register.RegisterPayload;
import org.mifos.mobile.models.register.UserVerify;
import org.mifos.mobile.utils.Constants;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;

@Singleton
public class FineractRepository {

    private final org.mifos.core.data.fineract.api.FineractApiManager fineractApiManager;
    @Inject
    public FineractRepository(org.mifos.mobile.core.data.fineract.api.FineractApiManager fineractApiManager) {
        this.fineractApiManager = fineractApiManager;
    }

    public Observable<CreateClient.ResponseValue> createClient(NewClient newClient) {
        return fineractApiManager.getClientsApi().createClient(newClient);
    }

    public Observable<CreateUser.ResponseValue> createUser(NewUser user) {
        return fineractApiManager.getUserApi().createUser(user);
    }

    public Observable<ResponseBody> registerUser(RegisterPayload registerPayload) {
        return fineractApiManager.getRegistrationAPi().registerUser(registerPayload);
    }

    public Observable<ResponseBody> verifyUser(UserVerify userVerify) {
        return fineractApiManager.getRegistrationAPi().verifyUser(userVerify);
    }

    public Observable<List<SearchedEntity>> searchResources(String query, String resources,
                                                            Boolean exactMatch) {
        return fineractApiManager.getSearchApi().searchResources(query, resources, exactMatch);
    }

    public Observable<ResponseBody> updateClient(long clientId, Object payload) {
        return fineractApiManager.getClientsApi().updateClient(clientId, payload)
                .map(new Func1<ResponseBody, ResponseBody>() {
                    @Override
                    public ResponseBody call(ResponseBody responseBody) {
                        return responseBody;
                    }
                });
    }

    public Observable<ClientAccounts> getAccounts(long clientId) {
        return fineractApiManager.getClientsApi().getAccounts(clientId, Constants.SAVINGS);
    }

    public Observable<Page<SavingsWithAssociations>> getSavingsAccounts() {
        return fineractApiManager.getSavingAccountsListApi().getSavingsAccounts(-1);
    }

    public Observable<Client> getClientDetails(long clientId) {
        return fineractApiManager.getClientsApi().getClientForId(clientId);
    }

    public Observable<ResponseBody> getClientImage(long clientId) {
        return fineractApiManager.getClientsApi().getClientImage(clientId);
    }

    public Observable<List<Card>> fetchSavedCards(long clientId) {
        return fineractApiManager.getSavedCardApi().getSavedCards((int) clientId);
    }

    public Observable<TransferDetail> getAccountTransfer(long transferId) {
        return fineractApiManager.getAccountTransfersApi().getAccountTransfer(transferId);
    }

    public Observable<List<NotificationPayload>> fetchNotifications(long clientId) {
        return fineractApiManager.getNotificationApi().fetchNotifications(clientId);
    }

    public Observable<List<DeliveryMethod>> getDeliveryMethods() {
        return fineractApiManager.getTwoFactorAuthApi().getDeliveryMethods();
    }

    public Observable<String> requestOTP(String deliveryMethod) {
        return fineractApiManager.getTwoFactorAuthApi().requestOTP(deliveryMethod);
    }

    public Observable<AccessToken> validateToken(String token) {
        return fineractApiManager.getTwoFactorAuthApi().validateToken(token);
    }

    public Observable<ResponseBody> getTransactionReceipt(String outputType,
                                                          String transactionId) {
        return fineractApiManager.getRunReportApi().getTransactionReceipt(outputType,
                transactionId);
    }

    public Observable<List<Invoice>> fetchInvoices(String clientId) {
        return fineractApiManager.getInvoiceApi().getInvoices(clientId);
    }

    public Observable<List<Invoice>> fetchInvoice(String clientId, String invoiceId) {
        return fineractApiManager.getInvoiceApi().getInvoice(clientId, invoiceId);
    }

    public Observable<ResponseBody> updateBeneficiary(long beneficiaryId,
                                                      BeneficiaryUpdatePayload payload) {
        return selfApiManager.getBeneficiaryApi().updateBeneficiary(beneficiaryId, payload);
    }
    public Observable<PartyIdentifiers> getSecondaryIdentifiers(String accountExternalId) {
        return fineractApiManager.getAccountTransfersApi()
                .fetchSecondaryIdentifiers(accountExternalId);
    }
}

