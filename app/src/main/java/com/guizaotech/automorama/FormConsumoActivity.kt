package com.guizaotech.automorama

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.guizaotech.automorama.database.AutomoramaDatabase
import com.guizaotech.automorama.helpers.HelperConsumo
import com.guizaotech.automorama.modelo.Consumo
import com.guizaotech.automorama.modelo.Veiculo
import com.guizaotech.automorama.repository.ConsumoRepository
import com.guizaotech.automorama.viewModel.ConsumoViewModel
import com.guizaotech.automorama.viewModel.factory.ListaConsumoViewModelFactory
import kotlinx.android.synthetic.main.activity_form_consumo.*
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*


class FormConsumoActivity : AppCompatActivity() {


    val calendario = Calendar.getInstance()
    var consumo: Consumo? = null

    private val viewModel by lazy {
        val repository = ConsumoRepository(
            AutomoramaDatabase.getInstance(this).getRoomConsumoDAO()
        )
        val factory = ListaConsumoViewModelFactory(repository)
        val provedor = ViewModelProviders.of(this, factory)
        provedor.get(ConsumoViewModel::class.java)
    }
    private var veiculo: Veiculo? = null

    private val helperConsumo: HelperConsumo
        get() {
            val helper = HelperConsumo(this, veiculo!!.idVeiculo)
            return helper
        }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_consumo)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        val parametros = intent.extras
        veiculo = parametros?.getSerializable("veiculo") as Veiculo


        if (parametros.getSerializable("consumo") != null) {
            consumo = parametros.getSerializable("consumo") as Consumo
        }



        veiculo(veiculo!!)

        configuraSpinner(combustivel)

        val selecionarData = selecionaData()


        data!!.setOnClickListener {

            val datePicker = DatePickerDialog(
                this@FormConsumoActivity,
                selecionarData,
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.datePicker.maxDate = Date().time //limita até a data atual
            /*  if(listaConsumo.isNotEmpty()){
                  val sdf = SimpleDateFormat()
                  datePicker.datePicker.minDate = calendario.time.time
              }*/
            datePicker.show()
        }



        btSalvar.setOnClickListener {
            if(helperConsumo.ehValido()) {
                val novoConsumo: Consumo = helperConsumo.getConsumo(consumo!!)

                runBlocking {
                   salva(novoConsumo)
                }

            }
        }
        if (consumo != null){
            preencherCampos()
        } else {
            consumo = Consumo()
        }
    }

    private suspend fun salva(novoConsumo: Consumo){
        viewModel.salvaConsumo(consumo = novoConsumo).observe(this, {
            it.dado?.let {
                enviaDados(novoConsumo)
                finish()
            }
        })
    }


    override fun onResume() {
        super.onResume()

    }

    fun preencherCampos(){
        helperConsumo.preencheConsumo(consumo!!)
    }

    private fun selecionaData(): DatePickerDialog.OnDateSetListener {
        return DatePickerDialog.OnDateSetListener { view, ano, mes, dia ->
            calendario.set(Calendar.YEAR, ano)
            calendario.set(Calendar.MONTH, mes)
            calendario.set(Calendar.DAY_OF_MONTH, dia)
            updateData()
        }
    }

    private fun veiculo(veiculo: Veiculo) {

        if (veiculo.apelido == "") {
            veiculoSelecionado.text = resources.getString(R.string.veiculo, veiculo.modelo, veiculo.anoFabricacao.toString(), veiculo.anoModelo.toString())
        } else {
            veiculoSelecionado.text = veiculo.apelido
        }
    }

//    private fun verificaCampos(novoConsumo: Consumo) =
//        novoConsumo.combustivel == "" || novoConsumo.data.time.toString() == "" || novoConsumo.kmAnterior == 0 ||
//                novoConsumo.kmAtual == 0 || novoConsumo.litros == 0.0

    private fun enviaDados(novoConsumo: Consumo) {
        val intentOk = Intent()
        intentOk.putExtra("consumoAlterado", novoConsumo)
        setResult(Activity.RESULT_OK, intentOk)
    }


    private fun configuraSpinner(spinner: Spinner) {
        val listaSpinner = ArrayList<String>()
        listaSpinner.add("Gasolina Comum")
        listaSpinner.add("Gasolina Aditivada")
        listaSpinner.add("Etanol")
        listaSpinner.add("Diesel")

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listaSpinner)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter
    }




    private fun updateData() {
        val myFormat = "dd/MM/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat)
        data!!.setText(sdf.format(calendario.time))
    }

}


