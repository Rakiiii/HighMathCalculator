package com.example.smurf.mtarixcalc

import androidx.lifecycle.ViewModel
import com.dev.smurf.highmathcalculator.ui.POJO.MatrixGroup


//View model для Recycler view.list
class MatrixRecyclerViewModel : ViewModel()
{
    private var valueArrayList : ArrayList<MatrixGroup> = ArrayList()

    fun getList() = valueArrayList

    fun add( value : MatrixGroup) : MatrixGroup
    {
        valueArrayList.add(value)
        //Log.d("RVM@","add")
        return value
    }

    fun updateList( newArrayList : ArrayList<MatrixGroup>)
    {
        valueArrayList = newArrayList
    }

    fun isEmpty() = valueArrayList.isEmpty()

    fun deleteItem(matrixGroup: MatrixGroup) = valueArrayList.remove(matrixGroup)


}