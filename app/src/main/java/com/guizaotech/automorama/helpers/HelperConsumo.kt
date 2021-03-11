package com.guizaotech.automorama.helpers

import android.widget.*
import com.guizaotech.automorama.FormConsumoActivity
import com.guizaotech.automorama.R
import com.guizaotech.automorama.modelo.Consumo

class HelperConsumo(activityForm: FormConsumoActivity)  {

    private val campoKmAnterior: EditText = activityForm.findViewById(R.id.kmAnterior)
    private val campoKmAtual: EditText = activityForm.findViewById(R.id.kmAtual)
    private var campoCombustivel: Spinner = activityForm.findViewById(R.id.combustivel)
    private val campoData: EditText = activityForm.findViewById(R.id.data)
    private val campoLitros: EditText = activityForm.findViewById(R.id.litros)
    private val campoTanqueCheio : CheckBox = activityForm.findViewById(R.id.checkTanque)
    private  val campoValor: EditText = activityForm.findViewById(R.id.valor)
    private val context = activityForm.applicationContext

    private var consumo: Consumo = Consumo()


    fun temVazio(): Boolean {
        var temVazio: Boolean = false
        if (campoData.text.toString() == "") {
            campoData.error = context.getString(R.string.campo_obrigatorio)
            temVazio = true
        } else {
            campoData.error = null
        }
        if (campoKmAnterior.text.toString() == "") {
            campoKmAnterior.error = context.getString(R.string.campo_obrigatorio)
            temVazio = true
        } else {
            campoKmAnterior.error = null
        }
        if(campoKmAtual.text.toString() == "") {
            campoKmAtual.error = context.getString(R.string.campo_obrigatorio)
            temVazio = true
        } else {
           campoKmAtual.error = null
        }
        if(campoLitros.text.toString() == "") {
            campoLitros.error = context.getString(R.string.campo_obrigatorio)
            temVazio = true
        } else {
            campoLitros.error = null
        }
        if(campoValor.text.toString() == ""){
            campoValor.error = context.getString(R.string.campo_obrigatorio)
            temVazio = true
        } else {
            campoValor.error = null
        }
        return  temVazio
    }
    fun ehValido(): Boolean {
        var ehValido: Boolean = true
        if (temVazio()) {
            ehValido = false
            Toast.makeText(
                context,
                context.getString(R.string.preencha_campos_obrigatorios),
                Toast.LENGTH_SHORT
            ).show()
        } else if (KmAnteriorEhMaiorKmAtual()) {
            ehValido = false
            Toast.makeText(context,context.getString(R.string.erro_kmAnterior_maior_kmAtual), Toast.LENGTH_LONG).show()
        }
        return ehValido
    }

    private fun KmAnteriorEhMaiorKmAtual(): Boolean {
        var ehMaior: Boolean = false
        if(campoKmAtual.text.toString().toInt() <= campoKmAnterior.text.toString().toInt()) {
            ehMaior = true
            campoKmAnterior.error = context.getString(R.string.erro_kmAnterior_maior_kmAtual)
            campoKmAtual.error = context.getString(R.string.erro_kmAnterior_maior_kmAtual)
        }
        return ehMaior
    }

    fun getConsumo(): Consumo {
        consumo.kmAnterior = campoKmAnterior.text.toString().toInt()
        consumo.kmAtual = campoKmAtual.text.toString().toInt()
        consumo.combustivel = campoCombustivel.selectedItem.toString()
        consumo.data = campoData.text.toString()
        consumo.litros = campoLitros.text.toString().toDouble()
        consumo.valor = campoValor.text.toString().toDouble()

        if(consumo.kmAnterior == 0 || consumo.kmAtual == 0 || consumo.litros == 0.0){
            consumo.consumoTotal = 0.0
        } else {
            consumo.consumoTotal = (campoKmAtual.text.toString().toDouble() - campoKmAnterior.text.toString().toDouble()) /
                    campoLitros.text.toString().toDouble()
        }
        if (campoTanqueCheio.isChecked){
            consumo.tanqueCompleto = 1
        } else {
            consumo.tanqueCompleto = 0
        }

        return consumo
    }

    fun preencheConsumo(consumo: Consumo){
        campoKmAnterior.setText(consumo.kmAnterior.toString())
        campoKmAtual.setText(consumo.kmAtual.toString())
        campoLitros.setText(consumo.litros.toString())
        campoValor.setText(consumo.valor.toString())
        val converter = consumo.data.split("-")
        val ano = converter[0]
        val mes = converter[1]
        val dia = converter[2]
        val data = context.resources.getString(R.string.data, dia, mes, ano)
        campoData.setText(data)
        //campoData.setText("$dia/$mes/$ano")


        campoCombustivel.setSelection((campoCombustivel.adapter as? ArrayAdapter<String>)!!.getPosition(consumo.combustivel))

        campoTanqueCheio.isChecked = consumo.tanqueCompleto == 1
        this.consumo = consumo

    }
    
}