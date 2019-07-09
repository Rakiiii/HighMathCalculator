package com.dev.smurf.highmathcalculator.mvp.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.dev.smurf.highmathcalculator.CalculatorApplication
import com.dev.smurf.highmathcalculator.mvp.models.MatrixDatabaseModel
import com.dev.smurf.highmathcalculator.mvp.models.SettingsModel
import com.dev.smurf.highmathcalculator.mvp.views.SettingsViewInterface
import org.jetbrains.anko.doAsync
import javax.inject.Inject

@InjectViewState
class SettingsPresenter : MvpPresenter<SettingsViewInterface>()
{

    /*
     * Вставка зависимостей
     */


    //модель настроек
    @Inject
    lateinit var mSettingsModel : SettingsModel

    init
    {
        CalculatorApplication.graph.inject(this)
    }

    @Inject
    lateinit var mMatrixDatabaseModel: MatrixDatabaseModel

    init
    {
        CalculatorApplication.graph.inject(this)
    }


    /*
     * Операции обраюотки изменения состояния фрагмента
     */


    //обновить режимы
    fun update()
    {
        //проверка состояния сохранения матриц
        if(mSettingsModel.getMatrixConsisten())viewState.setMatrixModeOn()
        else viewState.setMatrixModeOff()

        //проверка состояния сохранения полиномов
        if (mSettingsModel.getPolinomConsisten())viewState.setPolinomModeOn()
        else viewState.setPolinomModeOff()
    }

    //ВКЛ созранение матриц
    fun matrixModeSetOn()
    {
        mSettingsModel.onMatrixSaving()
    }

    //ВЫКЛ сохранение матриц
    fun matrixModeSetOff()
    {
        mSettingsModel.offMatrixSaving()
    }

    //ВКЛ сохранение полиномов
    fun polinomModeSetOn()
    {
        mSettingsModel.onPolinomSaving()
    }

    //ВЫКЛ сохранение полиномов
    fun polinomModeSetOff()
    {
        mSettingsModel.offPolinomSaving()
    }


    /*
     * Функции работы с базами данных
     */

    //очистка бд матриц
    fun deleteMatrixDb()
    {
        doAsync {
            mMatrixDatabaseModel.deleteDb()

        }
    }

    //сохранение кэша бд матриц
    fun saveMatrixCache() = mMatrixDatabaseModel.saveCache()

}