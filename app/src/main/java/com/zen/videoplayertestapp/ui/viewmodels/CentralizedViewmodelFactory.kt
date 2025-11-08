package com.zen.videoplayertestapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zen.videoplayertestapp.data.repo.Repository

class CentralizedViewmodelFactory(
    private val minisRepo: Repository // Takes the dependency
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Checks if the requested class is MinisHomeViewModel
        if (modelClass.isAssignableFrom(CentralizeViewmodel::class.java)) {
            // Returns a new instance, injecting the dependency
            return CentralizeViewmodel(minisRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}