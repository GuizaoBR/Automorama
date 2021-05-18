package com.guizaotech.automorama.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.guizaotech.automorama.modelo.Resource
import com.guizaotech.automorama.modelo.Veiculo
import com.guizaotech.automorama.repository.VeiculosRepository

class DetalhesVeiculosViewModel(
    private val repository: VeiculosRepository
): ViewModel() {

    suspend fun deleta(veiculo: Veiculo): LiveData<Resource<Void?>> {
        return repository.deleta(veiculo = veiculo)
    }


}