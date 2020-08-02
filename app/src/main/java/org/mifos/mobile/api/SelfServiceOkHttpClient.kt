package org.mifos.mobile.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.TimeUnit

import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class SelfServiceOkHttpClient(private val tenant: String?, private val authToken: String?) {
    // Create a trust manager that does not validate certificate chains
    val mifosOkHttpClient: OkHttpClient
        get() {
            val builder = OkHttpClient.Builder()
            try {
                // Create a trust manager that does not validate certificate chains
                val trustAllCerts = arrayOf<TrustManager>(
                        object : X509TrustManager {
                            @Throws(CertificateException::class)
                            override fun checkClientTrusted(
                                    chain: Array<X509Certificate>,
                                    authType: String
                            ) {
                            }

                            @Throws(CertificateException::class)
                            override fun checkServerTrusted(
                                    chain: Array<X509Certificate>,
                                    authType: String
                            ) {
                            }

                            override fun getAcceptedIssuers(): Array<X509Certificate?> {
                                return arrayOfNulls(0)
                            }
                        }
                )

                // Install the all-trusting trust manager
                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, SecureRandom())
                // Create an ssl socket factory with our all-trusting manager
                val sslSocketFactory = sslContext.socketFactory

                //Enable Full Body Logging
                val logger = HttpLoggingInterceptor()
                logger.level = HttpLoggingInterceptor.Level.BODY
                val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                trustManagerFactory.init(null as KeyStore?)
                val trustManagers = trustManagerFactory.trustManagers
                check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) { "Unexpected default trust managers:" + Arrays.toString(trustManagers) }
                val trustManager = trustManagers[0] as X509TrustManager

                //Set SSL certificate to OkHttpClient Builder
                builder.sslSocketFactory(sslSocketFactory, trustManager)
                builder.hostnameVerifier { _, _ -> true }
            } catch (e: Exception) {
                throw RuntimeException(e)
            }

            //Enable Full Body Logging
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BODY

            //Setting Timeout 30 Seconds
            builder.connectTimeout(60, TimeUnit.SECONDS)
            builder.readTimeout(60, TimeUnit.SECONDS)

            //Interceptor :> Full Body Logger and ApiRequest Header
            builder.addInterceptor(logger)
            builder.addInterceptor(SelfServiceInterceptor(tenant, authToken))
            return builder.build()
        }
}