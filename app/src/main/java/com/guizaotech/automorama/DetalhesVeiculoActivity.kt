package com.guizaotech.automorama

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.guizaotech.automorama.asyncTask.CarregaImagemDetalhesTask
import com.guizaotech.automorama.asyncTask.DeletarVeiculoTask
import com.guizaotech.automorama.asyncTask.mediaConsumoTask
import com.guizaotech.automorama.database.AutomoramaDatabase
import com.guizaotech.automorama.database.RoomConsumoDao
import com.guizaotech.automorama.database.RoomVeiculoDao
import com.guizaotech.automorama.helpers.Codigos_Activity
import com.guizaotech.automorama.modelo.Veiculo
import com.guizaotech.automorama.repository.VeiculosRepository
import com.guizaotech.automorama.viewModel.DetalhesVeiculosViewModel
import com.guizaotech.automorama.viewModel.FormVeiculosViewModel
import com.guizaotech.automorama.viewModel.factory.DetalhesVeiculosViewModelFactory
import com.guizaotech.automorama.viewModel.factory.FormVeiculosViewModelFactory
import kotlinx.android.synthetic.main.activity_detalhes_veiculo.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

class DetalhesVeiculoActivity : AppCompatActivity(), Codigos_Activity {
    var veiculoPosicao: Int? = null
    var consumoDAO: RoomConsumoDao? = null
    var veiculoSelecionado: Veiculo? = null

    private val viewModel by lazy {
        val repository = VeiculosRepository(AutomoramaDatabase.getInstance(this).getRoomVeiculoDAO())
        val factory = DetalhesVeiculosViewModelFactory(repository)
        val provedor = ViewModelProviders.of(this, factory)
        provedor.get(DetalhesVeiculosViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_veiculo)
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener {
            finish()
        }
        consumoDAO = AutomoramaDatabase.getInstance(this).getRoomConsumoDAO()

        veiculoSelecionado = preencheDetalhes(false)

        mediaConsumo(veiculoSelecionado!!)

        btAbastecimento.setOnClickListener {
            val pagConsumo = Intent(this, GastosActivity::class.java)
            pagConsumo.putExtra("veiculo", veiculoSelecionado)
            startActivity(pagConsumo)
        }


        btDeletar.setOnClickListener {
            confimarDeletar(veiculoSelecionado!!)
        }

        btEditarClick()
    }

    private fun btEditarClick() {
        btEditar.setOnClickListener {
            val pagEditar = Intent(this@DetalhesVeiculoActivity, FormVeiculoActivity::class.java)
            pagEditar.putExtra("editarVeiculo", veiculoSelecionado)
            pagEditar.putExtra("veiculoPosicao", veiculoPosicao)
            startActivityForResult(pagEditar, CODIGO_ALTERA)
        }
    }

    private fun confimarDeletar(veiculoSelecionado: Veiculo) {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle(resources.getString(R.string.atencao))

        builder.setMessage(
            resources.getString(R.string.confirmar_deletar_veiculo, veiculoSelecionado.modelo)
        )

        builder.setPositiveButton(resources.getString(R.string.sim)) { dialog, which ->
            runBlocking {
                deleta(veiculoSelecionado)
            }

        }
        builder.setNegativeButton(resources.getString(R.string.nao)) { dialog, which ->
            dialog.cancel()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private suspend fun deleta(veiculoSelecionado: Veiculo) {
        viewModel.deleta(veiculo = veiculoSelecionado).observe(this, Observer {
            if (it.erro == null) {
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                Toast.makeText(
                    this,
                    it.erro,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun preencheDetalhes(ehResume: Boolean): Veiculo {
        if(!ehResume){
            val parametros = intent.extras
            veiculoPosicao = parametros.getInt("veiculoDetalhes")
            veiculoSelecionado = parametros.getSerializable("veiculo") as Veiculo
        }
        textMarca!!.text = resources.getString(R.string.detalhes_marca, veiculoSelecionado!!.marca)
        textModelo!!.text = resources.getString(R.string.detalhes_modelo, veiculoSelecionado!!.modelo)
        textAnoFabricacaoModelo!!.text = resources.getString(R.string.detalhes_fabricacao_modelo,  veiculoSelecionado!!.anoFabricacao.toString(), veiculoSelecionado!!.anoModelo.toString())

        textPlaca!!.text = resources.getString(R.string.detalhes_Placa, veiculoSelecionado!!.placa)

        if (veiculoSelecionado!!.caminhoImagem != "") {
            carregaImagem(veiculoSelecionado!!)
        }

        if (veiculoSelecionado!!.apelido != "") {
            textApelido!!.text = resources.getString(R.string.detalhes_apelido, veiculoSelecionado!!.apelido)
            textApelido.visibility - View.VISIBLE
        } else {
            textApelido!!.visibility = View.INVISIBLE
        }
        return veiculoSelecionado!!
    }

    private fun carregaImagem(veiculoSelecionado: Veiculo) {
        CarregaImagemDetalhesTask(
            veiculoSelecionado,
            object : CarregaImagemDetalhesTask.EncontradoListener {
                override fun encontrado(imagem: Bitmap) {
                    imageVeiculo.setImageBitmap(imagem)
                    imageVeiculo.scaleType = ImageView.ScaleType.FIT_XY
                    progressBar.visibility = View.GONE
                    imageVeiculo.visibility = View.VISIBLE
                }

                override fun carregando() {
                    imageVeiculo.visibility = View.INVISIBLE
                    progressBar.visibility = View.VISIBLE
                }
            }).execute()
    }

    override fun onResume() {
        super.onResume()
        veiculoSelecionado = preencheDetalhes(true)
        mediaConsumo(veiculoSelecionado!!)
        btEditarClick()
    }

    private fun mediaConsumo(veiculoSelecionado: Veiculo) {
        mediaConsumoTask(consumoDAO!!, veiculoSelecionado,
            object : mediaConsumoTask.EncontradoListener{
                override fun encontrado(media: String) {
                    if (media != ""){
                        //textConsumoMedio.text = "Consumo médio: ${media}Km/L"
                        val consumoMedio = resources.getString(R.string.consumo_medio, media)
                        textConsumoMedio.text = consumoMedio
                    } else {
                        val semConsumoMedio = resources.getString(R.string.sem_consumo_medio)
                        textConsumoMedio.text = semConsumoMedio
                    }
                }

                override fun carregando() {
                    textConsumoMedio.setText(R.string.consumo_medio_calculando)
                }
            }).execute()

    }
}
