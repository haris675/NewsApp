package com.app.rescuemanagmentsystem.network

import com.app.myapplication.model.ModelNews
import com.app.myapplication.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiServices {

    @GET(Constants.LATEST_NEWS)
    suspend fun getNew(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String
    ): Response<ModelNews>


}