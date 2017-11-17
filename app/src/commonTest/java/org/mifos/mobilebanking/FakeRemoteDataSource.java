package org.mifos.mobilebanking;

import com.google.gson.reflect.TypeToken;

import org.mifos.mobilebanking.models.Page;
import org.mifos.mobilebanking.models.Transaction;
import org.mifos.mobilebanking.models.User;
import org.mifos.mobilebanking.models.accounts.loan.LoanAccount;
import org.mifos.mobilebanking.models.accounts.loan.LoanWithAssociations;
import org.mifos.mobilebanking.models.accounts.savings.SavingsWithAssociations;
import org.mifos.mobilebanking.models.beneficary.Beneficiary;
import org.mifos.mobilebanking.models.beneficary.BeneficiaryPayload;
import org.mifos.mobilebanking.models.beneficary.BeneficiaryUpdatePayload;
import org.mifos.mobilebanking.models.client.Client;
import org.mifos.mobilebanking.models.client.ClientAccounts;
import org.mifos.mobilebanking.models.payload.LoansPayload;
import org.mifos.mobilebanking.models.payload.TransferPayload;
import org.mifos.mobilebanking.models.templates.account.AccountOptionsTemplate;
import org.mifos.mobilebanking.models.templates.beneficiary.BeneficiaryTemplate;
import org.mifos.mobilebanking.models.templates.loans.LoanTemplate;

import java.util.List;

/**
 * Created by dilpreet on 26/6/17.
 */

public class FakeRemoteDataSource {

    private static TestDataFactory mTestDataFactory = new TestDataFactory();

    public static ClientAccounts getClientAccounts() {
        return mTestDataFactory.getListTypePojo(new TypeToken<ClientAccounts>() {
            }, FakeJsonName.CLIENT_ACCOUNTS_JSON);
    }

    public static ClientAccounts getClientSavingsAccount() {
        return mTestDataFactory.getObjectTypePojo(ClientAccounts.class,
                FakeJsonName.CLIENT_SAVINGS_ACCOUNT_JSON);
    }

    public static ClientAccounts getClientLoanAccount() {
        return mTestDataFactory.getObjectTypePojo(ClientAccounts.class,
                FakeJsonName.CLIENT_LOAN_ACCOUNT_JSON);
    }

    public static ClientAccounts getClientShareAccount() {
        return mTestDataFactory.getObjectTypePojo(ClientAccounts.class,
                FakeJsonName.CLIENT_SHARE_ACCOUNT_JSON);
    }

    public static List<Beneficiary> getBeneficiaries() {
        return mTestDataFactory.getListTypePojo(new TypeToken<List<Beneficiary>>() {
            }, FakeJsonName.BENEFICIARIES_JSON);
    }

    public static BeneficiaryTemplate getBeneficiaryTemplate() {
        return mTestDataFactory.getObjectTypePojo(BeneficiaryTemplate.class,
                FakeJsonName.BENEFICIARY_TEMPLATE_JSON);
    }

    public static BeneficiaryPayload beneficiaryPayload() {
        return mTestDataFactory.getObjectTypePojo(BeneficiaryPayload.class,
                FakeJsonName.BENEFICIARY_PAYLOAD_JSON);
    }

    public static BeneficiaryUpdatePayload beneficiaryUpdatePayload() {
        return mTestDataFactory.getObjectTypePojo(BeneficiaryUpdatePayload.class,
                FakeJsonName.BENEFICIARY_UPDATE_PAYLOAD_JSON);
    }

    public static LoanAccount getLoanAccount() {
        return mTestDataFactory.getObjectTypePojo(LoanAccount.class,
                FakeJsonName.LOAN_ACCOUNT_JSON);
    }

    public static LoanTemplate getLoanTemplate() {
        return mTestDataFactory.getObjectTypePojo(LoanTemplate.class,
                FakeJsonName.LOAN_TEMPLATE_JSON);
    }

    public static LoansPayload getLoansPayload() {
        return mTestDataFactory.getObjectTypePojo(LoansPayload.class,
                FakeJsonName.LOAN_PAYLOAD_JSON);
    }

    public static LoanTemplate getLoanTemplateByTemplate() {
        return mTestDataFactory.getObjectTypePojo(LoanTemplate.class,
                FakeJsonName.LOAN_TEMPLATE_BY_PRODUCT_JSON);
    }

    public static LoanWithAssociations getLoanAccountWithTransaction() {
        return mTestDataFactory.getObjectTypePojo(LoanWithAssociations.class,
                FakeJsonName.LOAN_ACCOUNT_WITH_TRANSACTIONS_JSON);
    }

    public static LoanWithAssociations getLoanAccountWithEmptyTransaction() {
        return mTestDataFactory.getObjectTypePojo(LoanWithAssociations.class,
                FakeJsonName.LOAN_ACCOUNT_WITH_EMPTY_TRANSACTIONS_JSON);
    }

    public static LoanWithAssociations getLoanAccountRepaymentSchedule() {
        return mTestDataFactory.getObjectTypePojo(LoanWithAssociations.class,
                FakeJsonName.LOAN_ACCOUNT_WITH_REPAYMENT_SCHEDULE_JSON);
    }
    public static LoanWithAssociations getLoanAccountEmptyRepaymentSchedule() {
        return mTestDataFactory.getObjectTypePojo(LoanWithAssociations.class,
                FakeJsonName.LOAN_ACCOUNT_WITH_EMPTY_REPAYMENT_SCHEDULE_JSON);
    }

    public static User getUser() {
        return mTestDataFactory.getObjectTypePojo(User.class,
                FakeJsonName.USER_JSON);
    }

    public static Page<Client> getClients() {
        return mTestDataFactory.getListTypePojo(new TypeToken<Page<Client>>() {
            }, FakeJsonName.CLIENTS_JSON);
    }

    public static Page<Client> getNoClients() {
        return mTestDataFactory.getListTypePojo(new TypeToken<Page<Client>>() {
        }, FakeJsonName.CLIENTS_NOT_FOUND_JSON);
    }

    public static Page<Transaction> getTransactions() {
        return mTestDataFactory.getListTypePojo(new TypeToken<Page<Transaction>>() {
        }, FakeJsonName.TRANSACTIONS_JSON);
    }

    public static SavingsWithAssociations getSavingsWithAssociations() {
        return mTestDataFactory.getObjectTypePojo(SavingsWithAssociations.class,
                FakeJsonName.SAVINGS_WITH_ASSOCIATIONS);
    }

    public static AccountOptionsTemplate getAccountOptionsTemplate() {
        return mTestDataFactory.getObjectTypePojo(AccountOptionsTemplate.class,
                FakeJsonName.ACCOUNT_OPTION_TEMPLATE);
    }

    public static TransferPayload getTransferPayload() {
        return mTestDataFactory.getObjectTypePojo(TransferPayload.class,
                FakeJsonName.TRANFER_PAYLOAD_JSON);
    }
}
