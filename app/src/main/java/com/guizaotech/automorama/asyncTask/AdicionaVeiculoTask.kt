package com.guizaotech.automorama.asyncTask

import android.os.AsyncTask
import com.guizaotech.automorama.database.RoomVeiculoDao
import com.guizaotech.automorama.modelo.Veiculo

class AdicionaVeiculoTask(private val daoVeiculo: RoomVeiculoDao,
                          private val veiculo: Veiculo,
                          private val listener: FinalizadaListener): AsyncTask<Any, Any, Any>() {

    override fun doInBackground(vararg params: Any?) {
        daoVeiculo.salva(veiculo)
    }

    override fun onPostExecute(result: Any?) {
        super.onPostExecute(result)
        listener.quandoFinalizada()
    }

    interface FinalizadaListener {
        fun quandoFinalizada()
    }


}