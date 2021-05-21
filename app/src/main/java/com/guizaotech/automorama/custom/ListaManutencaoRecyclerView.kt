package com.guizaotech.automorama.custom

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.guizaotech.automorama.R
import com.guizaotech.automorama.modelo.Consumo
import com.guizaotech.automorama.modelo.Manutencao
import java.math.RoundingMode
import java.text.DecimalFormat

class ListaManutencaoRecyclerView(
    private var listaManutecao: List<Manutencao>,
    private val context: Context
): RecyclerView.Adapter<ListaManutencaoRecyclerView.ConsumoViewHolder>() {

    var onItemClickListener : OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsumoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_lista_manutencao_personalizada, parent, false)
        return ConsumoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listaManutecao.size
    }

    override fun onBindViewHolder(holder: ConsumoViewHolder, position: Int) {
        val consumo = listaManutecao[position]
        holder.vincula(consumo, position)
    }




    inner class ConsumoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var titulo: TextView? =  itemView.findViewById(R.id.textTitulo)
        var data: TextView? = itemView.findViewById(R.id.textDataLista)
        var valor: TextView? = itemView.findViewById(R.id.textValor)
        var feito: ImageView? = itemView.findViewById(R.id.imageFeito)

        private var manutencao: Manutencao? = null

        init {
            itemView.setOnClickListener { onItemClickListener!!.onItemClick(manutencao as Manutencao, adapterPosition) }
        }

        fun getItemAnterior(position: Int): Manutencao{
            return  listaManutecao[position - 1]
        }

        fun vincula(manutencao: Manutencao, position: Int) {
            this.manutencao = manutencao

            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING
            valor!!.text = context.resources.getString(R.string.moeda) + df.format(manutencao.valor)


            data!!.text = manutencao.data.formatString("dd/MM/yyyy")

            titulo!!.text = manutencao.titulo

            if (manutencao.feito){
                feito!!.setImageResource(R.drawable.ic_tanque_cheio)
            } else {
                feito!!.setImageResource(R.drawable.ic_tanque_nao_cheio)
            }
        }

    }

}