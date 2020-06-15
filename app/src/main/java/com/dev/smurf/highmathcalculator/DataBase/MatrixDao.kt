package com.dev.smurf.highmathcalculator.DataBase

import androidx.room.*
import com.dev.smurf.highmathcalculator.ui.POJO.MatrixGroup

//todo::move to suspend functions
@Dao
interface MatrixDao
{

    //вставка результатов в бд
    @Insert
    fun insert(MatrixGroup: MatrixGroup)

    //удаление из бд
    @Delete
    fun delete(MatrixGroup: MatrixGroup)

    //получить все данные из бд
    @Query("SELECT *    FROM MatrixGroup")
    fun selectAll() : List<MatrixGroup>

    //записать целый список транзакцией
    @Transaction
    fun insertArrayList(ar : ArrayList<MatrixGroup>)
    {
        for(i in  ar)insert(i)
    }

}