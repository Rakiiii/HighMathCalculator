package com.dev.smurf.highmathcalculator.mvp.models

import android.content.Context
import androidx.room.Room
import com.dev.smurf.highmathcalculator.DataBase.PolinomDataBase
import com.example.smurf.mtarixcalc.PolinomGroup


//класс работы с бд полиномов
class PolinomDataBaseModel(val context: Context)
{

    //кэш базы данных
    private var cache : ArrayList<PolinomGroup> = ArrayList()

    //объект базы данных
    private val mPolinomDataBase : PolinomDataBase =
        Room.databaseBuilder(context , PolinomDataBase::class.java , "polinom_db").build()


    //вставить в базу данных
    fun insert(polinomGroup: PolinomGroup) = mPolinomDataBase.getPolinomDao().insert(polinomGroup)

    //удалить из базы данных
    fun delete(polinomGroup: PolinomGroup) = mPolinomDataBase.getPolinomDao().delete(polinomGroup)

    //получить все данные из бд
    fun selectAll() : List<PolinomGroup> = mPolinomDataBase.getPolinomDao().selectAll()

    //вставить целый список в бд транзакцией
    fun insertArrayList( ar : ArrayList<PolinomGroup>) = mPolinomDataBase.getPolinomDao().insertArrayList(ar)

    //очистка бд
    fun deleteDb() = mPolinomDataBase.clearAllTables()

    //вставка в кэщ
    fun addToCache(polinomGroup: PolinomGroup) = cache.add(polinomGroup)

    //сохранить кэш в бд
    fun saveCache()
    {
        mPolinomDataBase.getPolinomDao().insertArrayList(cache)
        cache.clear()
    }

    //удалить из кэш
    fun deleteFromDbCache(polinomGroup: PolinomGroup) = cache.remove(polinomGroup)


}