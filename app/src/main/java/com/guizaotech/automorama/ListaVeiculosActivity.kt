package com.guizaotech.automorama

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.guizaotech.automorama.asyncTask.CarregaListaVeiculoTask
import com.guizaotech.automorama.custom.ListaVeiculoRecyclerViewAdapter
import com.guizaotech.automorama.custom.OnItemClickListener
import com.guizaotech.automorama.database.AutomoramaDatabase
import com.guizaotech.automorama.database.RoomVeiculoDao
import com.guizaotech.automorama.helpers.Codigos_Activity
import com.guizaotech.automorama.modelo.Veiculo
import com.guizaotech.automorama.repository.VeiculosRepository
import com.guizaotech.automorama.viewModel.ListaVeiculosViewModel
import com.guizaotech.automorama.viewModel.factory.ListaVeiculosViewModelFactory
import kotlinx.android.synthetic.main.activity_lista_veiculos.*
import kotlinx.android.synthetic.main.content_lista_veiculos.*


class ListaVeiculosActivity : AppCompatActivity(), Codigos_Activity {

   // private var adapter : ListaVeiculoRecyclerViewAdapter? = null
    private var daoVeiculo : RoomVeiculoDao? = null
    private var listaVeiculo: MutableList<Veiculo>? = null

    private val adapter by lazy {
        ListaVeiculoRecyclerViewAdapter(context = this)
    }

    private val viewModel by lazy {
        val repository = VeiculosRepository(AutomoramaDatabase.getInstance(this).getRoomVeiculoDAO())
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
        //verificaTamanhoLista()


        registerForContextMenu(lista_veiculos_view)
    }



    override fun onResume() {
        super.onResume()
//        buscaLista()
//        if (listaVeiculo != null){
//            verificaTamanhoLista()
//        }


    }

    private fun verificaTamanhoLista() {

        if (listaVeiculo!!.size > 0) {
            textoLista.text = " "
        } else {
            textoLista.text = resources.getString(R.string.lista_veiculo_nenhum_veiculo)
        }
    }


//    private fun buscaLista() {
//
//        CarregaListaVeiculoTask(daoVeiculo!!,
//            object : CarregaListaVeiculoTask.EncontradoListener{
//                override fun encontrado(lista: MutableList<Veiculo>) {
//                    listaVeiculo = lista
//                    configuraListaAdapter(listaVeiculo!!)
//                    configuraListaLayoutManager()
//                    verificaTamanhoLista()
//                }
//            }).execute()
//
//    }

    fun configuraLayout() {
        configuraListaAdapter()
        configuraListaLayoutManager()
    }
    private fun buscaLista() {

        viewModel.buscaTodos().observe(this, Observer { resource ->
            resource?.let {
                    adapter.atualiza(it)
            }
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
                detalhes.putExtra("veiculoDetalhes", posicao)
                detalhes.putExtra("veiculo", item as Veiculo)
                startActivityForResult(detalhes, CODIGO_DETALHES)

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CODIGO_INSERE && resultCode == Activity.RESULT_OK && data!!.hasExtra("veiculo")){
            val veiculoRecebido = data.getSerializableExtra("veiculo") as Veiculo
            adapter.adiciona(veiculoRecebido)
            Toast.makeText(this, "Salvo", Toast.LENGTH_SHORT).show()



        }
        if (requestCode == CODIGO_DETALHES && resultCode == Activity.RESULT_OK && data!!.hasExtra("posicao")){
            val veiculoRecebido = data.getSerializableExtra("veiculo") as Veiculo
            val posicao = data.getIntExtra("posicao", -1)
            adapter.remove(posicao)

            Toast.makeText(this@ListaVeiculosActivity, "$veiculoRecebido deletado", Toast.LENGTH_SHORT).show()
        }


        if (requestCode == CODIGO_DETALHES && resultCode == Activity.RESULT_OK && data!!.hasExtra("veiculoAlterado") && data.hasExtra("veiculoPosicao")){
            val veiculoRecebido = data.getSerializableExtra("veiculoAlterado") as Veiculo
            val posicao = data.getIntExtra("veiculoPosicao", -CODIGO_INSERE)
            adapter.altera(veiculoRecebido, posicao)

        }

        super.onActivityResult(requestCode, resultCode, data)

    }



}
