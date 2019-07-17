package com.example.smurf.mtarixcalc

import androidx.lifecycle.ViewModel


//View model для recyclerView.list
class PolinomRecyclerViewModel : ViewModel()
{
    private var valueArrayList : ArrayList<PolinomGroup> = ArrayList()

    fun getList() = valueArrayList

    fun add( value : PolinomGroup) : PolinomGroup
    {
        valueArrayList.add(value)
        return value
    }

    fun updateList( newArrayList : ArrayList<PolinomGroup>)
    {
        valueArrayList = newArrayList
    }

    fun isEmpty() = valueArrayList.isEmpty()

    fun deleteItem(polinomGroup: PolinomGroup) = valueArrayList.remove(polinomGroup)


}