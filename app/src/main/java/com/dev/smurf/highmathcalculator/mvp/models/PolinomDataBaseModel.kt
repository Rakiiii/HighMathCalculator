package com.dev.smurf.highmathcalculator.mvp.models

import android.content.Context
import androidx.room.Room
import com.dev.smurf.highmathcalculator.DataBase.PolinomDataBase
import com.example.smurf.mtarixcalc.PolinomGroup


//класс работы с бд полиномов
class PolinomDataBaseModel(val context: Context)
{

    //объект базы данных
    private val mPolinomDataBase : PolinomDataBase

    init
    {
        //инициализация базы данных
        mPolinomDataBase = Room.databaseBuilder(context , PolinomDataBase::class.java , "polinom_db").build()
    }

    //вставить в базу данных
    fun insert(polinomGroup: PolinomGroup) = mPolinomDataBase.getPolinomDao().insert(polinomGroup)

    //удалить из базы данных
    fun delete(polinomGroup: PolinomGroup) = mPolinomDataBase.getPolinomDao().delete(polinomGroup)

    //получить все данные из бд
    fun selectAll() : List<PolinomGroup> = mPolinomDataBase.getPolinomDao().selectAll()
}