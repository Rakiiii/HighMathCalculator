package com.dev.smurf.highmathcalculator.di

import com.dev.smurf.highmathcalculator.mvp.models.MatrixModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class MatrixModule
{
    @Provides
    @Singleton
    fun provideMatrix() : MatrixModel = MatrixModel()

}