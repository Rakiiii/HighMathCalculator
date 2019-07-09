package com.dev.smurf.highmathcalculator.DataBase

import androidx.room.*
import com.example.smurf.mtarixcalc.PolinomGroup

@Dao
interface PolinomDao
{

    //вставка в бд
    @Insert
    fun insert(PolinomGroup: PolinomGroup)


    //удаление из бд
    @Delete
    fun delete(PolinomGroup: PolinomGroup)


    //получить все данный из бд
    @Query("SELECT * FROM PolinomGroup")
    fun selectAll() : List<PolinomGroup>


    //запись целого списка транзакцией
    @Transaction
    fun insertArrayList( ar : ArrayList<PolinomGroup>)
    {
        for( i in ar)insert(i)
    }
}