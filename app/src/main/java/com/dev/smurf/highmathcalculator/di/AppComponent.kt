package com.dev.smurf.highmathcalculator.di

import com.dev.smurf.highmathcalculator.mvp.presenters.MatrixPresenter
import com.dev.smurf.highmathcalculator.mvp.presenters.PolinomPresenter
import com.dev.smurf.highmathcalculator.mvp.presenters.SettingsPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [MatrixModule::class , PolinomModule::class , SettingsModule::class , MatrixDataBaseModule::class , PolinomDataBaseModule::class])
interface AppComponent
{
    fun inject( matrixPresenter : MatrixPresenter )

    fun inject( polinomPresenter: PolinomPresenter)

    fun inject( settingsPresenter: SettingsPresenter)

}