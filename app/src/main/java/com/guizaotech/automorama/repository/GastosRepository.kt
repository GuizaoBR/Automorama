package com.guizaotech.automorama.repository

import androidx.lifecycle.*
import com.guizaotech.automorama.database.RoomConsumoDao
import com.guizaotech.automorama.modelo.Consumo
import com.guizaotech.automorama.modelo.Resource
import com.guizaotech.automorama.modelo.Veiculo
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlin.Exception

class GastosRepository(private val dao: RoomConsumoDao) {

    private val listaConsumo = MutableLiveData<Resource<List<Consumo>?>>()

    fun buscaConsumo(idVeiculo: Long): LiveData<Resource<List<Consumo>?>> {
        runBlocking {
            val task = CoroutineScope(IO).launch {
                listaConsumo.postValue(Resource(dado = dao.todos(idVeiculo = idVeiculo)))
            }
            task.join()
        }
        return listaConsumo
    }

    suspend fun salva(consumo: Consumo): LiveData<Resource<Consumo>> {
        val liveData = MutableLiveData<Resource<Consumo>>()
        return try {
            salvaInterno(consumo)
            liveData.value = Resource(dado = consumo)
            liveData
        } catch (e: Exception) {
            liveData.value = Resource(dado = consumo, erro = e.message)
            liveData
        }

    }

    suspend fun deleta(consumo: Consumo): LiveData<Resource<Void?>>{
        val liveData= MutableLiveData<Resource<Void?>>()
        return try {
            deletaInterno(consumo)
            liveData.value = Resource(dado = null)
            liveData
        } catch (e: Exception) {
            liveData.value = Resource(dado = null, erro = e.message)
            liveData
        }
    }

    private suspend fun deletaInterno(consumo: Consumo) {
        var errorMessage: String = ""
        val coroutineScope = CoroutineScope(IO)
        val task = coroutineScope.launch {
            try {
                dao.remove(consumo = consumo)
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

    suspend fun salvaInterno(consumo: Consumo) {
        val coroutineScope = CoroutineScope(IO)
        var errorMessage: String? = null
        val task = coroutineScope.launch {
            try {
                dao.salva(consumo)
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