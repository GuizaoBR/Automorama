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
                ConsumoFragment()
            }
            else ->{
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