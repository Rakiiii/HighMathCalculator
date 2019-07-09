package com.dev.smurf.highmathcalculator.di

import com.dev.smurf.highmathcalculator.mvp.presenters.MatrixPresenter
import com.dev.smurf.highmathcalculator.mvp.presenters.PolinomPresenter
import com.dev.smurf.highmathcalculator.mvp.presenters.SettingsPresenter
import dagger.Component
import javax.inject.Singleton


//интрфейс для вставки зависимостей
@Singleton
//список зависимостей
@Component(modules = [MatrixModule::class , PolinomModule::class , SettingsModule::class , MatrixDataBaseModule::class , PolinomDataBaseModule::class])
interface AppComponent
{
    //вставка зависимостей в MatrixPresenter
    fun inject( matrixPresenter : MatrixPresenter )

    //вставка зависимостей в PolinomPresenter
    fun inject( polinomPresenter: PolinomPresenter)

    //вставка зависисмостей в SettingsPresenter
    fun inject( settingsPresenter: SettingsPresenter)

}