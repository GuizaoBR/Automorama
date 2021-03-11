package com.guizaotech.automorama

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.guizaotech.automorama.asyncTask.CarregaListaConsumoTask
import com.guizaotech.automorama.custom.ListaConsumoRecyclerView
import com.guizaotech.automorama.custom.OnItemClickListener
import com.guizaotech.automorama.database.AutomoramaDatabase
import com.guizaotech.automorama.database.RoomConsumoDao
import com.guizaotech.automorama.helpers.Codigos_Activity
import com.guizaotech.automorama.modelo.Consumo
import com.guizaotech.automorama.modelo.Veiculo
import kotlinx.android.synthetic.main.activity_gastos.*
import kotlinx.android.synthetic.main.activity_lista_consumo.*
import kotlinx.android.synthetic.main.content_lista_consumo.*

class ConsumoFragment : Fragment(), Codigos_Activity {
    private var adapter: ListaConsumoRecyclerView? = null
    private var veiculo: Veiculo? = null
    private var consumoDAO: RoomConsumoDao? = null
    private var listaConsumo: MutableList<Consumo>? = null

    companion object {
        fun newInstance() = ConsumoFragment()
    }


    override fun onAttach(context: Context?) {
        consumoDAO = AutomoramaDatabase.getInstance(context!!).getRoomConsumoDAO()
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_consumo, container, false)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()
        preencheDados()
    }

    private fun preencheDados() {
        val parametros = activity!!.intent.extras
        veiculo = parametros.getSerializable("veiculo") as Veiculo

        listaConsumo(veiculo!!.idVeiculo)

        verificaTamanhoLista()
    }
    fun listaConsumo(idVeiculo: Long?){
        CarregaListaConsumoTask(consumoDAO!!, idVeiculo!!,
            object : CarregaListaConsumoTask.CarregadoListener{
                override fun carregado(lista: MutableList<Consumo>) {
                    listaConsumo = lista
                    configuraAdapter(listaConsumo!!)
                    configuraLayoutManager()
                    verificaTamanhoLista()
                }
            }).execute()
    }
    private fun verificaTamanhoLista() {
        if (listaConsumo != null){
            if (listaConsumo!!.size > 0) {
                textoLista.visibility = View.GONE
            } else {
                textoLista.text = resources.getText(R.string.lista_consumo_nenhum_abastecimento)
                textoLista.visibility = View.VISIBLE
            }
        }
    }
    private fun configuraLayoutManager() {
        val layoutManager = LinearLayoutManager(context!!)
        lista_consumo_view.layoutManager = layoutManager
    }

    private fun configuraAdapter(consumo: MutableList<Consumo>) {
        adapter = ListaConsumoRecyclerView(consumo, context!!)
        lista_consumo_view.adapter = adapter

        adapter!!.onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(item: Any, posicao: Int) {
                val detalhes = Intent(activity, DetalhesConsumoActivity::class.java)
                detalhes.putExtra("veiculo", veiculo)
                detalhes.putExtra("consumo", item as Consumo)
                detalhes.putExtra("posicaoConsumo", posicao)
                startActivityForResult(detalhes, CODIGO_DETALHES)

            }
        }
    }
}