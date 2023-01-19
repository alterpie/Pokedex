package com.assignment.catawiki.di.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras

abstract class GenericViewModelFactory<V : ViewModel> : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return createViewModel(extras.createSavedStateHandle()) as T
    }

    abstract fun createViewModel(handle: SavedStateHandle): V
}
