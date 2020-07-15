package com.srikanthpuram.tmtsampleapp.api

import com.srikanthpuram.tmtsampleapp.model.ApiResponse
import retrofit2.Response
import retrofit2.http.GET

interface ImageListAPI {

    @GET("test/home")
    suspend fun getImageList(): Response<ApiResponse>
}