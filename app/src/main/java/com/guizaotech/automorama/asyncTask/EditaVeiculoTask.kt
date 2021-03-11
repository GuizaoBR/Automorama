package com.guizaotech.automorama.asyncTask

import android.os.AsyncTask
import com.guizaotech.automorama.database.RoomVeiculoDao
import com.guizaotech.automorama.modelo.Veiculo

class EditaVeiculoTask(private val dao: RoomVeiculoDao,
                       private val veiculo: Veiculo,
                       private val listener: QuandoFinalizado): AsyncTask<Any, Any, Any>() {

    override fun doInBackground(vararg params: Any?) {
        dao.altera(veiculo)
    }

    override fun onPostExecute(result: Any?) {
        super.onPostExecute(result)
        listener.finaliza()
    }

    interface QuandoFinalizado{
        fun finaliza()
    }
}