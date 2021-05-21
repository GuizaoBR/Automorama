package com.guizaotech.automorama.repository

import androidx.lifecycle.*
import com.guizaotech.automorama.database.RoomManutencaoDao
import com.guizaotech.automorama.modelo.Manutencao
import com.guizaotech.automorama.modelo.Resource
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlin.Exception

class ManutencaoRepository(private val dao: RoomManutencaoDao) {

    private val liveDataManutencao = MutableLiveData<Resource<List<Manutencao>?>>()

    fun buscaManutencaco(idVeiculo: Long): List<Manutencao> {
        var listManutencao = listOf<Manutencao>()
        runBlocking {
            val task = CoroutineScope(IO).launch {
                listManutencao = dao.todos(idVeiculo)
            }
            task.join()
        }
        return listManutencao
    }

    suspend fun salva(manutencao: Manutencao): Resource<Manutencao> {
        return try {
            salvaInterno(manutencao)
            Resource(dado = manutencao)
        } catch (e: Exception) {
            Resource(dado = manutencao, erro = e.message)
        }

    }

    suspend fun deleta(manutencao: Manutencao): LiveData<Resource<Void?>>{
        val liveData= MutableLiveData<Resource<Void?>>()
        return try {
            deletaInterno(manutencao)
            liveData.value = Resource(dado = null)
            liveData
        } catch (e: Exception) {
            liveData.value = Resource(dado = null, erro = e.message)
            liveData
        }
    }

    private suspend fun deletaInterno(manutencao: Manutencao) {
        var errorMessage: String = ""
        val coroutineScope = CoroutineScope(IO)
        val task = coroutineScope.launch {
            try {
                dao.remove(manutencao = manutencao)
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

    private suspend fun salvaInterno(manutencao: Manutencao) {
        val coroutineScope = CoroutineScope(IO)
        var errorMessage: String? = null
        val task = coroutineScope.launch {
            try {
                when (manutencao.idManutencao){
                    null -> {
                        dao.salva(manutencao)
                    }
                    else -> {
                        dao.altera(manutencao)
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