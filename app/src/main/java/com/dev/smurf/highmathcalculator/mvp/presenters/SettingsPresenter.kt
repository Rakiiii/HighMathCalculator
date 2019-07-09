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

    fun matrixModeSetOn()
    {
        mSettingsModel.onMatrixSaving()
    }

    fun matrixModeSetOff()
    {
        mSettingsModel.offMatrixSaving()
    }

    fun polinomModeSetOn()
    {
        mSettingsModel.onPolinomSaving()
    }

    fun polinomModeSetOff()
    {
        mSettingsModel.offPolinomSaving()
    }

    fun deleteMatrixDb()
    {
        doAsync {
            mMatrixDatabaseModel.deleteDb()

        }
    }

    fun saveMatrixCache() = mMatrixDatabaseModel.saveCache()

}