package com.example.smurf.mtarixcalc

import androidx.lifecycle.ViewModel


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
    private var valueArrayList : ArrayList<MatrixGroup> = ArrayList()

    fun getList() = valueArrayList

    fun add( value : MatrixGroup) : MatrixGroup
    {
        valueArrayList.add(value)
        return value
    }

    fun updateList( newArrayList : ArrayList<MatrixGroup>)
    {
        valueArrayList = newArrayList
    }

    fun isEmpty() = valueArrayList.isEmpty()


}