package com.guizaotech.automorama.asyncTask

import android.os.AsyncTask
import com.guizaotech.automorama.database.RoomManutencaoDao
import com.guizaotech.automorama.modelo.Manutencao

class EditaManutencaoTask(private val manutencaoDAO: RoomManutencaoDao,
                          private val manutencao: Manutencao,
                          private val listener: FinalizaListener): AsyncTask<Any, Any, Any>() {
    override fun doInBackground(vararg params: Any?) {
        manutencaoDAO.altera(manutencao)
    }

    override fun onPostExecute(result: Any?) {
        super.onPostExecute(result)
        listener.finaliza()
    }

    interface FinalizaListener{
        fun finaliza()
    }
}