package com.dev.smurf.highmathcalculator.di

import com.dev.smurf.highmathcalculator.mvp.presenters.MatrixPresenter
import com.dev.smurf.highmathcalculator.mvp.presenters.PolynomialPresenter
import com.dev.smurf.highmathcalculator.mvp.presenters.SettingsPresenter
import dagger.Component
import javax.inject.Singleton


//интрфейс для вставки зависимостей
@Singleton
//список зависимостей
@Component(modules = [MatrixModule::class , PolynomialModule::class , SettingsModule::class , MatrixDataBaseModule::class , PolynomialDataBaseModule::class])
interface AppComponent
{
    //вставка зависимостей в MatrixPresenter
    fun inject( matrixPresenter : MatrixPresenter )

    //вставка зависимостей в PolynomialPresenter
    fun inject(polynomialPresenter: PolynomialPresenter)

    //вставка зависисмостей в SettingsPresenter
    fun inject( settingsPresenter: SettingsPresenter)

}