package com.guizaotech.automorama.helpers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import com.guizaotech.automorama.FormVeiculoActivity
import com.guizaotech.automorama.R
import com.guizaotech.automorama.modelo.Veiculo




class HelperVeiculo(activity: FormVeiculoActivity) {

    private val campoMarca : EditText = activity.findViewById(R.id.marca) as EditText
    private val campoModelo : EditText = activity.findViewById(R.id.modelo) as EditText
    private val campoAnoFabricacao : EditText = activity.findViewById(R.id.anoFabricacao) as EditText
    private val campoAnoModelo : EditText = activity.findViewById(R.id.anoModelo) as EditText
    private val campoPlaca : EditText = activity.findViewById(R.id.placa) as EditText
    private val campoApelido : EditText = activity.findViewById(R.id.apelido) as EditText
    private val campoImagem : ImageView = activity.findViewById(R.id.imageVeiculo) as ImageView
    //private var veiculo : Veiculo = Veiculo()
    private val context = activity.applicationContext


    fun getVeiculo(veiculo: Veiculo): Veiculo{
        veiculo.marca = campoMarca.text.toString()
        veiculo.modelo = campoModelo.text.toString()
        if (campoAnoFabricacao.text.toString() != ""){
            veiculo.anoFabricacao = campoAnoFabricacao.text.toString().toInt()
        }
        if (campoAnoModelo.text.toString() != ""){
            veiculo.anoModelo = campoAnoModelo.text.toString().toInt()
        }

        veiculo.placa = campoPlaca.text.toString()

        if (campoApelido.text.toString() != "") {
            veiculo.apelido = campoApelido.text.toString()
        } else{
            veiculo.apelido = ""
        }
        if (campoImagem.tag != null){
            veiculo.caminhoImagem = campoImagem.tag.toString()
        } else {
            veiculo.caminhoImagem = ""
        }
        return veiculo
    }

    fun preencheVeiculo(veiculo: Veiculo){
        campoMarca.setText(veiculo.marca)
        campoModelo.setText(veiculo.modelo)
        campoAnoFabricacao.setText(veiculo.anoFabricacao.toString())
        campoAnoModelo.setText(veiculo.anoModelo.toString())
        campoPlaca.setText(veiculo.placa)
        if(veiculo.apelido != ""){
            campoApelido.setText(veiculo.apelido)
        }
        if (campoImagem.tag == ""){
            carregaImagem(veiculo.caminhoImagem)
        }
        //this.veiculo = veiculo
    }


    fun carregaImagem(caminhoFoto: String?) {
        if (caminhoFoto != null){
            val helperImagem = CarregaImagem()

            campoImagem.setImageBitmap(helperImagem.giraImagem(caminhoFoto, 500, 500))
            campoImagem.scaleType = ImageView.ScaleType.FIT_XY
            campoImagem.tag = caminhoFoto
        } else {
            campoImagem.tag = null
        }

    }

    private fun validaAnoModelo() {
        campoAnoModelo.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                TODO("Not yet implemented")
            }

            override fun afterTextChanged(p0: Editable?) {
                if(p0?.length != 4)
                    campoAnoModelo.error = context.getString(R.string.erro_formato_ano)
                else
                    campoAnoModelo.error = null
            }
        })
    }


    private fun temCamposVazios() =
        campoAnoFabricacao.text.toString() == "" || campoAnoModelo.text.toString() == "" ||
                campoMarca.text.toString() == "" ||
                campoModelo.text.toString() == "" || campoPlaca.text.toString() == ""

    private fun dataEhValida(data: String) =
        data.length == 4

    fun ehValido() : Boolean {
        var ehValido: Boolean = true
        if(temCamposVazios()) {
            ehValido = false
        } else {
//            if (!verificaPlaca(listaVeiculo)) {
//                campoPlaca.error = context.getString(R.string.placa_cadastrada_mensagem)
//                ehValido = false
//            } else {
//                campoPlaca.error = null
//            }
            if(!dataEhValida(campoAnoModelo.text.toString())){
                campoAnoModelo.error = context.getString(R.string.ano_modelo_invalido)
                ehValido = false
            } else {
                campoAnoModelo.error = null
            }
            if(!dataEhValida(campoAnoFabricacao.text.toString())){
                campoAnoFabricacao.error = context.getString(R.string.ano_fabricacao_invalido)
                ehValido = false
            } else {
                campoAnoFabricacao.error = null
            }

        }
        return ehValido
    }


}