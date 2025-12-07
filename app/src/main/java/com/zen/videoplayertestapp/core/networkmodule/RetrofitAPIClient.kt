package com.zen.videoplayertestapp.core.networkmodule

import com.zen.videoplayertestapp.data.remote.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitAPIClient {
    private var retrofitAPIClient: ApiService? = null
    private var BASE_URL = "https://dev-json-api.brochill.app/v1/data/"

    fun getInstance(): ApiService? {
        if (retrofitAPIClient == null) {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofitAPIClient = retrofit.create(ApiService::class.java)
        }
        return retrofitAPIClient
    }
}
