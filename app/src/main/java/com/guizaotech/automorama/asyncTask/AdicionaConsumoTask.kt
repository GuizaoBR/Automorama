package com.guizaotech.automorama.asyncTask

import android.os.AsyncTask
import com.guizaotech.automorama.database.RoomConsumoDao
import com.guizaotech.automorama.modelo.Consumo

class AdicionaConsumoTask(private val consumoDAO: RoomConsumoDao,
                          private val consumo: Consumo,
                          private val listener: SalvoListener) : AsyncTask<Any, Any, Any>() {
    override fun doInBackground(vararg params: Any?) {
        consumoDAO.salva(consumo)    }

    override fun onPostExecute(result: Any?) {
        super.onPostExecute(result)
        listener.salvo()
    }

    interface SalvoListener{
        fun salvo()
    }
}