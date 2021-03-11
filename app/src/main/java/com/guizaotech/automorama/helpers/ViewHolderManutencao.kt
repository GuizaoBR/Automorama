package com.guizaotech.automorama.helpers

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.guizaotech.automorama.R

class ViewHolderManutencao(view: View) {
    var titulo: TextView? = null
    var data: TextView? = null
    var valor: TextView? = null
    var feito: ImageView? = null

    init {
        titulo = view.findViewById(R.id.textTitulo)
        data = view.findViewById(R.id.textDataLista)
        valor = view.findViewById(R.id.textValor)
        feito = view.findViewById(R.id.imageFeito)
    }
}
