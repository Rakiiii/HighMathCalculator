package com.dev.smurf.highmathcalculator.di

import com.dev.smurf.highmathcalculator.mvp.models.PolinomModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PolinomModule
{
    @Provides
    @Singleton
    fun providePolinom() : PolinomModel = PolinomModel()
}