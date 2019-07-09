package com.dev.smurf.highmathcalculator.mvp.models

import android.content.Context
import android.content.SharedPreferences
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.doAsync

class SettingsModel(val context: Context)
{

    //SharedPreferences с настройками
    private val mPrefs : SharedPreferences = context.defaultSharedPreferences



    //получить настройки сохранения матриц
    fun getMatrixConsisten() : Boolean
    {
        return mPrefs.getBoolean("Matrix" , false)
    }


    //получения настройки сохранения полинома
    fun getPolinomConsisten() : Boolean
    {
        return mPrefs.getBoolean("Polinom" , false)
    }

    //включение сохранения матриц
    fun onMatrixSaving()
    {
        doAsync {
            //меняем на true в  xml файле
            mPrefs.edit().putBoolean("Matrix",true).apply()
        }
    }

    //выключение сохранеия матриц
    fun offMatrixSaving()
    {
        doAsync {
            mPrefs.edit().putBoolean("Matrix",false).apply()
        }
    }

    //включение сохранения матриц
    fun onPolinomSaving()
    {
        doAsync {
            mPrefs.edit().putBoolean("Polinom",true).apply()
        }
    }

    //выключение сохранения матриц
    fun offPolinomSaving()
    {
        doAsync {
            mPrefs.edit().putBoolean("Polinom",false).apply()
        }
    }
}