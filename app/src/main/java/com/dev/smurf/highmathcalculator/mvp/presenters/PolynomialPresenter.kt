package com.dev.smurf.highmathcalculator.mvp.presenters

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.dev.smurf.highmathcalculator.CalculatorApplication
import com.dev.smurf.highmathcalculator.Exceptions.WrongDataException
import com.dev.smurf.highmathcalculator.mvp.models.PolynomialDataBaseModel
import com.dev.smurf.highmathcalculator.mvp.models.PolynomialModel
import com.dev.smurf.highmathcalculator.mvp.models.SettingsModel
import com.dev.smurf.highmathcalculator.mvp.views.PolynomialViewInterface
import com.example.smurf.mtarixcalc.PolynomialGroup
import kotlinx.coroutines.*
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.presenterScope
import org.jetbrains.anko.doAsync
import javax.inject.Inject


@InjectViewState
class PolynomialPresenter : MvpPresenter<PolynomialViewInterface>(), LifecycleObserver
{
    /*
     * вставка зависимостей
     */

    @Inject
    lateinit var mPolynomialDataBaseModel: PolynomialDataBaseModel

    init
    {
        CalculatorApplication.graph.inject(this)
    }

    @Inject
    lateinit var mPolynomialModel: PolynomialModel

    init
    {
        CalculatorApplication.graph.inject(this)
    }

    @Inject
    lateinit var mSettingsModel: SettingsModel

    init
    {
        CalculatorApplication.graph.inject(this)
    }

    private var isLoaded = false

    val supJob = SupervisorJob()

    //корутин скоп для ui
    private val uiScope = CoroutineScope(Dispatchers.Main + supJob)

    //corutine scope для работы с памятью
    private val ioScope = CoroutineScope(Dispatchers.IO + supJob)

    //обработчик ошибок
    private val errorHandler = CoroutineExceptionHandler(handler = { _, error ->
        when (error)
        {
            is WrongDataException ->
            {
                viewState.showToast(error.toString().substringAfter(':'))
            }
            else ->
            {
                Log.d("ExceptionHandler@", error.toString())
                Log.d("ExceptionHandler@", "StackTrace@", error)
            }
        }
    })


    /*
     * Релизация операций с полиномами
     */

    //нажатие кнопки плюс
    //@SuppressLint("StaticFieldLeak")
    fun onPlusClick(left: String, right: String)
    {
        presenterScope.launch(Dispatchers.Main + errorHandler)
        {

            val time = java.util.GregorianCalendar()
            time.timeInMillis = System.currentTimeMillis()
            val result =
                mPolynomialModel.PolynomialPlus(presenterScope, left, right)

            result.time = time

            addToDb(result)

            uiScope.launch {
                viewState.addToPolynomialRecyclerView(result)
            }

        }
    }


    //нажатие на кнопку минус
    //@SuppressLint("StaticFieldLeak")
    fun onMinusClick(left: String, right: String)
    {
        presenterScope.launch(Dispatchers.Main + errorHandler)
        {

            val time = java.util.GregorianCalendar()
            time.timeInMillis = System.currentTimeMillis()

            val result = mPolynomialModel.PolynomialMinus(presenterScope, left, right)

            result.time = time

            addToDb(result)

            uiScope.launch {
                viewState.addToPolynomialRecyclerView(result)
            }


        }
    }

    //нажатие на кнопку умножения
    //@SuppressLint("StaticFieldLeak")
    fun onTimesClick(left: String, right: String)
    {
        presenterScope.launch(Dispatchers.Main + errorHandler)
        {

            val time = java.util.GregorianCalendar()
            time.timeInMillis = System.currentTimeMillis()
            val result = mPolynomialModel.PolynomialTimes(presenterScope, left, right)

            result.time = time

            addToDb(result)

            uiScope.launch {
                viewState.addToPolynomialRecyclerView(result)
            }

        }
    }

    //нажатие на кнопку деления
    //@SuppressLint("StaticFieldLeak")
    fun onDivisionClick(left: String, right: String)
    {
        presenterScope.launch(Dispatchers.Main + errorHandler)
        {

            val time = java.util.GregorianCalendar()
            time.timeInMillis = System.currentTimeMillis()
            val result = mPolynomialModel.PolynomialDivision(presenterScope, left, right)

            //устанавливаем время страта операции
            result.time = time

            addToDb(result)

            uiScope.launch {
                viewState.addToPolynomialRecyclerView(result)
            }

        }
    }

    fun onSwitchBtnFragmentClick(position: Int)
    {
        viewState.showToast("WIP")
    }

    //нажатие на кнопку решения
    //@SuppressLint("StaticFieldLeak")
    fun onRootsOfClick(left: String)
    {
        viewState.showToast("Work in progress")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart()
    {
        viewState.setObserver()
        if (!isLoaded)
        {
            isLoaded = true
            onLoadSavedInstance()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume()
    {
        updateSettings()
        viewState.restoreFromViewModel()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause()
    {
        viewState.saveRecyclerViewToViewModel()
    }


    fun updateSettings()
    {
        checkRecyclerViewMode()
    }

    private fun checkRecyclerViewMode()
    {
        if (mSettingsModel.getPolynomialHolderConsistens())
        {
            viewState.setImageAdapter()
        }
        else
        {
            viewState.setTxtAdapter()
        }
    }

/*
 * Реализация работы с бд
 */

    //удаление из базы данных
    fun deleteFromDb(polynomialGroup: PolynomialGroup)
    {
        doAsync {
            mPolynomialDataBaseModel.delete(polynomialGroup)
            mPolynomialDataBaseModel.deleteFromDbCache(polynomialGroup)
        }
    }

    //загрузка созраненного в бд состояния
    fun onLoadSavedInstance()
    {
        presenterScope.launch(Dispatchers.IO + errorHandler) {
            val result = mPolynomialDataBaseModel.selectAll().reversed()

            uiScope.launch {
                viewState.setRecyclerViewList(result.toMutableList())
            }
        }
    }

    private fun addToDb(result: PolynomialGroup)
    {
        ioScope.launch(Dispatchers.IO + errorHandler) {

            //если включена запись в бд
            if (mSettingsModel.getPolinomConsistens())
            {
                //записываем в бд
                mPolynomialDataBaseModel.insert(result)

            }
            else
            {
                //пишем в сache бд
                mPolynomialDataBaseModel.addToCache(result)

            }
        }
    }

}