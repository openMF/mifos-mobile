package org.mifos.mobile.data

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.mifos.mobile.api.BaseURL
import org.mifos.mobile.api.services.RegistrationService
import org.mifos.mobile.utils.Constants
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class PaymentHubApiManager constructor(context: Context) {
    private lateinit var transactionsApi: TransactionsService
    private lateinit var registrationApi: RegistrationService
    private lateinit var retrofit: Retrofit
    private lateinit var anonymousRetrofit: Retrofit
    private lateinit var anonymousService: AnonymousService

    init {
        createService(context)
        createAnonymousService()
    }

    fun init() {
        transactionsApi = createApi(TransactionsService::class.java)
        registrationApi = createApi(RegistrationService::class.java)
    }

    private fun initAnonymous() {
        anonymousService = anonymousRetrofit.create(AnonymousService::class.java)
    }

    private fun <T> createApi(clazz: Class<T>): T {
        return retrofit.create(clazz)
    }

    fun createService(context: Context) {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor(ApiInterceptor(Constants.TENANT_ID))
                .build()
        retrofit = Retrofit.Builder()
                .baseUrl(BaseURL.paymentUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
        init()
    }

    private fun createAnonymousService() {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

        anonymousRetrofit = Retrofit.Builder()
                .baseUrl(BaseURL.paymentUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
        initAnonymous()
    }

    fun getTransactionsApi(): TransactionsService {
        return transactionsApi
    }

    fun getRegistrationApi(): RegistrationService {
        return registrationApi
    }
}