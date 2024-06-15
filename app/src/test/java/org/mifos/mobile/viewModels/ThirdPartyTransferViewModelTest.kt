package org.mifos.mobile.viewModels

import CoroutineTestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.R
import org.mifos.mobile.models.AccountOptionAndBeneficiary
import org.mifos.mobile.models.beneficiary.Beneficiary
import org.mifos.mobile.models.payload.AccountDetail
import org.mifos.mobile.models.templates.account.AccountOption
import org.mifos.mobile.models.templates.account.AccountOptionsTemplate
import org.mifos.mobile.models.templates.account.AccountType
import org.mifos.mobile.repositories.BeneficiaryRepositoryImp
import org.mifos.mobile.repositories.ThirdPartyTransferRepositoryImp
import org.mifos.mobile.ui.third_party_transfer.ThirdPartyTransferViewModel
import org.mifos.mobile.util.RxSchedulersOverrideRule
import org.mifos.mobile.utils.ThirdPartyTransferUiState
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class ThirdPartyTransferViewModelTest {

    @JvmField
    @Rule
    val mOverrideSchedulersRule = RxSchedulersOverrideRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    lateinit var thirdPartyTransferRepositoryImp: ThirdPartyTransferRepositoryImp

    @Mock
    lateinit var beneficiaryRepositoryImp: BeneficiaryRepositoryImp

    private lateinit var thirdPartyTransferViewModel: ThirdPartyTransferViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        thirdPartyTransferViewModel =
            ThirdPartyTransferViewModel(
                thirdPartyTransferRepositoryImp,
                beneficiaryRepositoryImp,)

    }

    @Test
    fun testLoadTransferTemplate_Successful() = runBlocking {
        val templateResult = mock(AccountOptionsTemplate::class.java)
        val list1 = mock(Beneficiary::class.java)
        val list2 = mock(Beneficiary::class.java)
        val beneficiaryListResult = listOf(list1, list2)

        `when`(thirdPartyTransferRepositoryImp.thirdPartyTransferTemplate())
            .thenReturn(
            flowOf(
                templateResult
            )
        )
        `when`(beneficiaryRepositoryImp.beneficiaryList())
            .thenReturn(
            flowOf(
                beneficiaryListResult
            )
        )
        thirdPartyTransferViewModel.accountOptionAndBeneficiary =
            AccountOptionAndBeneficiary(
            templateResult,
            beneficiaryListResult
        )
        thirdPartyTransferViewModel.thirdPartyTransferUiState.test {
            thirdPartyTransferViewModel.loadTransferTemplate()
            assertEquals(ThirdPartyTransferUiState.Initial, awaitItem())
            assertEquals(ThirdPartyTransferUiState.Loading, awaitItem())
            assertEquals(ThirdPartyTransferUiState.ShowThirdPartyTransferTemplate(templateResult), awaitItem())
            assertEquals(ThirdPartyTransferUiState.ShowBeneficiaryList(beneficiaryListResult), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test(expected = Exception::class)
    fun testLoadTransferTemplate_Unsuccessful() = runBlocking {
        val errorMessage = R.string.error_fetching_third_party_transfer_template
        `when`(thirdPartyTransferRepositoryImp.thirdPartyTransferTemplate()).
        thenThrow(Exception("Error fetching third party transfer template"))
        `when`(thirdPartyTransferRepositoryImp.thirdPartyTransferTemplate())
            .thenThrow(Exception("Error fetching beneficiary list"))
        `when`(beneficiaryRepositoryImp.beneficiaryList())
            .thenThrow(Exception("Error fetching beneficiary list"))
        thirdPartyTransferViewModel.thirdPartyTransferUiState.test {
            thirdPartyTransferViewModel.loadTransferTemplate()
            assertEquals(ThirdPartyTransferUiState.Initial, awaitItem())
            assertEquals(ThirdPartyTransferUiState.Loading, awaitItem())
            assertEquals(ThirdPartyTransferUiState.Error(errorMessage), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testGetAccountNumbersFromAccountOptions() {
        val accountTypeLoanString = "loan"
        val accountOptions = listOf(
            AccountOption(
                accountId = 1,
                accountNo = "123456789",
                accountType = AccountType(1, "savings", "SAV"),
                clientId = 1001,
                clientName = "John Doe",
                officeId = 101,
                officeName = "Main Office"
            ),
            AccountOption(
                accountId = 2,
                accountNo = "987654321",
                accountType = AccountType(2, "current", "CUR"),
                clientId = 1002,
                clientName = "Jane Smith",
                officeId = 102,
                officeName = "Branch Office"
            )
        )

        val result = thirdPartyTransferViewModel.getAccountNumbersFromAccountOptions(
            accountOptions,
            accountTypeLoanString
        )

        assertEquals(AccountDetail("123456789", "SAV"), result[0])
        assertEquals(AccountDetail("987654321", "CUR"), result[1])
        assertNotEquals(AccountDetail("987654321", "CUR"), result[0])
        assertNotEquals(AccountDetail("123456789", "SAV"), result[1])
    }

    @Test
    fun testGetAccountNumbersFromBeneficiaries() {
        val beneficiaries = listOf(
            Beneficiary(
                1,
                "John Doe",
                "Main Office",
                "Client 1",
                AccountType(1, "savings", "SAV"),
                "123456789",
                1000.0
            ),
            Beneficiary(
                2,
                "Jane Smith",
                "Branch Office",
                "Client 2",
                AccountType(2, "current", "CUR"),
                "987654321",
                2000.0
            )
        )

        val result = thirdPartyTransferViewModel.getAccountNumbersFromBeneficiaries(beneficiaries)

        assertEquals("123456789", result[0].accountNumber)
        assertEquals("John Doe", result[0].beneficiaryName)
        assertEquals("987654321", result[1].accountNumber)
        assertEquals("Jane Smith", result[1].beneficiaryName)
        assertNotEquals("123456789", result[1].accountNumber)
        assertNotEquals("John Doe", result[1].beneficiaryName)
        assertNotEquals("987654321", result[0].accountNumber)
        assertNotEquals("Jane Smith", result[0].beneficiaryName)
    }

    @Test
    fun testSearchAccount() {
        val accountNoToSearch = "123456789"
        val accountOptions = listOf(
            AccountOption(1, "123456789", AccountType(1, "savings", "SAV")),
            AccountOption(2, "987654321", AccountType(2, "current", "CUR")),
            AccountOption(3, "111111111", AccountType(3, "loan", "LOAN")),
        )
        val result = thirdPartyTransferViewModel.searchAccount(accountOptions, accountNoToSearch)

        assertEquals(AccountOption(1, "123456789", AccountType(1, "savings", "SAV")), result)
        assertNotEquals(AccountOption(), result)
    }

    @After
    fun tearDown() {
    }
}