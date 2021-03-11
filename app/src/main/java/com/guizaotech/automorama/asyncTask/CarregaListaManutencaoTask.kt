package com.guizaotech.automorama.asyncTask

import android.os.AsyncTask
import com.guizaotech.automorama.database.RoomManutencaoDao
import com.guizaotech.automorama.modelo.Manutencao

class CarregaListaManutencaoTask(private val manutencaoDao: RoomManutencaoDao,
                                 private val idVeiculo: Long,
                                 private val listener: CarregadoListener): AsyncTask<Any, Any, MutableList<Manutencao>>() {

    override fun doInBackground(vararg params: Any?): MutableList<Manutencao> {
        return manutencaoDao.todos(idVeiculo)
    }

    override fun onPostExecute(result: MutableList<Manutencao>?) {
        super.onPostExecute(result)
        listener.carregado(result!!)
    }

    interface CarregadoListener{
        fun carregado(lista: MutableList<Manutencao>)
    }
}