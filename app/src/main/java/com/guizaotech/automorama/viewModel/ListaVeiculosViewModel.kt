package com.guizaotech.automorama.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.guizaotech.automorama.modelo.Resource
import com.guizaotech.automorama.modelo.Veiculo
import com.guizaotech.automorama.repository.VeiculosRepository

class ListaVeiculosViewModel(
    private val repository: VeiculosRepository
): ViewModel() {
    //val listaVeiculos: LiveData<List<Veiculo>?> = buscaTodos()

    fun buscaTodos() : LiveData<Resource<List<Veiculo>?>> {
        return repository.buscaVeiculos()
    }

}