package com.srikanthpuram.tmtsampleapp.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.srikanthpuram.tmtsampleapp.repository.TmtRepository

class TmtViewModelProviderFactory(
    val app: Application,
    val tmtRepository: TmtRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return  TmtViewModel(app, tmtRepository) as T
    }

}