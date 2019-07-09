package com.dev.smurf.highmathcalculator.di

import android.content.Context
import com.dev.smurf.highmathcalculator.mvp.models.PolinomDataBaseModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton



//класс инициализатор для PolinomDataBaseModel
@Module
class PolinomDataBaseModule(val context: Context)
{

    //метод предоставления PolinomDataBaseModel
    @Provides
    @Singleton
    fun providePolinomDataBaseModel() : PolinomDataBaseModel = PolinomDataBaseModel(context)
}