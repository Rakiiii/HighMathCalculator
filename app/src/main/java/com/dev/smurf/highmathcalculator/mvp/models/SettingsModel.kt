package com.dev.smurf.highmathcalculator.mvp.models

import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import org.jetbrains.anko.defaultSharedPreferences

class SettingsModel(val context: Context)
{
    private val mPrefs : SharedPreferences

    init
    {
        mPrefs = context.defaultSharedPreferences
    }

    fun getMatrixConsisten() : Boolean
    {
        return mPrefs.getBoolean("Matrix" , false)
    }

    fun getPolinomConsisten() : Boolean
    {
        return mPrefs.getBoolean("Polinom" , false)
    }

    fun onMatrixSaving()
    {
                object : AsyncTask<Void,Void,Boolean>(){
            override fun doInBackground(vararg params: Void?): Boolean
            {
                mPrefs.edit().putBoolean("Matrix",true).commit()
                return true
            }
        }.execute()
    }

    fun offMatrixSaving()
    {
                object : AsyncTask<Void,Void,Boolean>(){
            override fun doInBackground(vararg params: Void?): Boolean
            {
                mPrefs.edit().putBoolean("Matrix",false).commit()
                return true
            }
        }.execute()
    }

    fun onPolinomSaving()
    {
                object : AsyncTask<Void,Void,Boolean>(){
            override fun doInBackground(vararg params: Void?): Boolean
            {
                mPrefs.edit().putBoolean("Polinom",true).commit()
                return true
            }
        }.execute()
    }

    fun offPolinomSaving()
    {
                object : AsyncTask<Void,Void,Boolean>(){
            override fun doInBackground(vararg params: Void?): Boolean
            {
                mPrefs.edit().putBoolean("Polinom",false).commit()
                return true
            }
        }.execute()
    }
}