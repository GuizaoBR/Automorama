package com.guizaotech.automorama.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.guizaotech.automorama.modelo.Manutencao
import com.guizaotech.automorama.modelo.Resource
import com.guizaotech.automorama.repository.ManutencaoRepository
import java.lang.Exception

class ManutencaoViewModel(
    private val repository: ManutencaoRepository
): ViewModel() {

    var liveDataManutencao: MutableLiveData<Resource<List<Manutencao>?>> = MutableLiveData<Resource<List<Manutencao>?>>()

    fun buscaTodos(idVeiculo: Long) {
       try {
           val list = repository.buscaManutencaco(idVeiculo)
           liveDataManutencao.value = Resource(dado = list)
       }
       catch (e: Exception){
           liveDataManutencao.value = Resource(dado = null, erro = e.message)
       }
    }

}