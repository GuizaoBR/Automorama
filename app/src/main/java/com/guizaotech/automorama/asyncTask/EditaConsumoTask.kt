package com.guizaotech.automorama.asyncTask

import android.os.AsyncTask
import com.guizaotech.automorama.database.RoomConsumoDao
import com.guizaotech.automorama.modelo.Consumo

class EditaConsumoTask(private val consumoDAO: RoomConsumoDao,
                       private val consumo: Consumo,
                       private val listener: FinalizaListener): AsyncTask<Any, Any, Any>() {
    override fun doInBackground(vararg params: Any?) {
        consumoDAO.altera(consumo)
    }

    override fun onPostExecute(result: Any?) {
        super.onPostExecute(result)
        listener.finaliza()
    }

    interface FinalizaListener{
        fun finaliza()
    }
}