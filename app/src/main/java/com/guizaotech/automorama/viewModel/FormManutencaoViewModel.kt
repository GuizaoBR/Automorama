package com.guizaotech.automorama.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guizaotech.automorama.modelo.Manutencao
import com.guizaotech.automorama.modelo.Resource
import com.guizaotech.automorama.repository.ManutencaoRepository
import java.lang.Exception

class FormManutencaoViewModel(
    private val repository: ManutencaoRepository
) : ViewModel() {

    val resultadoLiveData = MutableLiveData<Resource<Manutencao>>()


    suspend fun salva(manutencao: Manutencao) {
        try {
            val resultado = repository.salva(manutencao)
            resultadoLiveData.value = resultado
        } catch (e: Exception) {
            resultadoLiveData.value = Resource(dado = null, erro = e.message )
        }
    }

}