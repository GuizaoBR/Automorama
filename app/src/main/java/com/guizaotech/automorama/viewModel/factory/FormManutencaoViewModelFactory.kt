package com.guizaotech.automorama.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.guizaotech.automorama.repository.ManutencaoRepository
import com.guizaotech.automorama.viewModel.FormManutencaoViewModel

@Suppress("UNCHECKED_CAST")
class FormManutencaoViewModelFactory(
    private val repository: ManutencaoRepository
) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        FormManutencaoViewModel(repository) as T
}