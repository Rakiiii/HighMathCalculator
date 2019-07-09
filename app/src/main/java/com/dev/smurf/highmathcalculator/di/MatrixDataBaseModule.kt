package com.dev.smurf.highmathcalculator.di

import android.content.Context
import com.dev.smurf.highmathcalculator.mvp.models.MatrixDatabaseModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


//класс инициализации MatrixDataBaseModel
@Module
class MatrixDataBaseModule(val context : Context)
{

    //метод предоставления MatrixDataBaseModel
    @Provides
    @Singleton
    fun provideMatrixDataBaseModel(context: Context) : MatrixDatabaseModel = MatrixDatabaseModel(context)
}