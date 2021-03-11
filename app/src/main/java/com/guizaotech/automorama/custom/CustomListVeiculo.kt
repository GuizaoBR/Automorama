package com.guizaotech.automorama.custom

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.guizaotech.automorama.R
import com.guizaotech.automorama.helpers.CarregaImagem
import com.guizaotech.automorama.helpers.ViewHolderVeiculos
import com.guizaotech.automorama.modelo.Veiculo


class CustomListVeiculo(private val veiculos: List<Veiculo>, private val activity: Activity ) : BaseAdapter() {


    override fun getCount(): Int {
        return veiculos.size
    }

    override fun getItem(position: Int): Veiculo {
        return veiculos[position]
    }

    override fun getItemId(position: Int): Long {
        return veiculos[position].idVeiculo as Long
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val veiculo = getItem(position)
        val view : View
        val holder: ViewHolderVeiculos
        if (convertView == null){
            view = activity.layoutInflater.inflate(R.layout.item_lista_veiculo_personalizada, parent, false)
            holder = ViewHolderVeiculos(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolderVeiculos
        }

        holder.marca.text = veiculo.marca
        holder.modelo.text = activity.resources.getString(R.string.veiculo, veiculo.modelo, veiculo.anoFabricacao.toString(), veiculo.anoModelo.toString())
        holder.placa.text = veiculo.placa
        if (veiculo.apelido != ""){
            holder.apelido.text = veiculo.apelido
        } else {
            holder.apelido.visibility = View.INVISIBLE
        }
        if (veiculo.caminhoImagem != ""){
            val helperImagem = CarregaImagem()
            holder.caminhoImagem!!.setImageBitmap(helperImagem.giraImagem(veiculo.caminhoImagem, 300, 300))
        } else {
            holder.caminhoImagem!!.setImageResource(R.drawable.ic_car_list)
        }
        return view
    }
}