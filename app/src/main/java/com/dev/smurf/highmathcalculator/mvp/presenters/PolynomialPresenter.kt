package com.dev.smurf.highmathcalculator.mvp.presenters

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.dev.smurf.highmathcalculator.CalculatorApplication
import com.dev.smurf.highmathcalculator.mvp.models.PolynomialDataBaseModel
import com.dev.smurf.highmathcalculator.mvp.models.PolynomialModel
import com.dev.smurf.highmathcalculator.mvp.models.SettingsModel
import com.dev.smurf.highmathcalculator.mvp.views.PolynomialViewInterface
import com.example.smurf.mtarixcalc.PolynomialGroup
import kotlinx.coroutines.*
import org.jetbrains.anko.doAsync
import javax.inject.Inject


@InjectViewState
class PolynomialPresenter : MvpPresenter<PolynomialViewInterface>()
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

    val supJob = SupervisorJob()

    //корутин скоп для ui
    private val uiScope = CoroutineScope(Dispatchers.Main + supJob)

    //corutine scope для работы с памятью
    private val ioScope = CoroutineScope(Dispatchers.IO + supJob)

    //обработчик ошибок
    val errorHandler = CoroutineExceptionHandler(handler = { _, error ->
        Log.d("ExceptionHandler@", error.toString())
        //for(i in error.stackTrace)Log.d("ExceptionHandlerStack@",i.lineNumber.toString() + ":"+i.className+"."+i.methodName)
        viewState.showToast(error.toString().substringAfter(':'))
    })


    /*
     * Релизация операций с полиномами
     */

    //нажатие кнопки плюс
    //@SuppressLint("StaticFieldLeak")
    fun onPlusClick(left: String, right: String)
    {
        uiScope.launch(Dispatchers.Main + errorHandler)
        {

            val result = withContext(Dispatchers.IO) {

                //сохранение время начала операции
                val time = java.util.GregorianCalendar()
                time.timeInMillis = System.currentTimeMillis()


                //складываем полиномы
                val result = mPolynomialModel.plus(left = left, right = right)

                //устанавливаем время страта операции
                result.time = time


                result
            }


            addToDb(result)

            viewState.addToPolynomialRecyclerView(result)


        }
    }


    //нажатие на кнопку минус
    //@SuppressLint("StaticFieldLeak")
    fun onMinusClick(left: String, right: String)
    {
        uiScope.launch(Dispatchers.Main + errorHandler)
        {
            val result = withContext(Dispatchers.IO) {

                //сохранение время начала операции
                val time = java.util.GregorianCalendar()
                time.timeInMillis = System.currentTimeMillis()

                //складываем полиномы
                val result = mPolynomialModel.minus(left = left, right = right)

                //устанавливаем время страта операции
                result.time = time

                result
            }


            addToDb(result)

            viewState.addToPolynomialRecyclerView(result)


        }
    }

    //нажатие на кнопку умножения
    //@SuppressLint("StaticFieldLeak")
    fun onTimesClick(left: String, right: String)
    {
        uiScope.launch(Dispatchers.Main + errorHandler)
        {

            val result = withContext(Dispatchers.IO) {

                //сохранение время начала операции
                val time = java.util.GregorianCalendar()
                time.timeInMillis = System.currentTimeMillis()

                //складываем полиномы
                val result = mPolynomialModel.times(left = left, right = right)

                //устанавливаем время страта операции
                result.time = time

                result
            }

            addToDb(result)

            viewState.addToPolynomialRecyclerView(result)

        }
    }

    //нажатие на кнопку деления
    //@SuppressLint("StaticFieldLeak")
    fun onDivisionClick(left: String, right: String)
    {
        uiScope.launch(Dispatchers.Main + errorHandler)
        {
            val result = withContext(Dispatchers.IO) {

                //сохранение время начала операции
                val time = java.util.GregorianCalendar()
                time.timeInMillis = System.currentTimeMillis()

                //складываем полиномы
                val result = mPolynomialModel.division(left = left, right = right)

                //устанавливаем время страта операции
                result.time = time

                result
            }

            addToDb(result)

            viewState.addToPolynomialRecyclerView(result)

        }
    }

    //нажатие на кнопку решения
    //@SuppressLint("StaticFieldLeak")
    fun onRootsOfClick(left: String)
    {
        viewState.showToast("Work in progress")
        /*uiScope.launch(Dispatchers.Main + errorHandler)
                {
                    val task: Deferred<PolynomialGroup> = async(Dispatchers.IO) {

                        //сохранение время начала операции
                        val time = java.util.GregorianCalendar()
                        time.timeInMillis = System.currentTimeMillis()

                        //складываем полиномы
                        val result = mPolynomialModel.times(left = left, right = right)

                        //устанавливаем время страта операции
                        result.time = time

                        result
                    }

                    val result = task.await()

                    doAsync {

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

                    viewState.addToPolynomialRecyclerView(result)

                }*/

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
    @SuppressLint(value = ["StaticFieldLeak"])
    fun onLoadSavedInstance()
    {
        Log.d("dbTrace@","start load db")
        object : AsyncTask<Void, Void, List<PolynomialGroup>>()
        {

            override fun doInBackground(vararg params: Void?): List<PolynomialGroup>
            {
                return mPolynomialDataBaseModel.selectAll().reversed()
            }

            override fun onPostExecute(result: List<PolynomialGroup>?)
            {
                Log.d("dbTrace@","show loaded db")
                for(i in result!!) Log.d("dbTrace@", i.polLeftPolynomial.toString())
                viewState.setRecyclerViewList(ArrayList(result))
            }
        }.execute()
    }


    fun checkImageMode() = mSettingsModel.getPolynomialHolderConsistens()

    private fun addToDb(result : PolynomialGroup)
    {
        ioScope.launch(Dispatchers.IO + errorHandler){

            //если включена запись в бд
            if (mSettingsModel.getPolinomConsistens())
            {
                Log.d("dbTrace@","add to db")
                //записываем в бд
                mPolynomialDataBaseModel.insert(result)

            }
            else
            {
                Log.d("dbTrace@","add to cache")
                //пишем в сache бд
                mPolynomialDataBaseModel.addToCache(result)

            }
        }
    }

}