package com.dev.smurf.highmathcalculator.mvp.presenters

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.dev.smurf.highmathcalculator.CalculatorApplication
import com.dev.smurf.highmathcalculator.mvp.models.MatrixDatabaseModel
import com.dev.smurf.highmathcalculator.mvp.models.MatrixModel
import com.dev.smurf.highmathcalculator.mvp.models.SettingsModel
import com.dev.smurf.highmathcalculator.mvp.views.MatrixViewInterface
import com.example.smurf.mtarixcalc.MatrixGroup
import kotlinx.coroutines.*
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
    lateinit var mMatrixModel : MatrixModel
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


    //корутин скоп для ui
    val uiScope = CoroutineScope(Dispatchers.Main)


    /*
     * Реализация операций
     */

    @SuppressLint("StaticFieldLeak")
    fun onPlusClick(firstMatrix : String , secondMatrix : String)
    {

        uiScope.launch(Dispatchers.Main + errorHandler)
        {

                val task: Deferred<MatrixGroup> = async(Dispatchers.IO) {

                    //сохраняем время начала операции
                    val time = java.util.GregorianCalendar()
                    time.timeInMillis = System.currentTimeMillis()

                    //складываем матрицы
                    val result = mMatrixModel.plus(
                        mMatrixModel.createMatrix(firstMatrix),
                        mMatrixModel.createMatrix(secondMatrix)
                    )


                    //устанавливаем время начала операции
                    result.time = time

                    result
                }

                val mMatrixGroup = task.await()

                async(Dispatchers.IO)
                {
                    if (mSettingsModel.getMatrixConsisten())
                    {
                        //записываем в бд
                        mMatrixDataBaseModel.insert(mMatrixGroup)
                    }
                    else
                    {

                        //иначе записываем в кэщ бд
                        mMatrixDataBaseModel.addToCache(mMatrixGroup)
                    }
                }

                viewState.addToRecyclerView(mMatrixGroup)

        }
    }


    @SuppressLint("StaticFieldLeak")
    fun onMinusClick(firstMatrix : String , secondMatrix : String)
    {

        uiScope.launch(Dispatchers.Main + errorHandler)
        {


                val task: Deferred<MatrixGroup> = async(Dispatchers.IO) {

                    //сохраняем время начала операции
                    val time = java.util.GregorianCalendar()
                    time.timeInMillis = System.currentTimeMillis()

                    //вычитаем матрицы
                    val result = mMatrixModel.minus(
                        mMatrixModel.createMatrix(firstMatrix),
                        mMatrixModel.createMatrix(secondMatrix)
                    )


                    //устанавливаем время начала операции
                    result.time = time

                    result
                }

                val mMatrixGroup = task.await()

                async(Dispatchers.IO)
                {
                    if (mSettingsModel.getMatrixConsisten())
                    {
                        //записываем в бд
                        mMatrixDataBaseModel.insert(mMatrixGroup)
                    }
                    else
                    {

                        //иначе записываем в кэщ бд
                        mMatrixDataBaseModel.addToCache(mMatrixGroup)
                    }
                }

                viewState.addToRecyclerView(mMatrixGroup)
        }
    }


        @SuppressLint("StaticFieldLeak")
        fun onTimesClick(firstMatrix: String, secondMatrix: String)
        {
            uiScope.launch(Dispatchers.Main + errorHandler)
            {


                    val task: Deferred<MatrixGroup> = async(Dispatchers.IO) {

                        //сохраняем время начала операции
                        val time = java.util.GregorianCalendar()
                        time.timeInMillis = System.currentTimeMillis()

                        //умнажаем матрицы
                        val result = mMatrixModel.times(
                            mMatrixModel.createMatrix(firstMatrix),
                            mMatrixModel.createMatrix(secondMatrix)
                        )

                        //устанавливаем время начала операции
                        result.time = time

                        result
                    }

                    val mMatrixGroup = task.await()

                    async(Dispatchers.IO)
                    {
                        if (mSettingsModel.getMatrixConsisten())
                        {
                            //записываем в бд
                            mMatrixDataBaseModel.insert(mMatrixGroup)
                        }
                        else
                        {

                            //иначе записываем в кэщ бд
                            mMatrixDataBaseModel.addToCache(mMatrixGroup)
                        }
                    }

                    viewState.addToRecyclerView(mMatrixGroup)
            }
        }



        @SuppressLint("StaticFieldLeak")
        fun onInversClick(firstMatrix: String)
        {

            uiScope.launch(Dispatchers.Main + errorHandler)
            {

                    val task: Deferred<MatrixGroup> = async(Dispatchers.IO) {

                        //сохраняем время начала операции
                        val time = java.util.GregorianCalendar()
                        time.timeInMillis = System.currentTimeMillis()

                        //инвертируем матрицу
                        val result = mMatrixModel.inverse(mMatrixModel.createMatrix(firstMatrix))


                        //устанавливаем время начала операции
                        result.time = time

                        result
                    }

                    val mMatrixGroup = task.await()

                    async(Dispatchers.IO)
                    {
                        if (mSettingsModel.getMatrixConsisten())
                        {
                            //записываем в бд
                            mMatrixDataBaseModel.insert(mMatrixGroup)
                        }
                        else
                        {

                            //иначе записываем в кэщ бд
                            mMatrixDataBaseModel.addToCache(mMatrixGroup)
                        }
                    }

                    viewState.addToRecyclerView(mMatrixGroup)
            }
        }



            @SuppressLint("StaticFieldLeak")
            fun onDeterminantClick(firstMatrix: String)
            {

                uiScope.launch(Dispatchers.Main + errorHandler)
                {

                        val task: Deferred<MatrixGroup> = async(Dispatchers.IO) {

                            //сохраняем время начала операции
                            val time = java.util.GregorianCalendar()
                            time.timeInMillis = System.currentTimeMillis()

                            //считаем определитель
                            val result = mMatrixModel.determinant(mMatrixModel.createMatrix(firstMatrix))


                            //устанавливаем время начала операции
                            result.time = time

                            result
                        }

                        val mMatrixGroup = task.await()

                        async(Dispatchers.IO)
                        {
                            if (mSettingsModel.getMatrixConsisten())
                            {
                                //записываем в бд
                                mMatrixDataBaseModel.insert(mMatrixGroup)
                            }
                            else
                            {

                                //иначе записываем в кэщ бд
                                mMatrixDataBaseModel.addToCache(mMatrixGroup)
                            }
                        }

                        viewState.addToRecyclerView(mMatrixGroup)
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


                //загрузка из базы данных сохраненных результатов
                @SuppressLint("StaticFieldLeak")
                fun onLoadSavedInstance()
                {
                    object : AsyncTask<Void, Void, List<MatrixGroup>>()
                    {
                        override fun doInBackground(vararg params: Void?): List<MatrixGroup>
                        {
                            return mMatrixDataBaseModel.selectAll().reversed()
                        }

                        override fun onPostExecute(result: List<MatrixGroup>?)
                        {
                            viewState.setRecyclerViewArrayList(ArrayList(result))
                        }
                    }.execute()

                }


                //обработчик ошибок
                val errorHandler = CoroutineExceptionHandler(handler = { _, error ->
                    Log.d("ExceptionHandler@" , error.toString())
                    viewState.showToast(error.toString().substringAfter(':'))
                })
            }