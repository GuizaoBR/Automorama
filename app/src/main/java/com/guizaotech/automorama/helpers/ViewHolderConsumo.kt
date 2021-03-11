package com.guizaotech.automorama.helpers

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.guizaotech.automorama.R

class ViewHolderConsumo(view: View) {
    var consumoLitro: TextView? = null
    var data: TextView? = null
    var combPercorrido: TextView? = null
    var combAbastecido: TextView? = null
    var tanqueCheio: ImageView? = null

    init {
        consumoLitro = view.findViewById(R.id.textConsumoLista)
        data = view.findViewById(R.id.textDataLista)
        combPercorrido = view.findViewById(R.id.textPercorridoLista)
        combAbastecido = view.findViewById(R.id.textAbastecidoLista)
        tanqueCheio = view.findViewById(R.id.imageTanqueCheio)
    }
}
