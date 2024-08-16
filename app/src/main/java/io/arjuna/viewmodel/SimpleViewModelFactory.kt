package io.arjuna.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SimpleViewModelFactory<T : ViewModel>(
    private val supplier: () -> T
) : ViewModelProvider.Factory {

    override fun <K : ViewModel> create(modelClass: Class<K>): K {
        return supplier() as? K ?: error("${this::class} can not create view model of $modelClass")
    }
}