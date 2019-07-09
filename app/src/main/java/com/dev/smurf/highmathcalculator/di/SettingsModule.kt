package com.dev.smurf.highmathcalculator.di

import android.content.Context
import com.dev.smurf.highmathcalculator.mvp.models.SettingsModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SettingsModule(val context: Context)
{

    @Provides
    @Singleton
    fun provideSettings() : SettingsModel = SettingsModel(context)

    @Provides
    @Singleton
    fun provideApplicationContext() : Context = context
}