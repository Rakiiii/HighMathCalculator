package com.example.smurf.mtarixcalc

import androidx.lifecycle.ViewModel


//View model для recyclerView.list
class PolynomialRecyclerViewModel : ViewModel()
{
    private var valueArrayList : ArrayList<PolynomialGroup> = ArrayList()

    fun getList() = valueArrayList

    fun add( value : PolynomialGroup) : PolynomialGroup
    {
        valueArrayList.add(value)
        return value
    }

    fun updateList( newArrayList : ArrayList<PolynomialGroup>)
    {
        valueArrayList = newArrayList
    }

    fun isEmpty() = valueArrayList.isEmpty()

    fun deleteItem(polynomialGroup: PolynomialGroup) = valueArrayList.remove(polynomialGroup)


}