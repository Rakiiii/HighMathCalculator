package com.dev.smurf.highmathcalculator.di

import android.content.Context
import com.dev.smurf.highmathcalculator.mvp.models.PolynomialDataBaseModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton



//класс инициализатор для PolynomialDataBaseModel
@Module
class PolynomialDataBaseModule(val context: Context)
{

    //метод предоставления PolynomialDataBaseModel
    @Provides
    @Singleton
    fun providePolynomialDataBaseModel() : PolynomialDataBaseModel = PolynomialDataBaseModel(context)
}