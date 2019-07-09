package com.dev.smurf.highmathcalculator.DataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.smurf.mtarixcalc.PolinomGroup

@Dao
interface PolinomDao
{
    @Insert
    fun insert(PolinomGroup: PolinomGroup)

    @Delete
    fun delete(PolinomGroup: PolinomGroup)

    @Query("SELECT * FROM PolinomGroup")
    fun selectAll() : List<PolinomGroup>

}