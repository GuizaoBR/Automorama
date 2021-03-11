package com.guizaotech.automorama.asyncTask

import android.os.AsyncTask
import com.guizaotech.automorama.database.RoomConsumoDao
import com.guizaotech.automorama.modelo.Consumo

class CarregaListaConsumoTask(private val consumoDao: RoomConsumoDao,
                              private val idVeiculo: Long,
                              private val listener: CarregadoListener): AsyncTask<Any, Any, MutableList<Consumo>>() {

    override fun doInBackground(vararg params: Any?): MutableList<Consumo> {
        return consumoDao.todos(idVeiculo)
    }

    override fun onPostExecute(result: MutableList<Consumo>?) {
        super.onPostExecute(result)
        listener.carregado(result!!)
    }

    interface CarregadoListener{
        fun carregado(lista: MutableList<Consumo>)
    }
}