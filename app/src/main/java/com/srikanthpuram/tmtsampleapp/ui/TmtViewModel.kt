package com.srikanthpuram.tmtsampleapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.srikanthpuram.tmtsampleapp.TmtApplication
import com.srikanthpuram.tmtsampleapp.model.ApiResponse
import com.srikanthpuram.tmtsampleapp.repository.TmtRepository
import com.srikanthpuram.tmtsampleapp.util.Resource
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class TmtViewModel(
    app: Application,
    val tmtRepository: TmtRepository
) : AndroidViewModel(app) {
    val imageList: MutableLiveData<Resource<ApiResponse>> = MutableLiveData()
    companion object {
        var cachedResponse :  Response<ApiResponse>? =  null
    }

    // make the network request, when ViewModel is being initialised
    val TAG = "TmtViewModel"

    init {
        getImageList()
    }

    fun getImageList() = viewModelScope.launch{
        getImageListFromRepo()
    }

    /**
     * get the ImageList from the repository
     * Once response is received, Post the LiveData value, so that the observing classes,
     * interested in this data can receive the data and handle it
     */
    private suspend fun getImageListFromRepo() {

        //show loading progress bar, when initiating network request
        imageList.postValue(Resource.Loading())

        //make network call, only if there's a internet connection, else throw error
        try {
            if(hasInternetConnection()) {
                //initiate the network request via repository
                val response = tmtRepository.getImageListFromNetwork()
                cachedResponse = response
                //post the received response using the LiveData object,
                //this data is being observed in the HomeActivity
                imageList.postValue(handleResponse(response))
            } else if (cachedResponse != null) {   //Handle the offline scenario hear, use the cached response
                imageList.postValue(handleResponse(cachedResponse!!))
            }else {
                imageList.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when(t) {
                is IOException ->imageList.postValue(Resource.Error("Network Failure"))
                else -> imageList.postValue((Resource.Error("Conversion Error")))
            }
        }
    }

    /**
     * handle the ApiResponse received from the repository layer,
     * if success, pass the data to the adapter
     * if error, show the error dialog in the ui
     */
    private fun handleResponse(response: Response<ApiResponse>) : Resource<ApiResponse> {

        //if response is success, handle the success scenario, pass the data to the adapter
        if(response.isSuccessful) {
            response.body()?.let {   resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        //if error, show the error dialog in the UI
        return Resource.Error(response.message())
    }

    /**
     * utility function to check for network availability
     * return true if there's a internet connection else returns false
     */
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<TmtApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}