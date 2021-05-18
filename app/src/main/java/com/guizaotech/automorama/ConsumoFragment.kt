package com.guizaotech.automorama

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.guizaotech.automorama.custom.ListaConsumoRecyclerView
import com.guizaotech.automorama.custom.OnItemClickListener
import com.guizaotech.automorama.database.AutomoramaDatabase
import com.guizaotech.automorama.helpers.Codigos_Activity
import com.guizaotech.automorama.modelo.Consumo
import com.guizaotech.automorama.modelo.Veiculo
import com.guizaotech.automorama.repository.ConsumoRepository
import com.guizaotech.automorama.viewModel.ConsumoViewModel
import com.guizaotech.automorama.viewModel.factory.ListaConsumoViewModelFactory
import kotlinx.android.synthetic.main.content_lista_consumo.*

class ConsumoFragment : Fragment(), Codigos_Activity {
    private var veiculo: Veiculo? = null
    private var listaConsumo: MutableList<Consumo>? = null


    private val adapter by lazy {
        ListaConsumoRecyclerView(context = this.requireContext())
    }

    private val viewModel by lazy {
        val repository = ConsumoRepository(
            AutomoramaDatabase.getInstance(requireContext()).getRoomConsumoDAO()
        )
        val factory = ListaConsumoViewModelFactory(repository)
        val provedor = ViewModelProviders.of(this, factory)
        provedor.get(ConsumoViewModel::class.java)
    }



    companion object {
        fun newInstance() = ConsumoFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_consumo, container, false)

        return view
    }



    override fun onStart() {
        super.onStart()
        preencheDados()
    }

    private fun preencheDados() {
        val parametros = requireActivity().intent.extras!!
        veiculo = parametros.getSerializable("veiculo") as Veiculo

        listaConsumo(veiculo!!.idVeiculo)

        verificaTamanhoLista()
    }
    fun listaConsumo(idVeiculo: Long){
        viewModel.buscaTodosConsummo(idVeiculo = idVeiculo).observe(this, Observer { resource ->
            resource.dado?.let { lista ->
                listaConsumo = lista.toMutableList()
                adapter.atualiza(lista)
                configuraLayout()
                verificaTamanhoLista()
            }
        })

    }

    fun configuraLayout(){
        configuraLayoutManager()
        configuraAdapter()
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
        val layoutManager = LinearLayoutManager(requireContext())
        lista_consumo_view.layoutManager = layoutManager
    }

    private fun configuraAdapter() {
        lista_consumo_view.adapter = adapter

        adapter.onItemClickListener = object : OnItemClickListener {
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