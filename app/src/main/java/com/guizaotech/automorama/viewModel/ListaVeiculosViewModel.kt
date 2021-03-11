package com.guizaotech.automorama.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.guizaotech.automorama.modelo.Veiculo
import com.guizaotech.automorama.repository.VeiculosRepository

class ListaVeiculosViewModel(
    private val repository: VeiculosRepository
): ViewModel() {

    fun buscaTodos() : LiveData<List<Veiculo>?> {
        return repository.buscaTodos()
    }

}