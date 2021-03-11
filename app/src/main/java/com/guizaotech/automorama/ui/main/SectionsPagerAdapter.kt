package com.guizaotech.automorama.ui.main

import android.content.Context
import android.content.Intent
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.guizaotech.automorama.*
import com.guizaotech.automorama.helpers.Codigos_Activity
import com.guizaotech.automorama.modelo.Veiculo
import kotlinx.android.synthetic.main.activity_gastos.*

private val TAB_TITLES = arrayOf(
    R.string.abastecimentos,
    R.string.manutencao
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val activity: Context,  fm: FragmentManager ) :
    FragmentPagerAdapter(fm), Codigos_Activity  {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                /*activity.fab.setOnClickListener {
                    val paginaConsumo = Intent(activity, FormConsumoActivity::class.java)
                    paginaConsumo.putExtra("veiculo", veiculo)
                    startActivityForResult(activity, paginaConsumo, CODIGO_INSERE, null)
                }*/
                ConsumoFragment()

            }
            else ->{
                /*activity.fab.setOnClickListener{
                    val paginaManutencao = Intent(activity, FormManutencaoActivity::class.java)
                    val dadosVeiculo =if(veiculo!!.apelido != "") "${veiculo!!.apelido}|${veiculo!!.idVeiculo}" else "${veiculo!!.modelo} ${veiculo!!.anoFabricacao}/${veiculo!!.anoModelo}|${veiculo!!.idVeiculo}"
                    paginaManutencao.putExtra("veiculo", dadosVeiculo)
                    startActivityForResult(activity, paginaManutencao, CODIGO_INSERE, null)
                }*/
                ManutencaoFragment()
            }

        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        //return PlaceholderFragment.newInstance(position + 1)
    }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return activity.resources.getString(TAB_TITLES[position])
    }
    
    override fun getCount(): Int {
        // Show 2 total pages.
        return 2
    }
}