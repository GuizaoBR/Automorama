package com.guizaotech.automorama.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.guizaotech.automorama.repository.ManutencaoRepository
import com.guizaotech.automorama.viewModel.ManutencaoViewModel

@Suppress("UNCHECKED_CAST")
class ManutencaoViewModelFactory(
    private val repository: ManutencaoRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ManutencaoViewModel(repository) as T
    }
}