package com.dev.smurf.highmathcalculator.mvp.presenters

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.dev.smurf.highmathcalculator.CalculatorApplication
import com.dev.smurf.highmathcalculator.Exceptions.MatrixSerializeExceptions.DifferentAmountOfElementsInMatrixLineException
import com.dev.smurf.highmathcalculator.Exceptions.MatrixSerializeExceptions.WrongAmountOfBracketsInMatrixException
import com.dev.smurf.highmathcalculator.Exceptions.MatrixSerializeExceptions.WrongElemntAtMatrixInputException
import com.dev.smurf.highmathcalculator.Exceptions.MatrixSerializeExceptions.WrongSymbolAtMatrixInputException
import com.dev.smurf.highmathcalculator.Exceptions.WrongDataException
import com.dev.smurf.highmathcalculator.R
import com.dev.smurf.highmathcalculator.mvp.models.InputFormatExceptionsRenderModel
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
class MatrixPresenter : MvpPresenter<MatrixViewInterface>(), LifecycleObserver
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

    @ExperimentalCoroutinesApi
    private val mExceptionRenderModel = InputFormatExceptionsRenderModel()

    private var isLoaded = false

    private var isImageViewHolder = false


    private val supJob = SupervisorJob()

    //корутин скоп для ui
    private val uiScope = CoroutineScope(Dispatchers.Main + supJob)


    @ExperimentalCoroutinesApi
    private val errorHandler = CoroutineExceptionHandler(handler = { _, error ->
        when (error)
        {
            is WrongElemntAtMatrixInputException ->
            {
                uiScope.launch {
                    val errorBitmap =
                        mExceptionRenderModel.drawErroredMatrixWhenPartOfLineIsWrong(
                            presenterScope,
                            error.input,
                            error.unrecognizedPart
                        )
                    viewState.showErrorDialog(
                        errorBitmap,
                        mExceptionRenderModel.screenWidth - 30,
                        mExceptionRenderModel.screenHeight,
                        CalculatorApplication.context.getString(R.string.wrongCofFormat)
                    )
                }
            }
            is DifferentAmountOfElementsInMatrixLineException ->
            {
                uiScope.launch {
                    val errorBitmap =
                        mExceptionRenderModel.drawErroredMatrixWhenFullLineIsWrong(
                            presenterScope,
                            error.input,
                            error.unrecognizedPart
                        )
                    viewState.showErrorDialog(
                        errorBitmap,
                        mExceptionRenderModel.screenWidth - 30,
                        mExceptionRenderModel.screenHeight,
                        if (error.unrecognizedPart == "") CalculatorApplication.context.getString(
                            R.string.emptyLineInMatrix
                        )
                        else CalculatorApplication.context.getString(R.string.diffLineLength)
                    )
                }
            }
            is WrongSymbolAtMatrixInputException ->
            {
                uiScope.launch {
                    val errorBitmap =
                        mExceptionRenderModel.drawErroredMatrixWithWrongChars(
                            presenterScope,
                            error.input,
                            error.unrecognizedPart
                        )
                    viewState.showErrorDialog(
                        errorBitmap,
                        mExceptionRenderModel.screenWidth - 30,
                        mExceptionRenderModel.screenHeight,
                        CalculatorApplication.context.getString(R.string.wrongSymbols)
                    )
                }
            }
            is WrongAmountOfBracketsInMatrixException ->
            {
                uiScope.launch {
                    val errorBitmap =
                        mExceptionRenderModel.drawErroredMatrixWithWrongChars(
                            presenterScope,
                            error.input,
                            error.unrecognizedPart
                        )
                    viewState.showErrorDialog(
                        errorBitmap,
                        mExceptionRenderModel.screenWidth - 30,
                        mExceptionRenderModel.screenHeight,
                        CalculatorApplication.context.getString(
                            if (error.unrecognizedPart.contains(
                                    '('
                                )
                            ) R.string.moreLeftBrackets
                            else R.string.moreRightBrackets
                        )
                    )
                }
            }
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

    fun setMaxDialogSize(width: Float, height: Float)
    {
        presenterScope.launch(Dispatchers.IO + supJob) {
            Log.d("size@", "set width:$width height:$height")
            mExceptionRenderModel.screenWidth = width
            mExceptionRenderModel.screenHeight = height
        }
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
        withContext(presenterScope.coroutineContext + Dispatchers.IO + errorHandler)
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

    fun onBtnOkInErrorDialogPressed()
    {
        viewState.dismissErrorDialog()
    }

    //загрузка из базы данных сохраненных результатов
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart()
    {
        viewState.setObservable()
        if (!isLoaded)
        {
            isLoaded = true
            onLoadSavedInstance()
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume()
    {
        //viewState.getMaxSizeOfErrorDialog()
        updateSettings()
        viewState.restoreFromViewModel()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause()
    {
        viewState.saveListRecyclerViewViewModel()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop()
    {
        viewState.dismissErrorDialog()
    }

    fun updateSettings()
    {
        checkMatrixRecyclerViewMode()
    }

    private fun checkMatrixRecyclerViewMode()
    {
        if (mSettingsModel.getMatrixHolderConsistens())
        {
            viewState.setImageAdapter()
        }
        else
        {
            viewState.setTextAdapter()
        }
    }

    private fun onLoadSavedInstance()
    {
        presenterScope.launch(Dispatchers.IO + supJob + errorHandler)
        {
            val result = mMatrixDataBaseModel.selectAll().reversed().toMutableList()
            uiScope.launch {
                viewState.setRecyclerViewList(result)
            }
        }
    }

}