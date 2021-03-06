package com.dev.smurf.highmathcalculator.DataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dev.smurf.highmathcalculator.ui.POJO.MatrixGroup



//получение интерфейса для работы с бд
@Database(entities = [MatrixGroup::class] , version = 2)
abstract class MatrixDataBase : RoomDatabase()
{
    abstract fun getMatrixDao() : MatrixDao
}