package com.dev.smurf.highmathcalculator.di

import android.content.Context
import com.dev.smurf.highmathcalculator.mvp.models.SettingsModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


//класс инициалищатор  для SettingsModel
@Module
class SettingsModule(val context: Context)
{

    //Метод предоставления SettingsModel
    @Provides
    @Singleton
    fun provideSettings() : SettingsModel = SettingsModel(context)


    //метод предоставления ApplicationContext
    @Provides
    @Singleton
    fun provideApplicationContext() : Context = context
}