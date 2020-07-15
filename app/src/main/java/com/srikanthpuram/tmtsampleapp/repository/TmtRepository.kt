package com.srikanthpuram.tmtsampleapp.repository

import com.srikanthpuram.tmtsampleapp.api.RetrofitInstance

class TmtRepository {

    suspend fun getImageListFromNetwork() = RetrofitInstance.api.getImageList()
}