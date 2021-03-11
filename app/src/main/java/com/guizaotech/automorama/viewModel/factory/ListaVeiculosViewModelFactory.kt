package com.guizaotech.automorama.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.guizaotech.automorama.repository.VeiculosRepository
import com.guizaotech.automorama.viewModel.ListaVeiculosViewModel

class ListaVeiculosViewModelFactory(
    private val repository: VeiculosRepository
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ListaVeiculosViewModel(repository) as T
    }
}