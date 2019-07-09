package com.dev.smurf.highmathcalculator.DataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.smurf.mtarixcalc.PolinomGroup


//получения интрфейса работы с бд
@Database(entities = [PolinomGroup::class] , version = 1)
abstract class PolinomDataBase : RoomDatabase()
{
    abstract fun getPolinomDao() : PolinomDao
}