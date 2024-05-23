package com.example.shop

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("key/api/v1/text2image/run")
    suspend fun postData(@Body requestBody: Params): Any
}