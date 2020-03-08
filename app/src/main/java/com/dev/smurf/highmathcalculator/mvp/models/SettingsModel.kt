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
        return mPrefs.getBoolean("Polynomial" , false)
    }

    //получить режим работы для matrix view holder
    fun getMatrixHolderConsistens() : Boolean
    {
        return mPrefs.getBoolean("MatrixHolder" , false)
    }

    //returns true if image view holder is turned view holder
    fun getPolynomialHolderConsistens() : Boolean
    {
        return mPrefs.getBoolean("PolynomialImageHolder" , false)
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
            mPrefs.edit().putBoolean("Polynomial",true).apply()
        }
    }

    //выключение сохранения матриц
    fun offPolinomSaving()
    {
        doAsync {
            mPrefs.edit().putBoolean("Polynomial",false).apply()
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

    //on polynomial image view holder
    fun onPolynomialImageViewHolder()
    {
        doAsync {
            mPrefs.edit().putBoolean("PolynomialImageHolder" , true).apply()
        }
    }

    //off polynomial image view holder
    fun offPolynomialImageViewHolder()
    {
        doAsync {
            mPrefs.edit().putBoolean("PolynomialImageHolder" , false).apply()
        }
    }
}