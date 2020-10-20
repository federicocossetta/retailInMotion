package com.fcossetta.retail.network

import android.util.Log
import com.fcossetta.retail.utils.Constants
import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class NetworkManager {
    private val log: Logger = LoggerFactory.getLogger(NetworkManager::class.simpleName)

    private var okHttpClient: OkHttpClient? = null

    companion object {
        val mInstance = NetworkManager()
    }


    private fun getBuilder(): OkHttpClient.Builder {
        val builder = OkHttpClient.Builder()
        try {
            builder.followSslRedirects(true)
            builder.retryOnConnectionFailure(false)
            builder.connectionPool(ConnectionPool(0, 1, TimeUnit.NANOSECONDS))
        } catch (e: java.lang.Exception) {
            log.error(Log.getStackTraceString(e))
        }
        return builder
    }

    @Synchronized
    private fun getOkHttpClient(): OkHttpClient? {
        if (okHttpClient == null) {
            try {
                val builder: OkHttpClient.Builder = getBuilder()
                val dispatcher = Dispatcher()
                builder.dispatcher(dispatcher)
                okHttpClient = builder.build()
            } catch (ex: Exception) {
                log.error(Log.getStackTraceString(ex))
            }
        }
        return okHttpClient
    }

    public fun getService(): RetailService {
        val retrofit: Retrofit = Retrofit.Builder().client(getOkHttpClient()).baseUrl(Constants.BASE_URL)
            .build()
        return retrofit.create(RetailService::class.java)
    }
}