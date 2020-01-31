package com.packetalk.retrofit

import com.packetalk.util.AppConstants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object APIClientBasicAuth {

    private val BASE_URL = AppConstants.BASE_URL
    private var retrofit: Retrofit? = null
    val client: Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(BasicAuthInterceptor("Admin", "1234"))
        .connectTimeout(220, TimeUnit.SECONDS)
        .writeTimeout(220, TimeUnit.SECONDS)
        .readTimeout(220, TimeUnit.SECONDS)
        .build()

}
