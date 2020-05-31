package com.dev.smurf.highmathcalculator.mvp.presenters

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.util.Log
import com.dev.smurf.highmathcalculator.CalculatorApplication
import com.dev.smurf.highmathcalculator.Exceptions.WrongDataException
import com.dev.smurf.highmathcalculator.mvp.models.MatrixDatabaseModel
import com.dev.smurf.highmathcalculator.mvp.models.MatrixModel
import com.dev.smurf.highmathcalculator.mvp.models.SettingsModel
import com.dev.smurf.highmathcalculator.mvp.views.MatrixViewInterface
import com.dev.smurf.highmathcalculator.ui.POJO.MatrixGroup
import kotlinx.coroutines.*
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.presenterScope
import org.jetbrains.anko.doAsync
import javax.inject.Inject

@InjectViewState
class MatrixPresenter : MvpPresenter<MatrixViewInterface>()
{

    /*
     * Вставка зависимостей
     */

    @Inject
    lateinit var mSettingsModel: SettingsModel

    init
    {
        CalculatorApplication.graph.inject(this)
    }

    @Inject
    lateinit var mMatrixModel: MatrixModel

    init
    {
        CalculatorApplication.graph.inject(this)
    }

    @Inject
    lateinit var mMatrixDataBaseModel: MatrixDatabaseModel

    init
    {
        CalculatorApplication.graph.inject(this)
    }


    private val supJob = SupervisorJob()

    //корутин скоп для ui
    private val uiScope = CoroutineScope(Dispatchers.Main + supJob)


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
     * Реализация операций
     */

    @SuppressLint("StaticFieldLeak")
    fun onPlusClick(firstMatrix: String, secondMatrix: String)
    {

        presenterScope.launch(Dispatchers.Main + errorHandler)
        {

            //сохраняем время начала операции
            val time = java.util.GregorianCalendar()
            time.timeInMillis = System.currentTimeMillis()

            val mMatrixGroup = mMatrixModel.MatrixPlus(presenterScope, firstMatrix, secondMatrix)

            mMatrixGroup.time = time
            addToDb(mMatrixGroup)

            uiScope.launch {
                viewState.addToRecyclerView(mMatrixGroup)
            }

        }
    }


    @SuppressLint("StaticFieldLeak")
    fun onMinusClick(firstMatrix: String, secondMatrix: String)
    {

        presenterScope.launch(Dispatchers.Main + errorHandler)
        {

            //сохраняем время начала операции
            val time = java.util.GregorianCalendar()
            time.timeInMillis = System.currentTimeMillis()

            val mMatrixGroup = mMatrixModel.MatrixMinus(presenterScope, firstMatrix, secondMatrix)

            mMatrixGroup.time = time

            addToDb(mMatrixGroup)

            uiScope.launch {
                viewState.addToRecyclerView(mMatrixGroup)
            }
        }
    }


    @SuppressLint("StaticFieldLeak")
    fun onTimesClick(firstMatrix: String, secondMatrix: String)
    {
        presenterScope.launch(Dispatchers.Main + errorHandler)
        {

            //сохраняем время начала операции
            val time = java.util.GregorianCalendar()
            time.timeInMillis = System.currentTimeMillis()

            val mMatrixGroup = mMatrixModel.MatrixTimes(presenterScope, firstMatrix, secondMatrix)

            mMatrixGroup.time = time

            addToDb(mMatrixGroup)

            uiScope.launch {
                viewState.addToRecyclerView(mMatrixGroup)
            }
        }
    }


    @SuppressLint("StaticFieldLeak")
    fun onInversClick(firstMatrix: String)
    {
        //CoroutineScope(Dispatchers.Main)
        presenterScope.launch(Dispatchers.Main + errorHandler)
        {

            //сохраняем время начала операции
            val time = java.util.GregorianCalendar()
            time.timeInMillis = System.currentTimeMillis()
            val mMatrixGroup = mMatrixModel.MatrixInverse(presenterScope, firstMatrix)

            mMatrixGroup.time = time
            addToDb(mMatrixGroup)

            uiScope.launch {
                viewState.addToRecyclerView(mMatrixGroup)
            }
        }
    }


    @SuppressLint("StaticFieldLeak")
    fun onDeterminantClick(firstMatrix: String)
    {

        presenterScope.launch(Dispatchers.Main + errorHandler)
        {

            val time = java.util.GregorianCalendar()
            time.timeInMillis = System.currentTimeMillis()
            val mMatrixGroup = mMatrixModel.MatrixDeterminant(presenterScope, firstMatrix)

            mMatrixGroup.time = time

            addToDb(mMatrixGroup)

            uiScope.launch {
                viewState.addToRecyclerView(mMatrixGroup)
            }
        }
    }

    fun btnSwitchClicked(position: Int)
    {
        viewState.setBtnFragment(position)
    }

    fun btnEighnvalueClicked()
    {
        viewState.showToast("WIP")
    }

    fun btnNegativeClicked()
    {
        viewState.showToast("WIP")
    }

    fun btnEighnvectorClicked()
    {
        viewState.showToast("WIP")
    }

    fun btnPositiveClicked()
    {
        viewState.showToast("WIP")
    }

    fun btnRankClicked()
    {
        viewState.showToast("WIP")
    }

    fun btnSolveSystemClicked()
    {
        viewState.showToast("WIP")
    }


    /*
     * Реализация работы с базой данных
     */

    //удаление из бд элемента matrixGroup
    fun deleteFromDb(matrixGroup: MatrixGroup)
    {
        doAsync {
            mMatrixDataBaseModel.delete(matrixGroup)
            mMatrixDataBaseModel.deleteFromDbCache(matrixGroup)
        }
    }

    suspend fun addToDb(group: MatrixGroup)
    {
        withContext(presenterScope.coroutineContext + Dispatchers.IO)
        {
            if (mSettingsModel.getMatrixConsistens())
            {
                //записываем в бд
                mMatrixDataBaseModel.insert(group)
            }
            else
            {

                //иначе записываем в кэщ бд
                mMatrixDataBaseModel.addToCache(group)
            }
        }
    }

    //загрузка из базы данных сохраненных результатов
    @SuppressLint("StaticFieldLeak")
    fun onLoadSavedInstance()
    {
        presenterScope.launch(Dispatchers.IO + supJob)
        {
            val result = mMatrixDataBaseModel.selectAll().reversed()
            uiScope.launch {
                viewState.setRecyclerViewArrayList(ArrayList(result))
            }
        }
    }

    fun checkImageMode() = mSettingsModel.getMatrixHolderConsistens()

}