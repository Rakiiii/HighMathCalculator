package com.dev.smurf.highmathcalculator.DataBase

import androidx.room.*
import com.example.smurf.mtarixcalc.PolynomialGroup

@Dao
interface PolynomialDao
{

    //вставка в бд
    @Insert
    fun insert(PolynomialGroup: PolynomialGroup)


    //удаление из бд
    @Delete
    fun delete(PolynomialGroup: PolynomialGroup)


    //получить все данный из бд
    @Query("SELECT * FROM PolynomialGroup")
    fun selectAll() : List<PolynomialGroup>


    //запись целого списка транзакцией
    @Transaction
    fun insertArrayList( ar : ArrayList<PolynomialGroup>)
    {
        for( i in ar)insert(i)
    }
}