package com.guizaotech.automorama.asyncTask

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.guizaotech.automorama.database.RoomVeiculoDao
import com.guizaotech.automorama.modelo.Veiculo

class CarregaListaVeiculoTask(private val daoVeiculo : RoomVeiculoDao,
                              private val listener: EncontradoListener): AsyncTask<Any, Any, LiveData<List<Veiculo>>> () {


    override fun doInBackground(vararg params: Any?): LiveData<List<Veiculo>> {
        return daoVeiculo.todos()
    }

    override fun onPostExecute(result: LiveData<List<Veiculo>>?) {
        super.onPostExecute(result)
        listener.encontrado(result!!)
    }


    interface EncontradoListener{
        fun encontrado(lista: LiveData<List<Veiculo>>)
    }
}