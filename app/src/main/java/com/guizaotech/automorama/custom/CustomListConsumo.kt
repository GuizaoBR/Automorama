package com.guizaotech.automorama.custom

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.guizaotech.automorama.R
import com.guizaotech.automorama.helpers.ViewHolderConsumo
import com.guizaotech.automorama.modelo.Consumo
import java.math.RoundingMode
import java.text.DecimalFormat

class CustomListConsumo(private val listaConsumo: List<Consumo>, private val activity: Activity): BaseAdapter() {

    override fun getCount(): Int {
        return listaConsumo.size
    }

    override fun getItem(position: Int): Consumo {
        return listaConsumo[position]
    }

    override fun getItemId(position: Int): Long {
        return listaConsumo[position].idConsumo as Long
    }

    fun getItemAnterior(position: Int): Consumo{
        return  listaConsumo[position - 1]
    }

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolderConsumo
        if (convertView == null) {
            view = activity.layoutInflater.inflate(R.layout.item_lista_consumo_personalizada, parent, false)
            holder = ViewHolderConsumo(view)
            view.tag = holder

        } else{
            view = convertView
            holder = view.tag as ViewHolderConsumo

        }
        val consumo = getItem(position)


        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING

        holder.consumoLitro!!.text = activity.applicationContext.getString(R.string.item_lista_consumo_consumo, df.format(consumo.consumoTotal))

        holder.combAbastecido!!.text = activity.applicationContext.getString(R.string.item_lista_consumo_abastecido)


       if (position > 0){
            holder.combPercorrido!!.text = activity.applicationContext.getString(R.string.item_lista_consumo_percorrido,getItemAnterior(position).combustivel)
            holder.combPercorrido!!.visibility = View.VISIBLE
        } else {
            holder.combPercorrido!!.visibility = View.INVISIBLE
        }

        if (consumo.tanqueCompleto){
            holder.tanqueCheio!!.setImageResource(R.drawable.ic_tanque_cheio)
        } else{
            holder.tanqueCheio!!.setImageResource(R.drawable.ic_tanque_nao_cheio)
        }

        return view
    }
}