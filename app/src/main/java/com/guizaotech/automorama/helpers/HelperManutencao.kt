package com.guizaotech.automorama.helpers

import android.widget.*
import com.guizaotech.automorama.FormManutencaoActivity
import com.guizaotech.automorama.R
import com.guizaotech.automorama.modelo.Manutencao
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class HelperManutencao(activityForm: FormManutencaoActivity)  {

    private val campoTitulo: EditText = activityForm.findViewById(R.id.inputTitulo)
    private val campoDescricao: EditText = activityForm.findViewById(R.id.inputDescricao)
    private val campoData: EditText = activityForm.findViewById(R.id.data)
    private val campoFeito : CheckBox = activityForm.findViewById(R.id.checkFeito)
    private  val campoValor: EditText = activityForm.findViewById(R.id.inputValor)
    private val context = activityForm.applicationContext

    private var manutencao: Manutencao = Manutencao()


    fun temVazio(): Boolean {
        var temVazio: Boolean = false
        if (campoData.text.toString() == "") {
            campoData.error = context.getString(R.string.campo_obrigatorio)
            temVazio = true
        } else {
            campoData.error = null
        }
        if (campoTitulo.text.toString() == "") {
            campoTitulo.error = context.getString(R.string.campo_obrigatorio)
            temVazio = true
        } else {
            campoTitulo.error = null
        }
        if (campoFeito.isChecked){
            if(campoValor.text.toString() == ""){
                campoValor.error = context.getString(R.string.campo_valor_manutencao_obrigatorio)
                temVazio = true
            } else {
                campoValor.error = null
            }
        }

        return  temVazio
    }
    fun dataValida(): Boolean{
        var ehValido: Boolean = true
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val currentDate = sdf.format(Date())

        if(campoData.text.toString() > currentDate.toString() && campoFeito.isChecked) {
            ehValido = false
            campoData.error = context.getString(R.string.form_manutencao_data_invalida)
        } else {
            campoData.error = null
        }
        return  ehValido
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
        }
        else if (!dataValida()){
            Toast.makeText(
                context,
                context.getString(R.string.verifique_erros),
                Toast.LENGTH_SHORT
            ).show()
        }
        return ehValido
    }


    fun getManutencao(): Manutencao {
        manutencao.titulo = campoTitulo.text.toString()
        manutencao.descricao = campoDescricao.text.toString()
        manutencao.data = campoData.text.toString()
        manutencao.valor = campoValor.text.toString().toFloat()
        manutencao.feito = campoFeito.isChecked

        return manutencao
    }

    fun preencheManutencao(manutecao: Manutencao){
        campoTitulo.setText(manutecao.titulo.toString())
        campoDescricao.setText(manutecao.descricao.toString())
        campoValor.setText(manutecao.valor.toString())
        val converter = manutecao.data.split("-")
        val ano = converter[0]
        val mes = converter[1]
        val dia = converter[2]
        val data = context.resources.getString(R.string.data, dia, mes, ano)
        campoData.setText(data)

        campoFeito.isChecked = manutecao.feito

        this.manutencao = manutecao

    }

    
}