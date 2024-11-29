package com.example.ambrosianaapp.book.newbook

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NewBookViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewBookViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewBookViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}