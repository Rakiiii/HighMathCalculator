package com.dev.smurf.highmathcalculator.mvp.models

import android.content.Context
import androidx.room.Room
import com.dev.smurf.highmathcalculator.DataBase.PolinomDataBase
import com.example.smurf.mtarixcalc.PolinomGroup

class PolinomDataBaseModel(val context: Context)
{

    private val mPolinomDataBase : PolinomDataBase

    init
    {

        mPolinomDataBase = Room.databaseBuilder(context , PolinomDataBase::class.java , "polinom_db").build()
    }

    fun insert(polinomGroup: PolinomGroup) = mPolinomDataBase.getPolinomDao().insert(polinomGroup)

    fun delete(polinomGroup: PolinomGroup) = mPolinomDataBase.getPolinomDao().delete(polinomGroup)

    fun selectAll() : List<PolinomGroup> = mPolinomDataBase.getPolinomDao().selectAll()
}