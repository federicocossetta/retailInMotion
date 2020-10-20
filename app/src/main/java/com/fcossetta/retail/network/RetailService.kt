package com.fcossetta.retail.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface RetailService {
    @GET("xml/get.ashx")
    fun getForecast(
        @Query("action") action: String,
        @Query("stop") stop: String,
        @Query("encrypt") encrypt: Boolean,
    ): Call<ResponseBody>
}