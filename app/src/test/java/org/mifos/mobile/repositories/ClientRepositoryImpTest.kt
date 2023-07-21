package org.mifos.mobile.repositories

import okhttp3.Credentials
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.api.BaseURL
import org.mifos.mobile.api.local.PreferencesHelper
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ClientRepositoryImpTest {

    @Mock
    lateinit var preferencesHelper: PreferencesHelper

    private lateinit var clientRepositoryImp: ClientRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        clientRepositoryImp = ClientRepositoryImp(preferencesHelper)
    }

    @Test
    fun testUpdateAuthenticationToken() {
        val mockPassword = "testPassword"
        val mockUsername = "testUsername"
        Mockito.`when`(preferencesHelper.userName).thenReturn(mockUsername)

        Mockito.`when`(preferencesHelper.baseUrl)
            .thenReturn(BaseURL.PROTOCOL_HTTPS + BaseURL.API_ENDPOINT)

        clientRepositoryImp.updateAuthenticationToken(mockPassword)
        val authenticationToken = Credentials.basic(preferencesHelper.userName!!, mockPassword)

        Mockito.verify(preferencesHelper).saveToken(authenticationToken)
    }
}