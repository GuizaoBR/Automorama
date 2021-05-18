package com.guizaotech.automorama.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.guizaotech.automorama.repository.ConsumoRepository
import com.guizaotech.automorama.viewModel.ConsumoViewModel

class ListaConsumoViewModelFactory(
    private val repository: ConsumoRepository
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ConsumoViewModel(repository) as T
    }
}