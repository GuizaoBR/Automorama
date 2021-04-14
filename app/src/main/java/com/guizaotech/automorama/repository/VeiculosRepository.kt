package com.guizaotech.automorama.repository

import androidx.lifecycle.*
import com.guizaotech.automorama.database.RoomVeiculoDao
import com.guizaotech.automorama.modelo.Resource
import com.guizaotech.automorama.modelo.Veiculo
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlin.Exception
import kotlinx.coroutines.flow.collect

class VeiculosRepository(private val dao: RoomVeiculoDao) {

    private val listaVeiculos = MutableLiveData<Resource<List<Veiculo>?>>()

    fun buscaVeiculos(): LiveData<Resource<List<Veiculo>?>> {
        runBlocking {
            val task = CoroutineScope(IO).launch {
                listaVeiculos.postValue(Resource(dado = dao.todos()))
            }
            task.join()
        }
        return listaVeiculos
    }

    suspend fun salva(veiculo: Veiculo): LiveData<Resource<Veiculo>> {
        val liveData = MutableLiveData<Resource<Veiculo>>()
        return try {
            salvaInterno(veiculo)
            liveData.value = Resource(dado = veiculo)
            liveData
        } catch (e: Exception) {
            liveData.value = Resource(dado = veiculo, erro = e.message)
            liveData
        }

    }

    suspend fun deleta(veiculo: Veiculo): LiveData<Resource<Void?>>{
        val liveData= MutableLiveData<Resource<Void?>>()
        return try {
            deletaInterno(veiculo)
            liveData.value = Resource(dado = null)
            liveData
        } catch (e: Exception) {
            liveData.value = Resource(dado = null, erro = e.message)
            liveData
        }
    }

    private suspend fun deletaInterno(veiculo: Veiculo) {
        var errorMessage: String = ""
        val coroutineScope = CoroutineScope(IO)
        val task = coroutineScope.launch {
            try {
                dao.remove(veiculo = veiculo)
            } catch (e: Exception) {
                errorMessage = e.message.toString()
                cancel()
            }
        }
        task.join()
        if (task.isCancelled) {
            throw Exception(errorMessage)
        }
    }

    suspend fun salvaInterno(veiculo: Veiculo) {
        val coroutineScope = CoroutineScope(IO)
        var errorMessage: String? = null
        val task = coroutineScope.launch {
            try {
                val placaExite =
                    dao.placaExiste(placa = veiculo.placa, idVeiculo = veiculo.idVeiculo)
                if (placaExite) {
                    throw Exception("Essa placa já esta cadastrada")
                }
                when {
                    veiculo.idVeiculo != 0L -> {
                        dao.altera(veiculo = veiculo)
                    }
                    else -> {
                        dao.salva(veiculo = veiculo)
                    }
                }
            } catch (e: Exception){
                errorMessage = e.message
                cancel()
            }
        }
        task.join()
        if(task.isCancelled){
            throw Exception(errorMessage)
        }
    }


}