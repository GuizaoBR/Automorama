package com.guizaotech.automorama

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.guizaotech.automorama.asyncTask.DeletaConsumoTask
import com.guizaotech.automorama.database.AutomoramaDatabase
import com.guizaotech.automorama.database.RoomConsumoDao
import com.guizaotech.automorama.helpers.Codigos_Activity
import com.guizaotech.automorama.modelo.Consumo
import com.guizaotech.automorama.modelo.Veiculo
import kotlinx.android.synthetic.main.activity_detalhes_consumo.*
import java.math.RoundingMode
import java.text.DecimalFormat


class DetalhesConsumoActivity : AppCompatActivity(), Codigos_Activity {
    var posicaoConsumo: Int? = null
    var consumoDAO: RoomConsumoDao? = null
    var listaConsumo: MutableList<Consumo>? = null
    var consumo: Consumo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_consumo)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        consumoDAO = AutomoramaDatabase.getInstance(this).getRoomConsumoDAO()

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

        val parametros = intent.extras
        val veiculo = veiculo(parametros)

        posicaoConsumo = parametros?.getInt("posicaoConsumo")

        textCombustivel.text = resources.getString(R.string.combustivel, consumo!!.combustivel)

//        CarregaListaConsumoTask(consumoDAO!!, veiculo.idVeiculo!!,
//            object :CarregaListaConsumoTask.CarregadoListener{
//                override fun carregado(lista: MutableList<Consumo>) {
//                    listaConsumo = lista
//                    val df = DecimalFormat("#.##")
//                    df.roundingMode = RoundingMode.CEILING
//                    if (posicaoConsumo!! > 0) {
//                        textConsumo.text =
//                            "Consumo: ${df.format(consumo!!.consumoTotal)}Km/L com ${lista[posicaoConsumo!! - 1].combustivel}"
//                    } else {
//                        textConsumo.text = "Consumo: ${df.format(consumo!!.consumoTotal)}Km/L"
//
//                    }
//                }
//            }).execute()




        converteParaDataBR(consumo!!, textData)


        textPercorrido.text = "Percorrido: " + (consumo!!.kmAtual!!.toDouble() - consumo!!.kmAnterior!!.toDouble()).toString() + "Km"


        if (consumo!!.tanqueCompleto == 1) {
            textTanqueAbastecido.text = "Tanque abastecido por completo"
        } else {
            textTanqueAbastecido.text = "Tanque não abastecido por completo"
        }


        btEditar.setOnClickListener {
            val pagEditar = Intent(this@DetalhesConsumoActivity, FormConsumoActivity::class.java)
            pagEditar.putExtra("veiculo", veiculo)
            pagEditar.putExtra("consumo", consumo)
            startActivityForResult(pagEditar, CODIGO_ALTERA)


        }
    }

    private fun confimarDeletar(consumo: Consumo) {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle("ATENÇÃO")

        builder.setMessage(
            "Tem certeza que deseja deletar este abastecimento?"
        )

        builder.setPositiveButton("Sim") { dialog, which ->
            DeletaConsumoTask(consumoDAO!!, consumo,
                object : DeletaConsumoTask.FinalizaListener{
                    override fun finaliza() {
                        val intentOk = Intent()
                        intentOk.putExtra("consumoDeletado", consumo)
                        intentOk.putExtra("consumoPosicao", posicaoConsumo as Int)
                        setResult(Activity.RESULT_OK, intentOk)
                        //Toast.makeText(this, "Deletado", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }).execute()


        }
        builder.setNegativeButton("Não") { dialog, which ->
            dialog.cancel()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun converteParaDataBR(consumo: Consumo, data: TextView) {
        val converter = consumo.data!!.split("-")
        val ano = converter[0]
        val mes = converter[1]
        val dia = converter[2]
        data.text = "Data: $dia/$mes/$ano"
    }


    private fun preencheDados(): Pair<Veiculo, Consumo> {

        val parametros = intent.extras
        val veiculo = veiculo(parametros)

        consumo = parametros?.getSerializable("consumo") as Consumo
        posicaoConsumo = parametros.getInt("posicaoConsumo", -1)

        //val detalhesConsumo = daoConsumo.pegarConsumo(idConsumo)

        textCombustivel.text = "Combustível: ${consumo!!.combustivel}"

        //val daoConsumo = AutomoramaDatabase.getInstance(this).getRoomConsumoDAO()
        //val listaConsumo = daoConsumo.todos(veiculo.idVeiculo)

//        CarregaListaConsumoTask(consumoDAO!!, veiculo.idVeiculo!!,
//            object :CarregaListaConsumoTask.CarregadoListener{
//                override fun carregado(lista: MutableList<Consumo>) {
//                    listaConsumo = lista
//                    val df = DecimalFormat("#.##")
//                    df.roundingMode = RoundingMode.CEILING
//                    if (posicaoConsumo!! > 0) {
//                        textConsumo.text =
//                            "Consumo: ${df.format(consumo!!.consumoTotal)}Km/L com ${listaConsumo!![posicaoConsumo!! - 1].combustivel}"
//                    } else {
//                        textConsumo.text = "Consumo: ${df.format(consumo!!.consumoTotal)}Km/L"
//
//                    }
//                }
//            }).execute()
//




        converteParaDataBR(consumo!!, textData)





        textPercorrido.text =
            "Percorrido: " + (consumo!!.kmAtual!!.toDouble() - consumo!!.kmAnterior!!.toDouble()).toString() + "Km"


        if (consumo!!.tanqueCompleto == 1) {
            textTanqueAbastecido.text = "Tanque abastecido por completo"
        } else {
            textTanqueAbastecido.text = "Tanque não abastecido por completo"
        }
        return Pair(veiculo, consumo!!)
    }

    private fun veiculo(parametros: Bundle?): Veiculo {
        val veiculo = parametros!!.getSerializable("veiculo") as Veiculo




        if (veiculo.apelido == null) {
            veiculoSelecionado.text =
                veiculo.modelo + " " + veiculo.anoFabricacao + "/" + veiculo.anoModelo
        } else {
            veiculoSelecionado.text = veiculo.apelido
        }
        return veiculo
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //val parametros = intent.extras
        //val veiculo = veiculo(parametros)
        //val listaConsumo = consumoDAO!!.todos(veiculo.idVeiculo)
        //val adapter : ListaConsumoRecyclerView? = ListaConsumoRecyclerView(listaConsumo, this)


        if (requestCode == CODIGO_ALTERA && resultCode == Activity.RESULT_OK){
            consumo = data!!.getSerializableExtra("consumo") as Consumo

            mandaParaListaConsumoActivity(consumo!!)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun mandaParaListaConsumoActivity(
        consumo: Consumo
    ) {
        val alteraHomeConsumo = Intent()
        alteraHomeConsumo.putExtra("consumoAlterado", consumo)
        alteraHomeConsumo.putExtra("consumoPosicao", posicaoConsumo as Int)
        //adapter!!.altera(consumo, posicaoConsumo as Int)
        setResult(Activity.RESULT_OK, alteraHomeConsumo)
    }

   /* private fun altera(
        consumo: Consumo,
        veiculo: Veiculo
    ) {
        val daoConsumo = DaoConsumo(this)
        daoConsumo.editarConsumo(consumo, veiculo.idVeiculo as Long)
        daoConsumo.close()
    }*/
}
