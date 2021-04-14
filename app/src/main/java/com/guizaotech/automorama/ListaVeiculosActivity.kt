package com.guizaotech.automorama

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.guizaotech.automorama.custom.ListaVeiculoRecyclerViewAdapter
import com.guizaotech.automorama.custom.OnItemClickListener
import com.guizaotech.automorama.database.AutomoramaDatabase
import com.guizaotech.automorama.helpers.Codigos_Activity
import com.guizaotech.automorama.modelo.Veiculo
import com.guizaotech.automorama.repository.VeiculosRepository
import com.guizaotech.automorama.viewModel.ListaVeiculosViewModel
import com.guizaotech.automorama.viewModel.factory.ListaVeiculosViewModelFactory
import kotlinx.android.synthetic.main.activity_lista_veiculos.*
import kotlinx.android.synthetic.main.content_lista_veiculos.*
import kotlinx.coroutines.runBlocking


class ListaVeiculosActivity : AppCompatActivity(), Codigos_Activity {

    private var listaVeiculo: MutableList<Veiculo>? = null

    private val adapter by lazy {
        ListaVeiculoRecyclerViewAdapter(context = this)
    }

    private val viewModel by lazy {
        val repository = VeiculosRepository(
            AutomoramaDatabase.getInstance(this).getRoomVeiculoDAO()
        )
        val factory = ListaVeiculosViewModelFactory(repository)
        val provedor = ViewModelProviders.of(this, factory)
        provedor.get(ListaVeiculosViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_veiculos)
        imageAdd.setOnClickListener {
            val paginaNovoVeiculo = Intent(this, FormVeiculoActivity::class.java)
            startActivityForResult(paginaNovoVeiculo, CODIGO_INSERE)
        }
        configuraLayout()
        buscaLista()

        registerForContextMenu(lista_veiculos_view)
    }

    override fun onResume() {
        super.onResume()
        buscaLista()
    }


    private fun verificaTamanhoLista() {

        if (listaVeiculo != null && listaVeiculo!!.size > 0) {
            textoLista.text = " "
        } else {
            textoLista.text = resources.getString(R.string.lista_veiculo_nenhum_veiculo)
        }
    }



    fun configuraLayout() {
        configuraListaAdapter()
        configuraListaLayoutManager()
    }
    private fun buscaLista() {
        buscaViewModel()
    }

    private  fun buscaViewModel() {
        viewModel.buscaTodos().observe(this, Observer { resource ->
            resource.dado?.let {
                adapter.atualiza(it)
                listaVeiculo = it.toMutableList()
            }
            verificaTamanhoLista()
        })
    }


    private fun configuraListaLayoutManager() {
        val layoutManager = LinearLayoutManager(this)
        lista_veiculos_view.layoutManager = layoutManager
    }

    private fun configuraListaAdapter() {
        lista_veiculos_view.adapter = adapter
        adapter.onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(item: Any, posicao: Int) {
                val detalhes = Intent(this@ListaVeiculosActivity, DetalhesVeiculoActivity::class.java)
                detalhes.putExtra("veiculo", item as Veiculo)
                startActivityForResult(detalhes, CODIGO_DETALHES)

            }
        }
    }
}
