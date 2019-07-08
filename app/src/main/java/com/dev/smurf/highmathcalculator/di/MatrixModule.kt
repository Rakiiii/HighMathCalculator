package com.dev.smurf.highmathcalculator.di

import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class MatrixModel
{
    @Provides
    @Singleton
    fun provideMatrix() : MatrixModel = MatrixModel()

}