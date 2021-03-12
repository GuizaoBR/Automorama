package com.guizaotech.automorama.viewModel

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.guizaotech.automorama.R
import com.guizaotech.automorama.asyncTask.AdicionaVeiculoTask
import com.guizaotech.automorama.asyncTask.EditaVeiculoTask
import com.guizaotech.automorama.modelo.Resource
import com.guizaotech.automorama.modelo.Veiculo
import com.guizaotech.automorama.repository.VeiculosRepository
import kotlinx.android.synthetic.main.activity_form_veiculo.*

class FormVeiculosViewModel(
    private val repository: VeiculosRepository
): ViewModel() {

    fun salva(veiculo: Veiculo): LiveData<Resource<Void?>> {
        return repository.salva(veiculo = veiculo)
    }


}