package com.dev.smurf.highmathcalculator.DataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.smurf.mtarixcalc.PolynomialGroup


//получения интрфейса работы с бд
@Database(entities = [PolynomialGroup::class] , version = 2)
abstract class PolynomialDataBase : RoomDatabase()
{
    abstract fun getPolinomDao() : PolynomialDao
}