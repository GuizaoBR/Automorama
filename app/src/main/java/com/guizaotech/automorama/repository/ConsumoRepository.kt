package com.guizaotech.automorama.repository

import androidx.lifecycle.*
import com.guizaotech.automorama.database.RoomConsumoDao
import com.guizaotech.automorama.modelo.Consumo
import com.guizaotech.automorama.modelo.Resource
import com.guizaotech.automorama.modelo.Veiculo
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.Exception

class ConsumoRepository(private val dao: RoomConsumoDao) {

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
                when (consumo.idConsumo){
                    0L -> {
                        dao.salva(consumo)
                    }
                    else -> {
                        dao.altera(consumo)
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

    suspend fun mediaConsumo(idVeiculo: Long): LiveData<Resource<String?>>{
        val liveData = MutableLiveData<Resource<String?>>()
        try {
            liveData.value = Resource(dado = calculaMedia(idVeiculo))

        } catch (e: Exception){
            liveData.value = Resource(dado = null, erro =  e.message)
        }

        return liveData
    }

    suspend fun calculaMedia(idVeiculo: Long): String{
        var mediaConsumoGeralRound = ""
        var errorMessage: String? = null

            val task = CoroutineScope(IO).launch {

                try {
                    val listaConsumoTanqueCheio = dao.listaConsumoTanqueCompleto(idVeiculo)
                    if (listaConsumoTanqueCheio.size > 2) {
                        var somaConsumoTanqueCheio = 0.0
                        var quantidadeConsumoTanqueCheio = 0.0
                        val df = DecimalFormat("#.##")
                        df.roundingMode = RoundingMode.CEILING
                        for (consumo in listaConsumoTanqueCheio) {
                            if (consumo.kmAnterior == 0) {
                                if (consumo.tanqueCompleto) {
                                    somaConsumoTanqueCheio += consumo.consumoTotal
                                    quantidadeConsumoTanqueCheio++
                                }
                            }
                        }
                        val mediaConsumoGeral = somaConsumoTanqueCheio / quantidadeConsumoTanqueCheio
                        mediaConsumoGeralRound = df.format(mediaConsumoGeral)
                    }
                } catch(e: Exception) {
                    errorMessage = e.message
                    cancel()
                }

            }
            task.join()

        if (errorMessage != null){
            throw Exception(errorMessage)
        }
        return mediaConsumoGeralRound


    }

    suspend fun consumoAnterior(idVeiculo: Long): LiveData<Resource<Consumo?>>{
        val liveData = MutableLiveData<Resource<Consumo?>>()
        try {
            liveData.value = Resource(dado = pegaUltimoConsumo(idVeiculo))

        } catch (e: Exception){
            liveData.value = Resource(dado = null, erro =  e.message)
        }

        return liveData
    }

    suspend fun pegaUltimoConsumo(idVeiculo: Long): Consumo?{
        var consumo: Consumo? = null
        var errorMessage: String? = null
        val task = CoroutineScope(IO).launch {
            try {
                consumo = dao.ultimoConsumo(idVeiculo)
            } catch(e: Exception) {
                cancel()
                errorMessage = e.message
            }

        }
        task.join()
        if (errorMessage != null){
            throw Exception(errorMessage)
        }
        return consumo
    }

}