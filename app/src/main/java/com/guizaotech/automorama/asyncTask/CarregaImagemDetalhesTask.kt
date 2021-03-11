package com.guizaotech.automorama.asyncTask

import android.graphics.Bitmap
import android.os.AsyncTask
import com.guizaotech.automorama.helpers.CarregaImagem
import com.guizaotech.automorama.modelo.Veiculo

class CarregaImagemDetalhesTask(private val veiculo: Veiculo,
                                private val listener: EncontradoListener) : AsyncTask<Any, Any, Bitmap?>(){
    override fun doInBackground(vararg params: Any?): Bitmap? {
        val helperImagem = CarregaImagem()
        //Thread.sleep(5000)
        return helperImagem.giraImagem(veiculo.caminhoImagem!!, 500, 500)
    }

    override fun onPostExecute(result: Bitmap?) {
        super.onPostExecute(result)
        listener.encontrado(result!!)
    }

    override fun onPreExecute() {
        super.onPreExecute()
        listener.carregando()
    }


    interface EncontradoListener{
        fun encontrado(imagem: Bitmap)
        fun carregando()
    }
}