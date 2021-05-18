package com.guizaotech.automorama

import android.content.Intent
import android.os.Bundle

import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.guizaotech.automorama.helpers.Codigos_Activity
import com.guizaotech.automorama.modelo.Veiculo
import com.guizaotech.automorama.ui.main.SectionsPagerAdapter
import kotlinx.android.synthetic.main.activity_detalhes_consumo.toolbar
import kotlinx.android.synthetic.main.activity_gastos.*

class GastosActivity : AppCompatActivity(), Codigos_Activity {
    private var veiculo: Veiculo? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gastos)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this, fm =  supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)

        val parametros = intent.extras
        veiculo = parametros?.getSerializable("veiculo") as Veiculo

        val veiculoSelecionado = textVeiculoTitulo
        if (veiculo!!.apelido == "") {
            veiculoSelecionado.text = veiculo!!.modelo + " " + veiculo!!.anoFabricacao + "/" + veiculo!!.anoModelo
        } else {
            veiculoSelecionado.text = veiculo!!.apelido
        }
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab!!.position) {
                    0 -> {

                        fab.setOnClickListener {
                            val paginaConsumo = Intent(this@GastosActivity, FormConsumoActivity::class.java)
                            paginaConsumo.putExtra("veiculo", veiculo)
                            startActivityForResult(paginaConsumo, CODIGO_INSERE)
                        }

                    }
                    else -> {
                        fab.setOnClickListener {
                            val paginaManutencao = Intent(this@GastosActivity, FormManutencaoActivity::class.java)
                            val dadosVeiculo =if(veiculo!!.apelido != "") "${veiculo!!.apelido}|${veiculo!!.idVeiculo}" else "${veiculo!!.modelo} ${veiculo!!.anoFabricacao}/${veiculo!!.anoModelo}|${veiculo!!.idVeiculo}"
                            paginaManutencao.putExtra("veiculo", dadosVeiculo)
                            startActivityForResult(paginaManutencao, CODIGO_INSERE)
                        }

                    }
                }
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabReselected(p0: TabLayout.Tab?) {
            }


        })

        tabs.setupWithViewPager(viewPager)

    }
}