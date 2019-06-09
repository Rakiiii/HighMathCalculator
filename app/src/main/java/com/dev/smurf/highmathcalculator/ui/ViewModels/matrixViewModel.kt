package com.example.smurf.mtarixcalc

import android.arch.lifecycle.ViewModel


/*class recyclerViewModel<T> : ViewModel()
{
    private var valueArrayList : ArrayList<T> = ArrayList()

    fun getList() = valueArrayList

    fun add( value : T) : T
    {
        valueArrayList.add(value)
        return value
    }

    fun updateList( newArrayList : ArrayList<T>)
    {
        valueArrayList = newArrayList
    }

    fun isEmpty() = valueArrayList.isEmpty()


}*/

class MatrixRecyclerViewModel : ViewModel()
{
    private var valueArrayList : ArrayList<matrixGroup> = ArrayList()

    fun getList() = valueArrayList

    fun add( value : matrixGroup) : matrixGroup
    {
        valueArrayList.add(value)
        return value
    }

    fun updateList( newArrayList : ArrayList<matrixGroup>)
    {
        valueArrayList = newArrayList
    }

    fun isEmpty() = valueArrayList.isEmpty()


}