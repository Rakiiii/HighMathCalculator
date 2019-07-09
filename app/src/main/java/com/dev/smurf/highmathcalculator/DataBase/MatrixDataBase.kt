package com.dev.smurf.highmathcalculator.DataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.smurf.mtarixcalc.MatrixGroup



//получение интерфейса для работы с бд
@Database(entities = [MatrixGroup::class] , version = 1)
abstract class MatrixDataBase : RoomDatabase()
{
    abstract fun getMatrixDao() : MatrixDao
}