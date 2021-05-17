package com.guizaotech.automorama

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.guizaotech.automorama.custom.ListaConsumoRecyclerView
import com.guizaotech.automorama.custom.OnItemClickListener
import com.guizaotech.automorama.database.AutomoramaDatabase
import com.guizaotech.automorama.helpers.Codigos_Activity
import com.guizaotech.automorama.modelo.Consumo
import com.guizaotech.automorama.modelo.Veiculo
import com.guizaotech.automorama.repository.GastosRepository
import com.guizaotech.automorama.viewModel.ConsumoViewModel
import com.guizaotech.automorama.viewModel.factory.ListaConsumoViewModelFactory
import kotlinx.android.synthetic.main.activity_lista_consumo.*
import kotlinx.android.synthetic.main.content_lista_consumo.*

class ListaConsumoActivity : AppCompatActivity(), Codigos_Activity {

    private var veiculo: Veiculo? = null
    private var listaConsumo: MutableList<Consumo>? = null


    private val adapter by lazy {
        ListaConsumoRecyclerView(context = this)
    }

    private val viewModel by lazy {
        val repository = GastosRepository(
            AutomoramaDatabase.getInstance(this).getRoomConsumoDAO()
        )
        val factory = ListaConsumoViewModelFactory(repository)
        val provedor = ViewModelProviders.of(this, factory)
        provedor.get(ConsumoViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_consumo)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        preencheDados()


        imageAdd.setOnClickListener {
            val paginaConsumo = Intent(this, FormConsumoActivity::class.java)
            paginaConsumo.putExtra("veiculo", veiculo)
            startActivityForResult(paginaConsumo, CODIGO_INSERE)
        }

        registerForContextMenu(lista_consumo_view)


    }

    private fun preencheDados() {
        val parametros = intent.extras
        veiculo = parametros!!.getSerializable("veiculo") as Veiculo

        listaConsumo(veiculo!!.idVeiculo)

        if (veiculo!!.apelido == "") {
            veiculoSelecionado.text = veiculo!!.modelo + " " + veiculo!!.anoFabricacao + "/" + veiculo!!.anoModelo
        } else {
            veiculoSelecionado.text = veiculo!!.apelido
        }
        verificaTamanhoLista()
    }

    override fun onResume() {
        super.onResume()
        verificaTamanhoLista()

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

    fun listaConsumo(idVeiculo: Long){
        viewModel.buscaTodosConsummo(idVeiculo = idVeiculo).observe(this, Observer { resource ->
            resource.dado?.let { lista ->
                listaConsumo = lista.toMutableList()
                configuraLayout()
                verificaTamanhoLista()
            }
        })
//        CarregaListaConsumoTask(consumoDAO!!, idVeiculo!!,
//            object : CarregaListaConsumoTask.CarregadoListener{
//                override fun carregado(lista: MutableList<Consumo>) {
//                    listaConsumo = lista
//                    configuraAdapter(listaConsumo!!)
//                    configuraLayoutManager()
//                    verificaTamanhoLista()
//                }
//            }).execute()
    }

    fun configuraLayout(){
        configuraLayoutManager()
        configuraAdapter()
    }

    private fun configuraLayoutManager() {
        val layoutManager = LinearLayoutManager(this)
        lista_consumo_view.layoutManager = layoutManager
    }

    private fun configuraAdapter() {
        lista_consumo_view.adapter = adapter
        adapter.onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(item: Any, posicao: Int) {
                val detalhes = Intent(this@ListaConsumoActivity, DetalhesConsumoActivity::class.java)
                detalhes.putExtra("veiculo", veiculo)
                detalhes.putExtra("consumo", item as Consumo)
                detalhes.putExtra("posicaoConsumo", posicao)
                startActivityForResult(detalhes, CODIGO_DETALHES)

            }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == CODIGO_DETALHES && resultCode == Activity.RESULT_OK && data!!.hasExtra("consumoDeletado")){
            val consumoRecebido = data.getSerializableExtra("consumoDeletado") as Consumo

            val posicao = data.getIntExtra("consumoPosicao", -1)
            adapter!!.remove(posicao)
        }

        if (requestCode == CODIGO_INSERE && resultCode == Activity.RESULT_OK){
            val consumo = data!!.getSerializableExtra("consumo") as Consumo

            adapter!!.adiciona(consumo)
        }

        if (requestCode == CODIGO_DETALHES && resultCode == Activity.RESULT_OK && data!!.hasExtra("consumoAlterado")){
            val consumo = data.getSerializableExtra("consumoAlterado") as Consumo
            val posicao = data.getIntExtra("consumoPosicao", -1)
            adapter!!.altera(consumo, posicao)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    /*private fun adiciona(consumo: Consumo) {
        val daoConsumo = DaoConsumo(this)
        daoConsumo.insereConsumo(consumo, veiculo!!.idVeiculo as Long)
        daoConsumo.close()
    }*/

    /*private fun deleta(consumoRecebido: Consumo) {
        val db = DaoConsumo(this@ListaConsumoActivity)
        db.deletaConsumo(consumoRecebido)
        db.close()
    }*/

    /*override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {

        val info =  menuInfo as AdapterView.AdapterContextMenuInfo
        val consumo = listaConsumo!!.getItemAtPosition(info.position) as Consumo

        val parametros = intent.extras
        val veiculo = parametros.getSerializable("veiculo") as Veiculo


        val opDeletar = menu!!.add("Deletar")
        opDeletar.setOnMenuItemClickListener {
            val db = DaoConsumo(this@ListaConsumoActivity)
            db.deletaConsumo(consumo)
            db.close()
            Toast.makeText(this, "Deletado", Toast.LENGTH_SHORT).show()
            listaConsumo(veiculo.idVeiculo)
            false
        }

        val opEditar = menu.add("Editar")
        opEditar.setOnMenuItemClickListener {
            val pagEditar = Intent(this@ListaConsumoActivity, FormConsumoActivity::class.java)
            val parametros = intent.extras
            val veiculo = parametros.getSerializable("veiculo") as Veiculo
            pagEditar.putExtra("editarConsumo", consumo)
            pagEditar.putExtra("idVeiculoEditar", veiculo.idVeiculo)
            startActivity(pagEditar)
            false

        }

        super.onCreateContextMenu(menu, v, menuInfo)
    }*/

}
