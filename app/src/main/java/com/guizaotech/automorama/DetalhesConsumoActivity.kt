package com.guizaotech.automorama

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.guizaotech.automorama.custom.formatString
import com.guizaotech.automorama.database.AutomoramaDatabase
import com.guizaotech.automorama.helpers.Codigos_Activity
import com.guizaotech.automorama.modelo.Consumo
import com.guizaotech.automorama.modelo.Veiculo
import com.guizaotech.automorama.repository.ConsumoRepository
import com.guizaotech.automorama.viewModel.DetalhesConsumoViewModel
import com.guizaotech.automorama.viewModel.factory.DetalhesConsumoViewModelFactory
import kotlinx.android.synthetic.main.activity_detalhes_consumo.*
import kotlinx.coroutines.runBlocking


class DetalhesConsumoActivity : AppCompatActivity(), Codigos_Activity {
    var consumo: Consumo? = null

    private val viewModel by lazy {
        val repository = ConsumoRepository(AutomoramaDatabase.getInstance(this).getRoomConsumoDAO())
        val factory = DetalhesConsumoViewModelFactory(repository)
        val provedor = ViewModelProviders.of(this, factory)
        provedor.get(DetalhesConsumoViewModel::class.java)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_consumo)
        //setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }


        val (veiculo, consumo) = preencheDados()

        btDeletar.setOnClickListener {
            confimarDeletar(consumo)
        }

        btEditar.setOnClickListener {
            val pagEditar = Intent(this@DetalhesConsumoActivity, FormConsumoActivity::class.java)
            pagEditar.putExtra("veiculo", veiculo)
            pagEditar.putExtra("consumo", consumo)
            startActivityForResult(pagEditar, CODIGO_ALTERA)
        }

    }

    override fun onResume() {
        super.onResume()
        preencheDados()
    }

    private fun confimarDeletar(consumo: Consumo) {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle("ATENÇÃO")

        builder.setMessage(
            "Tem certeza que deseja deletar este abastecimento?"
        )

        builder.setPositiveButton("Sim") { dialog, which ->
            runBlocking {
                deleta(consumo)
            }



        }
        builder.setNegativeButton("Não") { dialog, which ->
            dialog.cancel()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private suspend fun deleta(consumo: Consumo) {
        viewModel.deleta(consumo).observe(this, Observer {
            if (it.erro == null){
                finish()
            }
        })
    }




    private fun preencheDados(): Pair<Veiculo, Consumo> {
        val parametros = intent.extras
        val veiculo = veiculo(parametros)
        if (consumo == null) {
            consumo = parametros?.getSerializable("consumo") as Consumo
        }

        textCombustivel.text = "Combustível: ${consumo!!.combustivel}"
        runBlocking {
            consumoAnterior(veiculo.idVeiculo)
        }

        textData.text = "Data: ${consumo!!.data.formatString("dd/MM/yyyy")}"
        textValor?.text = "${getString(R.string.moeda)} ${consumo!!.valor}"
        textPercorrido.text =
            "Percorrido: " + (consumo!!.kmAtual!!.toDouble() - consumo!!.kmAnterior!!.toDouble()).toString() + "Km"

        if (consumo!!.tanqueCompleto) {
            textTanqueAbastecido.text = "Tanque abastecido por completo"
        } else {
            textTanqueAbastecido.text = "Tanque não abastecido por completo"
        }
        return Pair(veiculo, consumo!!)
    }

   suspend fun consumoAnterior(idVeiculo: Long){
        viewModel.consumoAnterior(idVeiculo).observe(this, {
            if (it.erro == null){
                textConsumo.text ="Consumo: ${consumo!!.consumoTotal}Km/L com ${it.dado!!.combustivel}"
            }
        })
    }

    private fun veiculo(parametros: Bundle?): Veiculo {
        val veiculo = parametros!!.getSerializable("veiculo") as Veiculo


        if (veiculo.apelido.isEmpty()) {
            veiculoSelecionado.text =
                veiculo.modelo + " " + veiculo.anoFabricacao + "/" + veiculo.anoModelo
        } else {
            veiculoSelecionado.text = veiculo.apelido
        }
        return veiculo
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODIGO_ALTERA && resultCode == Activity.RESULT_OK){
            consumo = data!!.getSerializableExtra("consumoAlterado") as Consumo

        }
    }




}
