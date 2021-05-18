package com.guizaotech.automorama.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.guizaotech.automorama.modelo.Consumo
import com.guizaotech.automorama.modelo.Resource
import com.guizaotech.automorama.modelo.Veiculo
import com.guizaotech.automorama.repository.ConsumoRepository
import com.guizaotech.automorama.repository.VeiculosRepository

class DetalhesConsumoViewModel(
    private val repository: ConsumoRepository
): ViewModel() {

    suspend fun deleta(consumo: Consumo): LiveData<Resource<Void?>> {
        return repository.deleta(consumo = consumo)
    }

    suspend fun consumoAnterior(idVeiculo: Long) : LiveData<Resource<Consumo?>> {
        return  repository.consumoAnterior(idVeiculo)
    }


}