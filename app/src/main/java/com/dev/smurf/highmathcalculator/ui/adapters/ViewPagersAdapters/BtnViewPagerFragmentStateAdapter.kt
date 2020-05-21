package com.dev.smurf.highmathcalculator.ui.adapters.ViewPagersAdapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class BtnViewPagerFragmentStateAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity)
{
    private var fragmentSet = mutableListOf<Fragment>()

    fun addNewFragment(f: Fragment) = fragmentSet.add(f)
    fun setNewFragmentSet(fs: MutableList<Fragment>) = apply { fragmentSet = fs }

    override fun createFragment(position: Int): Fragment
    {
        if (position >= 0)
        {
            return if (position < fragmentSet.size) fragmentSet[position]
            else fragmentSet[0]
        }
        else throw WrongFragmentPositionException()
    }

    override fun getItemCount(): Int
    {
        return fragmentSet.size
    }

    class WrongFragmentPositionException : Exception()
    {
        override val message: String?
            get() = super.message + "Wrong fragment position at adapter"
    }
}