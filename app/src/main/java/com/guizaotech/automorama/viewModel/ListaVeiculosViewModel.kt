package com.guizaotech.automorama.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.guizaotech.automorama.modelo.Veiculo
import com.guizaotech.automorama.repository.VeiculosRepository

class ListaVeiculosViewModel(
    private val repository: VeiculosRepository
): ViewModel() {
    val listaVeiculos: LiveData<List<Veiculo>?> = buscaTodos()

    private fun buscaTodos() : LiveData<List<Veiculo>?> {
        return repository.buscaVeiculos().asLiveData()
    }

}