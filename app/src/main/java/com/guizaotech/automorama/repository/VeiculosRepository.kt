package com.guizaotech.automorama.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.guizaotech.automorama.database.RoomVeiculoDao
import com.guizaotech.automorama.modelo.Veiculo

class VeiculosRepository (private  val dao: RoomVeiculoDao){
    private val mediador = MediatorLiveData<List<Veiculo>?>()

    fun buscaTodos(): LiveData<List<Veiculo>?>{
        mediador.addSource(buscaVeiculos()) { veiculos ->
            mediador.value = veiculos
        }
        return mediador
    }

    fun buscaVeiculos() : LiveData<List<Veiculo>>{
        return dao.todos()
    }

}