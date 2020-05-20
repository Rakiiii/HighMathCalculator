package com.dev.smurf.highmathcalculator.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
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

    override fun createFragment(position: Int): Fragment
    {
        return fragmentSet[position]
    }

    override fun getItemCount(): Int = fragmentSet.size
}