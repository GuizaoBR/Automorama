package com.guizaotech.automorama

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.guizaotech.automorama.asyncTask.AdicionaConsumoTask
import com.guizaotech.automorama.asyncTask.CarregaListaConsumoTask
import com.guizaotech.automorama.asyncTask.EditaConsumoTask
import com.guizaotech.automorama.database.AutomoramaDatabase
import com.guizaotech.automorama.database.RoomConsumoDao
import com.guizaotech.automorama.helpers.HelperConsumo
import com.guizaotech.automorama.modelo.Consumo
import com.guizaotech.automorama.modelo.Veiculo
import kotlinx.android.synthetic.main.activity_form_consumo.*
import java.text.SimpleDateFormat
import java.util.*


class FormConsumoActivity : AppCompatActivity() {


    val calendario = Calendar.getInstance()!!
    var daoConsumo : RoomConsumoDao? = null
    var listaConsumo: MutableList<Consumo>? = null
    var consumo: Consumo? = null

    private val helperConsumo: HelperConsumo
        get() {
            val helper = HelperConsumo(this)
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
        val veiculo = parametros.getSerializable("veiculo") as Veiculo


        if (parametros.getSerializable("consumo") != null) {
            consumo = parametros.getSerializable("consumo") as Consumo
        }

        daoConsumo = AutomoramaDatabase.getInstance(this).getRoomConsumoDAO()



        veiculo(veiculo)

        //carregaListaConsumo(veiculo)

        CarregaListaConsumoTask(daoConsumo!!, veiculo.idVeiculo,
            object : CarregaListaConsumoTask.CarregadoListener {
                override fun carregado(lista: MutableList<Consumo>) {
                    listaConsumo = lista
                    if (consumo != null) {
                        helperConsumo.preencheConsumo(consumo!!)
                    } else if (listaConsumo!!.isNotEmpty()) {
                        val kmAnterior = findViewById<TextView>(R.id.kmAnterior)
                        kmAnterior.text = listaConsumo!!.last().kmAtual.toString()
                    }
                }
            }).execute()
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
                val novoConsumo: Consumo = helperConsumo.getConsumo()
                if (consumo!= null){
                    novoConsumo.idConsumo = consumo!!.idConsumo
                    novoConsumo.idVeiculo = consumo!!.idVeiculo
                }
                when {
                    novoConsumo.idConsumo != 0L -> {
                        btSalvar.isClickable = false
                        converteParaDataEUA(novoConsumo)
                        EditaConsumoTask(daoConsumo!!, novoConsumo,
                            object : EditaConsumoTask.FinalizaListener{
                                override fun finaliza() {
                                    enviaDados(novoConsumo)
                                    finish()
                                }
                            }).execute()

                    }
                    //verificaCampos(novoConsumo) -> Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_LONG).show()
                    else -> {
                        btSalvar.isClickable = false
                        converteParaDataEUA(novoConsumo)
                        novoConsumo.idVeiculo = veiculo.idVeiculo
                        AdicionaConsumoTask(daoConsumo!!, novoConsumo,
                            object: AdicionaConsumoTask.SalvoListener{
                                override fun salvo() {
                                    if (listaConsumo!!.size  > 0 ){
                                        novoConsumo.idConsumo = listaConsumo!!.last().idConsumo + 1
                                    }
                                    enviaDados(novoConsumo)
                                    finish()
                                }
                            }).execute()
                    }
                }

            }
        }
    }

    private fun carregaListaConsumo(veiculo: Veiculo) {
        CarregaListaConsumoTask(daoConsumo!!, veiculo.idVeiculo,
            object : CarregaListaConsumoTask.CarregadoListener {
                override fun carregado(lista: MutableList<Consumo>) {
                    listaConsumo = lista
                    if (consumo != null) {
                        helperConsumo.preencheConsumo(consumo!!)
                    } else if (listaConsumo!!.isNotEmpty()) {
                        val kmAnterior = findViewById<TextView>(R.id.kmAnterior)
                        kmAnterior.text = listaConsumo!!.last().kmAtual.toString()
                    }
                }
            }).execute()
    }

    override fun onResume() {
        super.onResume()

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
            //veiculoSelecionado.text = "${veiculo.modelo} ${veiculo.anoFabricacao}/${veiculo.anoModelo}"
        } else {
            veiculoSelecionado.text = veiculo.apelido
        }
    }

    private fun verificaCampos(novoConsumo: Consumo) =
        novoConsumo.combustivel == "" || novoConsumo.data == "" || novoConsumo.kmAnterior == 0 ||
                novoConsumo.kmAtual == 0 || novoConsumo.litros == 0.0

    private fun enviaDados(novoConsumo: Consumo) {
        val intentOk = Intent()
        intentOk.putExtra("consumo", novoConsumo)
        setResult(Activity.RESULT_OK, intentOk)
    }

    private fun converteParaDataEUA(novoConsumo: Consumo) {
        val converter = novoConsumo.data.split("/")
        val ano = converter[2]
        val mes = converter[1]
        val dia = converter[0]
        novoConsumo.data = "$ano-$mes-$dia"
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


