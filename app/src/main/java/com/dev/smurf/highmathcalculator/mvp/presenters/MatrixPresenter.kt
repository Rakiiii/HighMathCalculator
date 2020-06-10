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
import com.dev.smurf.highmathcalculator.Exceptions.TimeableException
import com.dev.smurf.highmathcalculator.Exceptions.WrongDataException
import com.dev.smurf.highmathcalculator.Matrix.Matrix
import com.dev.smurf.highmathcalculator.R
import com.dev.smurf.highmathcalculator.mvp.models.InputFormatExceptionsRenderModel
import com.dev.smurf.highmathcalculator.mvp.models.MatrixDatabaseModel
import com.dev.smurf.highmathcalculator.mvp.models.MatrixModel
import com.dev.smurf.highmathcalculator.mvp.models.SettingsModel
import com.dev.smurf.highmathcalculator.mvp.views.MatrixViewInterface
import com.dev.smurf.highmathcalculator.ui.POJO.MatrixGroup
import com.dev.smurf.highmathcalculator.withTime
import kotlinx.coroutines.*
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.presenterScope
import org.jetbrains.anko.doAsync
import javax.inject.Inject

@ExperimentalCoroutinesApi
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

    private val supJob = SupervisorJob()

    //корутин скоп для ui
    private val uiScope = CoroutineScope(Dispatchers.Main + supJob)


    @ExperimentalCoroutinesApi
    private val errorHandler = CoroutineExceptionHandler(handler = { job, error ->
        if (error is TimeableException && error.time != TimeableException.zeroTime)
        {
            uiScope.launch {
                viewState.calculationFailed(
                    MatrixGroup(
                        Matrix.EmptyMatrix,
                        Matrix.EmptyMatrix,
                        MatrixGroup.CALCULATION,
                        Matrix.EmptyMatrix,
                        error.time
                    )
                )
            }
        }
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
                        mExceptionRenderModel.getErrorDialogWidth(),
                        mExceptionRenderModel.getErrorDialogHeight(),
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
                        mExceptionRenderModel.getErrorDialogWidth(),
                        mExceptionRenderModel.getErrorDialogHeight(),
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
                        mExceptionRenderModel.getErrorDialogWidth(),
                        mExceptionRenderModel.getErrorDialogHeight(),
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
                        mExceptionRenderModel.getErrorDialogWidth(),
                        mExceptionRenderModel.getErrorDialogHeight(),
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
                viewState.displayError(error.toString().substringAfter(':'))
                //viewState.showToast(error.toString().substringAfter(':'))
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
        if (firstMatrix.isEmpty() || secondMatrix.isEmpty()) return
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
        if (firstMatrix.isEmpty() || secondMatrix.isEmpty()) return
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
        if (firstMatrix.isEmpty() || secondMatrix.isEmpty()) return
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
        if (firstMatrix.isEmpty()) return
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
        if (firstMatrix.isEmpty()) return
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

    fun btnSolveSystemClicked(matrix: String)
    {
        if (matrix.isEmpty()) return
        presenterScope.launch(Dispatchers.Main + errorHandler)
        {

            val time = java.util.GregorianCalendar()
            time.timeInMillis = System.currentTimeMillis()

            viewState.startCalculation(
                MatrixGroup(
                    Matrix.EmptyMatrix,
                    Matrix.EmptyMatrix,
                    MatrixGroup.CALCULATION,
                    Matrix.EmptyMatrix,
                    time
                )
            )

            val mMatrixGroup = withTime(
                presenterScope.coroutineContext + Dispatchers.Default,
                time
            ) { mMatrixModel.MatrixSolve(presenterScope, matrix) }


            mMatrixGroup.time = time

            addToDb(mMatrixGroup)

            uiScope.launch {
                viewState.stopCalculation(mMatrixGroup)
            }
        }
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

    fun restoreInDb(matrixGroup: MatrixGroup)
    {
        presenterScope.launch(Dispatchers.IO) {
            mMatrixDataBaseModel.insert(matrixGroup)
            mMatrixDataBaseModel.addToCache(matrixGroup)
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
            onLoadSavedInstance()
            isLoaded = true
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume()
    {
        updateSettings()
        if (!isLoaded)
        {
            presenterScope.launch(Dispatchers.IO)
            {
                uiScope.launch { viewState.startLoadingInRecyclerView() }
                delay(500)
                uiScope.launch {
                    viewState.stopLoadingInRecyclerView()
                    viewState.restoreFromViewModel()
                }
            }
        }
        else
        {
            onLoadSavedInstance()
            isLoaded = true
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause()
    {
        viewState.setTopPosition()
        viewState.stopLoadingInRecyclerView()
        viewState.allCalculationsStoped()
        viewState.saveListRecyclerViewViewModel()
        viewState.clearRecyclerView()
        isLoaded = !isLoaded
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
            uiScope.launch { viewState.startLoadingInRecyclerView() }
            delay(1000)
            val result =
                mMatrixDataBaseModel.selectAll().sortedBy { s -> s.time }.reversed().toMutableList()
            uiScope.launch {
                viewState.stopLoadingInRecyclerView()
                viewState.setRecyclerViewList(result)
            }
        }
    }

}