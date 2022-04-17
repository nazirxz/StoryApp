package com.nazirman.sub1storyapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nazirman.sub1storyapp.api.SharedViewModel
import com.nazirman.sub1storyapp.utils.UserPreference

class ViewModelFactory(private val pref: UserPreference) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel> create(modelClass: Class<T>) : T {
        return when {
            modelClass.isAssignableFrom(SharedViewModel::class.java) -> {
                SharedViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}