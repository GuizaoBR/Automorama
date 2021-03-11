package com.guizaotech.automorama.asyncTask

import android.os.AsyncTask
import com.guizaotech.automorama.database.RoomConsumoDao
import com.guizaotech.automorama.modelo.Veiculo
import java.math.RoundingMode
import java.text.DecimalFormat

class mediaConsumoTask(
    private val consumoDAO: RoomConsumoDao,
    private val veiculo: Veiculo,
    private val listener: EncontradoListener
) : AsyncTask<Any, Any, String>() {

    override fun doInBackground(vararg params: Any?): String {
        val listaConsumoTanqueCheio = consumoDAO.listaConsumoTanqueCompleto(veiculo.idVeiculo)
        var mediaConsumoGeralRound = ""

        if (listaConsumoTanqueCheio.size > 2) {
            var somaConsumoTanqueCheio = 0.0
            var quantidadeConsumoTanqueCheio = 0.0
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING
            for (consumo in listaConsumoTanqueCheio) {
                if (consumo != listaConsumoTanqueCheio[0]) {
                    if (consumo.tanqueCompleto == 1) {
                        somaConsumoTanqueCheio += consumo.consumoTotal!!.toDouble()
                        quantidadeConsumoTanqueCheio++
                    }
                }
            }
            val mediaConsumoGeral = somaConsumoTanqueCheio / quantidadeConsumoTanqueCheio
            mediaConsumoGeralRound = df.format(mediaConsumoGeral)
        }
        return mediaConsumoGeralRound
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        listener.encontrado(result!!)

    }

    override fun onPreExecute() {
        super.onPreExecute()
        listener.carregando()
    }
    interface EncontradoListener {
        fun encontrado(media: String)
        fun carregando()
    }


}