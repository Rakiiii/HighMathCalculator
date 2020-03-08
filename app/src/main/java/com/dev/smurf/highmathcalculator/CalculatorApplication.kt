package com.dev.smurf.highmathcalculator

import android.app.Application
import com.dev.smurf.highmathcalculator.di.*


class CalculatorApplication : Application()
{
    companion object
    {
        lateinit var graph : AppComponent
    }

    override fun onCreate()
    {
        super.onCreate()

        graph = DaggerAppComponent.builder().
            matrixModule(MatrixModule()).
            polynomialModule(PolynomialModule()).
            settingsModule(SettingsModule(applicationContext)).
            matrixDataBaseModule(MatrixDataBaseModule(applicationContext)).
            polynomialDataBaseModule(PolynomialDataBaseModule(applicationContext)).
            build()

    }
}