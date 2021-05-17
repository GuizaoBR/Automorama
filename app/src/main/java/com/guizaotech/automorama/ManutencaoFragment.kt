package com.guizaotech.automorama

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.guizaotech.automorama.asyncTask.CarregaListaManutencaoTask
import com.guizaotech.automorama.custom.ListaManutencaoRecyclerView
import com.guizaotech.automorama.custom.OnItemClickListener
import com.guizaotech.automorama.database.AutomoramaDatabase
import com.guizaotech.automorama.database.RoomManutencaoDao
import com.guizaotech.automorama.helpers.Codigos_Activity
import com.guizaotech.automorama.modelo.Manutencao
import com.guizaotech.automorama.modelo.Veiculo
import kotlinx.android.synthetic.main.content_lista_manutencao.*


class ManutencaoFragment : Fragment(), Codigos_Activity {

    private var adapter: ListaManutencaoRecyclerView? = null
    private var veiculo: Veiculo? = null
    private var manutencaoDAO: RoomManutencaoDao? = null
    private var listaMenutencao: MutableList<Manutencao>? = null

    companion object {
        fun newInstance() = ManutencaoFragment()
    }

    override fun onAttach(context: Context) {
        manutencaoDAO = AutomoramaDatabase.getInstance(requireContext()).getRoomManutencaoDAO()
        super.onAttach(context)
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

        listaManutencao(veiculo!!.idVeiculo)

        verificaTamanhoLista()
    }
    fun listaManutencao(idVeiculo: Long?){
        CarregaListaManutencaoTask(manutencaoDAO!!, idVeiculo!!,
            object : CarregaListaManutencaoTask.CarregadoListener{
                override fun carregado(lista: MutableList<Manutencao>) {
                    listaMenutencao = lista
                    configuraAdapter(listaMenutencao!!)
                    configuraLayoutManager()
                    verificaTamanhoLista()
                }
            }).execute()
    }
    private fun verificaTamanhoLista() {
        if (listaMenutencao != null){
            if (listaMenutencao!!.size > 0) {
                textoLista.visibility = View.GONE
            } else {
                textoLista.text = resources.getText(R.string.lista_manutecao_nenhuma_manutecao)
                textoLista.visibility = View.VISIBLE
            }
        }
    }
    private fun configuraLayoutManager() {
        val layoutManager = LinearLayoutManager(requireContext())
        lista_manutencao_view.layoutManager = layoutManager
    }

    private fun configuraAdapter(manutencao: MutableList<Manutencao>) {
        adapter = ListaManutencaoRecyclerView(manutencao, requireContext())
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