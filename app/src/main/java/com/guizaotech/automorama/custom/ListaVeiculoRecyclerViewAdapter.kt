package com.guizaotech.automorama.custom

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.guizaotech.automorama.R
import com.guizaotech.automorama.helpers.CarregaImagem
import com.guizaotech.automorama.modelo.Veiculo





class ListaVeiculoRecyclerViewAdapter(
    private var listaVeiculos: MutableList<Veiculo> = mutableListOf(),
    private val context: Context
) : RecyclerView.Adapter<ListaVeiculoRecyclerViewAdapter.VeiculoViewHolder>() {

    var onItemClickListener : OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaVeiculoRecyclerViewAdapter.VeiculoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_lista_veiculo_personalizada, parent, false)

        return VeiculoViewHolder(view)

    }

    override fun getItemCount(): Int {
        return listaVeiculos.size
    }

    override fun onBindViewHolder(holder: ListaVeiculoRecyclerViewAdapter.VeiculoViewHolder, position: Int) {
        val veiculo = listaVeiculos[position]

        holder.vincula(veiculo)
    }

    fun adiciona(veiculo: Veiculo) {
        listaVeiculos.add(veiculo)
        notifyDataSetChanged()
    }

    fun remove(posicao: Int){
        //configuraListaVeiculos.remove(veiculo)
        listaVeiculos.removeAt(posicao)
        notifyDataSetChanged()

    }

    fun altera(veiculo: Veiculo, posicao: Int){
        listaVeiculos.set(posicao, veiculo)
        notifyDataSetChanged()

    }

    fun atualiza(veiculos: List<Veiculo>) {
        notifyItemRangeRemoved(0, this.listaVeiculos.size)
        this.listaVeiculos.clear()
        this.listaVeiculos.addAll(veiculos)
        notifyItemRangeInserted(0, this.listaVeiculos.size)
    }

    inner class VeiculoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var marca: TextView
        var modelo: TextView
        var placa: TextView
        var apelido: TextView
        var caminhoImagem: ImageView

        private var veiculo: Veiculo? = null

        init {
            marca = itemView.findViewById(R.id.textMarcaLista)
            modelo = itemView.findViewById(R.id.textModeloLista)
            placa = itemView.findViewById(R.id.textPlacaLista)
            apelido = itemView.findViewById(R.id.textApelidoLista)
            caminhoImagem = itemView.findViewById(R.id.imageVeiculo)

            itemView.setOnClickListener { onItemClickListener!!.onItemClick(veiculo as Veiculo, adapterPosition) }
        }

        fun vincula(veiculo: Veiculo) {

            this.veiculo = veiculo
            marca.text = veiculo.marca

            modelo.text = context.resources.getString(R.string.veiculo, veiculo.modelo, veiculo.anoFabricacao.toString(), veiculo.anoModelo.toString())

            placa.text = veiculo.placa


            if (veiculo.apelido != "") {
                apelido.text = veiculo.apelido
            } else {
                apelido.visibility = View.INVISIBLE

            }

            if (veiculo.caminhoImagem != "") {
                val helperImagem = CarregaImagem()
                caminhoImagem.setImageBitmap(helperImagem.giraImagem(veiculo.caminhoImagem!!, 300, 300))
            } else {
                caminhoImagem.setImageResource(R.drawable.ic_car_list)
            }
        }
    }
}