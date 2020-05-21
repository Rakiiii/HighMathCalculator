package com.dev.smurf.highmathcalculator.ui.adapters.ViewPagersAdapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dev.smurf.highmathcalculator.ui.fragments.fragmentInterfaces.Settingable
import moxy.MvpAppCompatFragment

class ViewPagerFragmentStateAdapter(activity : FragmentActivity) : FragmentStateAdapter(activity)
{
    private var fragmentSet = mutableListOf<MvpAppCompatFragment>()

    fun addNewFragment( f : MvpAppCompatFragment)
    {
        fragmentSet.add(f)
    }

    fun setFragmentSet( fs : MutableList<MvpAppCompatFragment>){
        fragmentSet = fs
    }

    fun callUpdateSettings()
    {
        for( fragment in fragmentSet)
        {
            if (fragment is Settingable)
            {
                fragment.updateSettings()
            }
        }
    }

    override fun createFragment(position: Int): Fragment
    {
        return fragmentSet[position]
    }

    override fun getItemCount(): Int = fragmentSet.size
}