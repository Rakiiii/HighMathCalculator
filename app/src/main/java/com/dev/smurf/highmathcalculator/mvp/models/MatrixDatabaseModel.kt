package com.dev.smurf.highmathcalculator.mvp.models

import android.content.Context
import androidx.room.Room
import com.dev.smurf.highmathcalculator.DataBase.MatrixDataBase
import com.dev.smurf.highmathcalculator.ui.POJO.MatrixGroup


//модель работы с базой данных для матриц
class MatrixDatabaseModel(val context: Context)
{

    //кэш базы данных
    private var cache : ArrayList<MatrixGroup> = ArrayList()
    //база данных
    private val mMatrixDatabase : MatrixDataBase =
        Room.
            databaseBuilder( context ,MatrixDataBase::class.java , "matrix_db").
            fallbackToDestructiveMigration().
            //allowMainThreadQueries().
            build()


    //вставка в бд
    fun insert(matrixGroup: MatrixGroup) = mMatrixDatabase.getMatrixDao().insert(matrixGroup)

    //удаление из бд
    fun delete(matrixGroup: MatrixGroup) = mMatrixDatabase.getMatrixDao().delete(matrixGroup)

    //получение всех элементов бд
    fun selectAll() : List<MatrixGroup> = mMatrixDatabase.getMatrixDao().selectAll()


    //втсавка целого листа транзакцией
    fun insertArrayList(ar : ArrayList<MatrixGroup>) = mMatrixDatabase.getMatrixDao().insertArrayList(ar)


    //добавить в кэш
    fun addToCache(matrixGroup: MatrixGroup) = cache.add(matrixGroup)

    //сохранить кэш в бд и очистить кэш
    fun saveCache()
    {
        mMatrixDatabase.getMatrixDao().insertArrayList(cache)
        cache.clear()
    }

    //удаление из кэша бд
    fun deleteFromDbCache(matrixGroup: MatrixGroup) = cache.remove(matrixGroup)

    //очистить бд
    fun deleteDb() = mMatrixDatabase.clearAllTables()
}