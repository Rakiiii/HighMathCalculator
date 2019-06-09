package com.example.smurf.mtarixcalc

import android.arch.lifecycle.ViewModel

class PolinomRecyclerViewModel : ViewModel()
{
    private var valueArrayList : ArrayList<polGroup> = ArrayList()

    fun getList() = valueArrayList

    fun add( value : polGroup) : polGroup
    {
        valueArrayList.add(value)
        return value
    }

    fun updateList( newArrayList : ArrayList<polGroup>)
    {
        valueArrayList = newArrayList
    }

    fun isEmpty() = valueArrayList.isEmpty()


}