package io.arjuna.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SimpleViewModelFactory<T : ViewModel>(
    private val supplier: () -> T
) : ViewModelProvider.Factory {

    override fun <K : ViewModel> create(modelClass: Class<K>): K {
        val viewModel = supplier.invoke()
        if (viewModel::class.java == modelClass) {
            return viewModel as K
        } else {
            error("${this::class} can not create view model of $modelClass")
        }
    }
}