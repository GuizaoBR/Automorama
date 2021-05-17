package com.guizaotech.automorama.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.guizaotech.automorama.modelo.Consumo
import com.guizaotech.automorama.modelo.Resource
import com.guizaotech.automorama.modelo.Veiculo
import com.guizaotech.automorama.repository.GastosRepository

class ConsumoViewModel(
    private val repository: GastosRepository
): ViewModel() {

    fun buscaTodosConsummo(idVeiculo: Long) : LiveData<Resource<List<Consumo>?>> {
        return repository.buscaConsumo(idVeiculo = idVeiculo)
    }
    suspend fun salvaConsumo(consumo: Consumo): LiveData<Resource<Consumo>> {
        return repository.salva(consumo = consumo)
    }
}