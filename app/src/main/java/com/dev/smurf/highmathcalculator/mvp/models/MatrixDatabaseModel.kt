package com.dev.smurf.highmathcalculator.mvp.models

import android.content.Context
import androidx.room.Room
import com.dev.smurf.highmathcalculator.DataBase.MatrixDataBase
import com.example.smurf.mtarixcalc.MatrixGroup


//модель работы с базой данных для матриц
class MatrixDatabaseModel(val context: Context)
{

    //кэш базы данных
    private var cache : ArrayList<MatrixGroup> = ArrayList()

    //база данных
    private val mMatrixDatabase : MatrixDataBase

    init
    {
        //инициализируем базу дынных
        mMatrixDatabase = Room.databaseBuilder( context ,MatrixDataBase::class.java , "matrix_db").build()
    }

    //вставка в бд
    fun insert(matrixGroup: MatrixGroup) = mMatrixDatabase.getMatrixDao().insert(matrixGroup)

    //удаление из бд
    fun delete(matrixGroup: MatrixGroup) = mMatrixDatabase.getMatrixDao().delete(matrixGroup)

    //получение всех элементов бд
    fun selectAll() : List<MatrixGroup>
    {
        var tmp = mMatrixDatabase.getMatrixDao().selectAll()
        cache = ArrayList(tmp)
        return tmp
    }

    //втсавка целого листа транзакцией
    fun insertArrayList(ar : ArrayList<MatrixGroup>) = mMatrixDatabase.getMatrixDao().insertArrayList(ar)


    //добавить в кэш
    fun addToCache(matrixGroup: MatrixGroup) = cache.add(matrixGroup)

    //сохранить кэш в бд и очистить кэш
    fun saveCache()
    {
        insertArrayList(cache)
        cache.clear()
    }

    //очистить бд
    fun deleteDb() = mMatrixDatabase.clearAllTables()
}