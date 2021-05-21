package com.guizaotech.automorama

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.guizaotech.automorama.custom.toDate
import com.guizaotech.automorama.database.AutomoramaDatabase
import com.guizaotech.automorama.databinding.ActivityFormManutencaoBinding
import com.guizaotech.automorama.modelo.Manutencao
import com.guizaotech.automorama.repository.ManutencaoRepository
import com.guizaotech.automorama.viewModel.FormManutencaoViewModel
import com.guizaotech.automorama.viewModel.factory.FormManutencaoViewModelFactory
import kotlinx.coroutines.runBlocking
import java.util.*

class FormManutencaoActivity : AppCompatActivity() {

    private var biding : ActivityFormManutencaoBinding? = null
    private var idVeiculo = 0L

    private val viewModel by lazy {
        val repository = ManutencaoRepository(AutomoramaDatabase.getInstance(this).getRoomManutencaoDAO())
        val factory = FormManutencaoViewModelFactory(repository)
        val provedor = ViewModelProviders.of(this, factory)
        provedor.get(FormManutencaoViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        biding = ActivityFormManutencaoBinding.inflate(layoutInflater)
        setContentView(biding!!.root)
        viewModel.resultadoLiveData.observe(this, {
            if (it.erro == null){
                finish()
            }
        })

        var dadosVeiculo = intent.getStringExtra("veiculo")
        idVeiculo = dadosVeiculo?.split("|")!!.last().toLong()
        biding?.btSalvar?.setOnClickListener {
            runBlocking {
                val manutencaoNovo = Manutencao()
                manutencaoNovo.titulo  = biding?.inputTitulo?.text.toString()
                manutencaoNovo.descricao = biding?.inputDescricao?.text.toString()
                manutencaoNovo.feito = biding?.checkFeito?.isChecked!!
                manutencaoNovo.valor = biding?.inputValor?.text.toString().toFloat()
                manutencaoNovo.data = "20/05/2021".toDate("dd/MM/yyyy")!!
                manutencaoNovo.idVeiculo = idVeiculo
                viewModel.salva(manutencaoNovo)

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        biding = null
    }
}