package com.dev.smurf.highmathcalculator

import android.app.Application
import android.content.Context
import com.dev.smurf.highmathcalculator.di.*


class CalculatorApplication : Application()
{
    companion object
    {
        lateinit var graph : AppComponent
        lateinit var context : Context
    }

    override fun onCreate()
    {
        super.onCreate()

        context = applicationContext

        graph = DaggerAppComponent.builder().
            matrixModule(MatrixModule()).
            polynomialModule(PolynomialModule()).
            settingsModule(SettingsModule(applicationContext)).
            matrixDataBaseModule(MatrixDataBaseModule(applicationContext)).
            polynomialDataBaseModule(PolynomialDataBaseModule(applicationContext)).
            build()

    }
}