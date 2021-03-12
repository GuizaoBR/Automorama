package com.guizaotech.automorama.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.guizaotech.automorama.asyncTask.AdicionaVeiculoTask
import com.guizaotech.automorama.asyncTask.EditaVeiculoTask
import com.guizaotech.automorama.database.RoomVeiculoDao
import com.guizaotech.automorama.modelo.Resource
import com.guizaotech.automorama.modelo.Veiculo
import kotlin.Exception

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

    fun salva(veiculo: Veiculo): LiveData<Resource<Void?>> {
        val liveData = MutableLiveData<Resource<Void?>>()
//        if(placaVeiculoExiste(veiculo = veiculo)){
//            throw Exception("Já existe veículo com essa placa cadastrada!")
//        }
        try {
            when {
                veiculo.idVeiculo != 0L -> {
                        EditaVeiculoTask(dao, veiculo,
                            object : EditaVeiculoTask.QuandoFinalizado {
                                override fun finaliza() {
                                }
                            }).execute()
                    }
                    else -> {
//                        if (listaVeiculo!!.size > 0) {
//                            veiculoNovo.idVeiculo = listaVeiculo!!.last().idVeiculo + 1
//                        }
                        AdicionaVeiculoTask(dao, veiculo,
                            object : AdicionaVeiculoTask.FinalizadaListener {
                                override fun quandoFinalizada() {
                                }
                            }).execute()
                    }
            }
            liveData.value = Resource(dado = null)
        } catch (e: Exception) {
            liveData.value = Resource(dado = null, erro = e.message)
        } finally {
            return liveData
        }
    }

    private fun placaVeiculoExiste(veiculo: Veiculo): Boolean {
        var existe = false
        val listaVeiculoLiveData = buscaVeiculos()
        val listaVeiculo = listaVeiculoLiveData.value!!
        for (veiculoLista in listaVeiculo) {
            if (veiculoLista.placa == veiculo.placa && veiculoLista.idVeiculo != veiculo.idVeiculo) {
                existe = true
                break
            }
        }
        return existe
    }



}