package com.guizaotech.automorama.repository

import androidx.lifecycle.*
import com.guizaotech.automorama.database.RoomVeiculoDao
import com.guizaotech.automorama.modelo.Resource
import com.guizaotech.automorama.modelo.Veiculo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.Exception

class VeiculosRepository(private val dao: RoomVeiculoDao) {


    fun buscaVeiculos(): Flow<List<Veiculo>> {
        return dao.todos()
    }

    //   fun placaExiste(placa : String, idVeiculo: Long) : Boolean{
//       val  task = CoroutineScope(IO).launch {
//           existe = dao.placaExiste(placa = placa,  idVeiculo = idVeiculo)
//       }.start()
//
//       return task
//
//    }
    suspend fun salva(veiculo: Veiculo): LiveData<Resource<Void?>> {
        val liveData = MutableLiveData<Resource<Void?>>()
        return try {
            processa(veiculo)
            liveData.value = Resource(dado = null)
            liveData
        } catch (e: Exception) {
            liveData.value = Resource(dado = null, erro = e.message)
            liveData
        }


    }

    suspend fun processa(veiculo: Veiculo) {
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