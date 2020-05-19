package com.dev.smurf.highmathcalculator.mvp.presenters

import com.dev.smurf.highmathcalculator.CalculatorApplication
import com.dev.smurf.highmathcalculator.mvp.models.MatrixDatabaseModel
import com.dev.smurf.highmathcalculator.mvp.models.PolynomialDataBaseModel
import com.dev.smurf.highmathcalculator.mvp.models.SettingsModel
import com.dev.smurf.highmathcalculator.mvp.views.SettingsViewInterface
import moxy.InjectViewState
import moxy.MvpPresenter
import org.jetbrains.anko.doAsync
import javax.inject.Inject

//todo::add switch fot recycler view adpter for polynomial

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

    //модель базы данных матриц
    @Inject
    lateinit var mMatrixDatabaseModel: MatrixDatabaseModel

    init
    {
        CalculatorApplication.graph.inject(this)
    }

    //модель бызы данных полиномов
    @Inject
    lateinit var mPolynomialDatabaseModel: PolynomialDataBaseModel

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
        if(mSettingsModel.getMatrixConsistens())viewState.setMatrixModeOn()
        else viewState.setMatrixModeOff()

        //проверка состояния сохранения полиномов
        if (mSettingsModel.getPolinomConsistens())viewState.setPolinomModeOn()
        else viewState.setPolinomModeOff()

        //проверка режима работы списка матриц
        if(mSettingsModel.getMatrixHolderConsistens())viewState.setHolderImageModeOn()
        else viewState.setHolderImageModeOff()
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

    //вкл картинки в списке матриц
    fun holderImageModeSetOn()
    {
        mSettingsModel.onMatrixImageHolder()

        mSettingsModel.onPolynomialImageViewHolder()
    }

    //выкл картинки в списке матриц
    fun holderImageModeSetOff()
    {
        mSettingsModel.offMatrixImageHolder()

        mSettingsModel.offPolynomialImageViewHolder()
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
    fun saveMatrixCache()
    {
        doAsync {
            mMatrixDatabaseModel.saveCache()
        }
    }

    //очистка бд полиномов
    fun deletePolinomDb()
    {
        doAsync {
            mPolynomialDatabaseModel.deleteDb()
        }
    }

    //сохранение кэша бд полиномов
    fun savePolinomCache()
    {
        doAsync {
            mPolynomialDatabaseModel.saveCache()
        }
    }

}