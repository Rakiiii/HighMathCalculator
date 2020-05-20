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
    lateinit var mSettingsModel: SettingsModel

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

    private val state = settingsState()
    private val startState = settingsState()


    /*
     * Операции обраюотки изменения состояния фрагмента
     */


    //обновить режимы
    fun settingsOpened()
    {
        //проверка состояния сохранения матриц
        if (mSettingsModel.getMatrixConsistens())
        {
            viewState.setMatrixModeOn()
            state.matrixMode = true
            startState.matrixMode = true
        }
        else viewState.setMatrixModeOff()

        //проверка состояния сохранения полиномов
        if (mSettingsModel.getPolinomConsistens())
        {
            viewState.setPolinomModeOn()
            state.polynomialMode = true
            startState.polynomialMode = true
        }
        else viewState.setPolinomModeOff()

        //проверка режима работы списка матриц
        if (mSettingsModel.getMatrixHolderConsistens())
        {
            viewState.setHolderImageModeOn()
            state.holderImageMode = true
            startState.holderImageMode = true

        }
        else viewState.setHolderImageModeOff()
    }

    //ВКЛ созранение матриц
    fun matrixModeSetOn()
    {
        state.matrixMode = true
    }

    //ВЫКЛ сохранение матриц
    fun matrixModeSetOff()
    {
        state.matrixMode = false
    }

    //ВКЛ сохранение полиномов
    fun polinomModeSetOn()
    {
        state.polynomialMode = true
    }

    //ВЫКЛ сохранение полиномов
    fun polinomModeSetOff()
    {
        state.polynomialMode = false
    }

    //вкл картинки в списке матриц
    fun holderImageModeSetOn()
    {
        state.holderImageMode = true
        mSettingsModel.onMatrixImageHolder()

        mSettingsModel.onPolynomialImageViewHolder()
    }

    //выкл картинки в списке матриц
    fun holderImageModeSetOff()
    {
        state.holderImageMode = false
        mSettingsModel.offMatrixImageHolder()

        mSettingsModel.offPolynomialImageViewHolder()
    }

    fun onCancelHappened()
    {
        saveState()
    }

    fun doneBtnPressed()
    {
        saveState()
        viewState.dismissDialog()
    }

    /*
     * Функции работы с базами данных
     */

    //очистка бд матриц
    fun deleteMatrixDbBtnPressed()
    {
        doAsync {
            mMatrixDatabaseModel.deleteDb()
        }
    }

    //сохранение кэша бд матриц
    private fun saveMatrixCache()
    {
        doAsync {
            mMatrixDatabaseModel.saveCache()
        }
    }

    //очистка бд полиномов
    fun deletePolinomDbPressed()
    {
        doAsync {
            mPolynomialDatabaseModel.deleteDb()
        }
    }

    //сохранение кэша бд полиномов
    private fun savePolynomialCache()
    {
        doAsync {
            mPolynomialDatabaseModel.saveCache()
        }
    }

    private data class settingsState(
        var matrixMode: Boolean = false,
        var polynomialMode: Boolean = false,
        var holderImageMode: Boolean = false
    )

    private fun saveState()
    {
        if (startState.matrixMode != state.matrixMode)
        {
            if (state.matrixMode)
            {
                mSettingsModel.onMatrixSaving()
                saveMatrixCache()
            }
            else
            {
                mSettingsModel.offMatrixSaving()
            }
        }

        if (startState.polynomialMode != state.polynomialMode)
        {
            if (state.polynomialMode)
            {
                mSettingsModel.onPolinomSaving()
                savePolynomialCache()
            }
            else
            {
                mSettingsModel.offPolinomSaving()
            }
        }

        if (startState.holderImageMode != state.holderImageMode)
        {
            if (state.holderImageMode)
            {
                mSettingsModel.onPolynomialImageViewHolder()
                mSettingsModel.onMatrixImageHolder()
            }
            else
            {
                mSettingsModel.offMatrixImageHolder()
                mSettingsModel.offPolynomialImageViewHolder()
            }
        }

    }


}