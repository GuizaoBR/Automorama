package com.guizaotech.automorama.asyncTask

import android.os.AsyncTask
import com.guizaotech.automorama.database.RoomManutencaoDao
import com.guizaotech.automorama.modelo.Manutencao

class AdicionaManutencaoTask(private val manutencaoDAO: RoomManutencaoDao,
                             private val manutencao: Manutencao,
                             private val listener: SalvoListener) : AsyncTask<Any, Any, Any>() {
    
    override fun doInBackground(vararg params: Any?) {
        manutencaoDAO.salva(manutencao)    }

    override fun onPostExecute(result: Any?) {
        super.onPostExecute(result)
        listener.salvo()
    }

    interface SalvoListener{
        fun salvo()
    }
}