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
    fun getMatrixConsistens() : Boolean
    {
        return mPrefs.getBoolean("Matrix" , false)
    }


    //получения настройки сохранения полинома
    fun getPolinomConsistens() : Boolean
    {
        return mPrefs.getBoolean("Polinom" , false)
    }

    //получить режим работы для matrix view holder
    fun getMatrixHolderConsistens() : Boolean
    {
        return mPrefs.getBoolean("MatrixHolder" , false)
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

    //вкл картинки в списке матриц
    fun onMatrixImageHolder()
    {
        doAsync {
            mPrefs.edit().putBoolean("MatrixHolder" , true).apply()
        }
    }

    //вкл картинки в списке матриц
    fun offMatrixImageHolder()
    {
        doAsync {
            mPrefs.edit().putBoolean("MatrixHolder" , false).apply()
        }
    }
}