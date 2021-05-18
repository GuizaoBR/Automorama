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
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat

class ListaConsumoRecyclerView(
    private var listaConsumo: MutableList<Consumo> = mutableListOf(),
    private val context: Context
): RecyclerView.Adapter<ListaConsumoRecyclerView.ConsumoViewHolder>() {

    var onItemClickListener : OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsumoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_lista_consumo_personalizada, parent, false)
        return ConsumoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listaConsumo.size
    }

    override fun onBindViewHolder(holder: ConsumoViewHolder, position: Int) {
        val consumo = listaConsumo[position]
        holder.vincula(consumo, position)
    }

    fun adiciona(consumo: Consumo) {
        listaConsumo.add(consumo)
        notifyDataSetChanged()
    }

    fun remove(posicao: Int){
        //configuraListaVeiculos.remove(veiculo)
        listaConsumo.removeAt(posicao)
        notifyDataSetChanged()

    }

    fun altera(consumo: Consumo, posicao: Int){
        listaConsumo[posicao] = consumo
        notifyDataSetChanged()

    }

    fun atualiza(consumos: List<Consumo>) {
        notifyItemRangeRemoved(0, this.listaConsumo.size)
        this.listaConsumo.clear()
        this.listaConsumo.addAll(consumos)
        notifyItemRangeInserted(0, this.listaConsumo.size)
    }

    inner class ConsumoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){


        var consumoLitro: TextView? = itemView.findViewById(R.id.textConsumoLista)
        var data: TextView? = itemView.findViewById(R.id.textDataLista)
        var combPercorrido: TextView? = itemView.findViewById(R.id.textPercorridoLista)
        var combAbastecido: TextView? = itemView.findViewById(R.id.textAbastecidoLista)
        var tanqueCheio: ImageView? = itemView.findViewById(R.id.imageTanqueCheio)

        private var consumo: Consumo? = null

        init {
            itemView.setOnClickListener { onItemClickListener!!.onItemClick(consumo as Consumo, adapterPosition) }
        }

        fun getItemAnterior(position: Int): Consumo{
            return  listaConsumo[position - 1]
        }

        fun vincula(consumo: Consumo, position: Int) {
            this.consumo = consumo

            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING
            consumoLitro!!.text = context.resources.getString(R.string.item_lista_consumo_consumo, df.format(consumo.consumoTotal))


            val myFormat = "dd/MM/yyyy"
            val sdf = SimpleDateFormat(myFormat)

            data!!.text = sdf.format(consumo.data)

            combAbastecido!!.text = context.resources.getString(R.string.item_lista_consumo_abastecido, consumo.combustivel)

            if (position > 0) {
                combPercorrido!!.text = context.resources.getString(R.string.item_lista_consumo_percorrido, getItemAnterior(position).combustivel)
                combPercorrido!!.visibility = View.VISIBLE
            } else {
                combPercorrido!!.visibility = View.INVISIBLE
                combPercorrido!!.visibility = View.INVISIBLE
            }


            if (consumo.tanqueCompleto){
                tanqueCheio!!.setImageResource(R.drawable.ic_tanque_cheio)
            } else {
                tanqueCheio!!.setImageResource(R.drawable.ic_tanque_nao_cheio)
            }
        }

    }

}