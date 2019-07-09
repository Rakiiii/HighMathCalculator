package com.dev.smurf.highmathcalculator.di

import com.dev.smurf.highmathcalculator.mvp.models.PolinomModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


//класс инициализатор для PolinomModel
@Module
class PolinomModule
{

    //метод предоставления PolinomModel
    @Provides
    @Singleton
    fun providePolinom() : PolinomModel = PolinomModel()
}