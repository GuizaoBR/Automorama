package com.guizaotech.automorama

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.guizaotech.automorama.custom.ListaManutencaoRecyclerView
import com.guizaotech.automorama.custom.OnItemClickListener
import com.guizaotech.automorama.database.AutomoramaDatabase
import com.guizaotech.automorama.helpers.Codigos_Activity
import com.guizaotech.automorama.modelo.Manutencao
import com.guizaotech.automorama.modelo.Veiculo
import com.guizaotech.automorama.repository.ManutencaoRepository
import com.guizaotech.automorama.viewModel.ManutencaoViewModel
import com.guizaotech.automorama.viewModel.factory.ManutencaoViewModelFactory
import kotlinx.android.synthetic.main.content_lista_manutencao.*


class ManutencaoFragment : Fragment(), Codigos_Activity {

    private var adapter: ListaManutencaoRecyclerView? = null
    private var veiculo: Veiculo? = null

    private val viewModel by lazy {
        val repository = ManutencaoRepository(
            AutomoramaDatabase.getInstance(requireContext()).getRoomManutencaoDAO()
        )
        val factory = ManutencaoViewModelFactory(repository)
        val provedor = ViewModelProviders.of(this, factory)
        provedor.get(ManutencaoViewModel::class.java)
    }

    companion object {
        fun newInstance() = ManutencaoFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_manutencao, container, false)
        return view
    }

    override fun onStart() {
        super.onStart()
        preencheDados()

    }

    private fun preencheDados() {
        val parametros = requireActivity().intent.extras
        veiculo = parametros?.getSerializable("veiculo") as Veiculo
        viewModel.buscaTodos(veiculo!!.idVeiculo)
        listaManutencao()

    }
    fun listaManutencao() {
        viewModel.liveDataManutencao.observe(this, {
            it.dado?.let { lista ->
                configuraLista(lista)
            }
        })


    }

    fun configuraLista(lista: List<Manutencao>){
        configuraAdapter(lista)
        configuraLayoutManager()
        verificaTamanhoLista(lista)
    }

    private fun verificaTamanhoLista(listaManutencao: List<Manutencao>) {
        if (listaManutencao.size > 0) {
            textoLista.visibility = View.GONE
        } else {
            textoLista.text = resources.getText(R.string.lista_manutecao_nenhuma_manutecao)
            textoLista.visibility = View.VISIBLE
        }
    }
    private fun configuraLayoutManager() {
        val layoutManager = LinearLayoutManager(requireContext())
        lista_manutencao_view.layoutManager = layoutManager
    }

    private fun configuraAdapter(lista: List<Manutencao>) {
        adapter = ListaManutencaoRecyclerView(lista, requireContext())
        lista_manutencao_view.adapter = adapter

        adapter!!.onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(item: Any, posicao: Int) {
                /*val detalhes = Intent(activity, DetalhesConsumoActivity::class.java)
                detalhes.putExtra("veiculo", veiculo)
                detalhes.putExtra("manutencao", item as Manutencao)
                detalhes.putExtra("posicaoManutencao", posicao)
                startActivityForResult(detalhes, CODIGO_DETALHES)*/

            }
        }
    }

}