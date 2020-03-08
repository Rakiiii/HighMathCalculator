package com.dev.smurf.highmathcalculator.di

import com.dev.smurf.highmathcalculator.mvp.models.PolynomialModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


//класс инициализатор для PolynomialModel
@Module
class PolynomialModule
{

    //метод предоставления PolynomialModel
    @Provides
    @Singleton
    fun providePolynomial() : PolynomialModel = PolynomialModel()
}