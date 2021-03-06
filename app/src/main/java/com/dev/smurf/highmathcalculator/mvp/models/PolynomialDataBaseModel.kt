package com.dev.smurf.highmathcalculator.mvp.models

import android.content.Context
import androidx.room.Room
import com.dev.smurf.highmathcalculator.DataBase.Migration.PolynomialDataBaseMigration_2_3
import com.dev.smurf.highmathcalculator.DataBase.Migration.PolynomialDataBaseMigration_3_4
import com.dev.smurf.highmathcalculator.DataBase.Migration.PolynomialDataBaseMigration_4_5
import com.dev.smurf.highmathcalculator.DataBase.PolynomialDataBase
import com.example.smurf.mtarixcalc.PolynomialGroup


//класс работы с бд полиномов
class PolynomialDataBaseModel(val context: Context)
{

    //кэш базы данных
    private var cache: ArrayList<PolynomialGroup> = ArrayList()

    //объект базы данных
    private val mPolynomialDataBase: PolynomialDataBase =
        Room.databaseBuilder(context, PolynomialDataBase::class.java, "polinom_db").addMigrations(
            PolynomialDataBaseMigration_2_3,PolynomialDataBaseMigration_3_4,PolynomialDataBaseMigration_4_5
        ).build()



    //вставить в базу данных
    fun insert(polynomialGroup: PolynomialGroup) = mPolynomialDataBase.getPolinomDao().insert(polynomialGroup)

    //удалить из базы данных
    fun delete(polynomialGroup: PolynomialGroup) = mPolynomialDataBase.getPolinomDao().delete(polynomialGroup)

    //получить все данные из бд
    fun selectAll(): List<PolynomialGroup> = mPolynomialDataBase.getPolinomDao().selectAll()

    //вставить целый список в бд транзакцией
    fun insertArrayList(ar: ArrayList<PolynomialGroup>) = mPolynomialDataBase.getPolinomDao().insertArrayList(ar)

    //очистка бд
    fun deleteDb() = mPolynomialDataBase.clearAllTables()

    //вставка в кэщ
    fun addToCache(polynomialGroup: PolynomialGroup) = cache.add(polynomialGroup)

    //сохранить кэш в бд
    fun saveCache()
    {
        mPolynomialDataBase.getPolinomDao().insertArrayList(cache)
        cache.clear()
    }

    //удалить из кэш
    fun deleteFromDbCache(polynomialGroup: PolynomialGroup) = cache.remove(polynomialGroup)


}