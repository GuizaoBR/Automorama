package com.guizaotech.automorama.asyncTask

import android.os.AsyncTask
import com.guizaotech.automorama.database.RoomVeiculoDao
import com.guizaotech.automorama.modelo.Veiculo

class DeletarVeiculoTask(private val daoVeiculo: RoomVeiculoDao,
                         private val veiculo: Veiculo,
                         private val listener: FinalizaListener): AsyncTask<Any, Any, Any>() {
    override fun doInBackground(vararg params: Any?) {
        daoVeiculo.remove(veiculo)
    }

    override fun onPostExecute(result: Any?) {
        super.onPostExecute(result)
        listener.finaliza()
    }

    interface FinalizaListener{
        fun finaliza()
    }
}