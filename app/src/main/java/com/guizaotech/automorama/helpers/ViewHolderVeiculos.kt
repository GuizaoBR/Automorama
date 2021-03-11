package com.guizaotech.automorama.helpers

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.guizaotech.automorama.R

class ViewHolderVeiculos(view: View) {
    var marca: TextView
    var modelo: TextView
    var placa: TextView
    var apelido: TextView
    var caminhoImagem: ImageView?

    init {
        marca = view.findViewById(R.id.textMarcaLista)
        modelo = view.findViewById(R.id.textModeloLista)
        placa = view.findViewById(R.id.textPlacaLista)
        apelido = view.findViewById(R.id.textApelidoLista)
        caminhoImagem = view.findViewById(R.id.imageVeiculo)
    }
}