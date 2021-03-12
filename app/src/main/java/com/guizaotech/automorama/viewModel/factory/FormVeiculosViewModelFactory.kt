package com.guizaotech.automorama.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.guizaotech.automorama.repository.VeiculosRepository
import com.guizaotech.automorama.viewModel.FormVeiculosViewModel
import com.guizaotech.automorama.viewModel.ListaVeiculosViewModel

class FormVeiculosViewModelFactory(
    private val repository: VeiculosRepository
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FormVeiculosViewModel(repository) as T
    }

}