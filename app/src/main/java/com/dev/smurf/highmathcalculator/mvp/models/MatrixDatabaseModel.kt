package com.dev.smurf.highmathcalculator.mvp.models

import android.content.Context
import androidx.room.Room
import com.dev.smurf.highmathcalculator.DataBase.MatrixDataBase
import com.example.smurf.mtarixcalc.MatrixGroup


class MatrixDatabaseModel(val context: Context)
{

    private var cache : ArrayList<MatrixGroup> = ArrayList()

    private val mMatrixDatabase : MatrixDataBase

    init
    {
        mMatrixDatabase = Room.databaseBuilder( context ,MatrixDataBase::class.java , "matrix_db").build()
    }

    fun insert(matrixGroup: MatrixGroup) = mMatrixDatabase.getMatrixDao().insert(matrixGroup)


    fun delete(matrixGroup: MatrixGroup) = mMatrixDatabase.getMatrixDao().delete(matrixGroup)


    fun selectAll() : List<MatrixGroup>
    {
        var tmp = mMatrixDatabase.getMatrixDao().selectAll()
        cache = ArrayList(tmp)
        return tmp
    }

    fun insertArrayList(ar : ArrayList<MatrixGroup>) = mMatrixDatabase.getMatrixDao().insertArrayList(ar)

    fun addToCache(matrixGroup: MatrixGroup) = cache.add(matrixGroup)

    fun saveCache()
    {
        insertArrayList(cache)
        cache.clear()
    }

    fun deleteDb() = mMatrixDatabase.clearAllTables()
}