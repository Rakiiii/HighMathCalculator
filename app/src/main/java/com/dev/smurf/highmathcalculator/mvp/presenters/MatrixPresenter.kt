package com.dev.smurf.highmathcalculator.mvp.presenters

import android.annotation.SuppressLint
import android.os.AsyncTask
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.dev.smurf.highmathcalculator.CalculatorApplication
import com.dev.smurf.highmathcalculator.mvp.models.MatrixDatabaseModel
import com.dev.smurf.highmathcalculator.mvp.models.MatrixModel
import com.dev.smurf.highmathcalculator.mvp.models.SettingsModel
import com.dev.smurf.highmathcalculator.mvp.views.MatrixViewInterface
import com.example.smurf.mtarixcalc.MatrixGroup
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




    /*
     * Реализация операций
     */

    @SuppressLint("StaticFieldLeak")
    fun onPlusClick(firstMatrix : String , secondMatrix : String)
    {
        try
        {

            object : AsyncTask<Void , Void , MatrixGroup>(){

                override fun doInBackground(vararg params: Void?): MatrixGroup
                {
                    //сохранение время начала операции
                    val time = java.util.GregorianCalendar()
                    time.timeInMillis = System.currentTimeMillis()

                    //складываем матрицы
                    val result = mMatrixModel.plus(mMatrixModel.createMatrix(firstMatrix) , mMatrixModel.createMatrix(secondMatrix))

                    //устанавливаем время старта операции
                    result.time = time


                    doAsync {
                        //если включено сохранение в бд
                        if (mSettingsModel.getMatrixConsisten())
                        {
                            //записываем в бд
                            mMatrixDataBaseModel.insert(result)
                        }
                        else
                        {

                            //иначе записываем в кэщ бд
                            mMatrixDataBaseModel.addToCache(result)
                        }
                    }
                    return result
                }

                override fun onPostExecute(result: MatrixGroup)
                {
                    viewState.addToRecyclerView(result)
                }
            }.execute()
        }catch (e : Exception)
        {
            viewState.showToast(e.toString())
        }
    }

    @SuppressLint("StaticFieldLeak")
    fun onMinusClick(firstMatrix : String , secondMatrix : String)
    {
        try
        {

            object : AsyncTask<Void , Void , MatrixGroup>(){

                override fun doInBackground(vararg params: Void?): MatrixGroup
                {
                    //сохраняем врема старта операции
                    val time = java.util.GregorianCalendar()
                    time.timeInMillis = System.currentTimeMillis()

                    //вычитаем матрицы
                    val result = mMatrixModel.minus(mMatrixModel.createMatrix(firstMatrix) , mMatrixModel.createMatrix(secondMatrix))

                    //утсанавливаем время начала операции
                    result.time = time

                    doAsync {
                        //если включено сохранение в бд
                        if (mSettingsModel.getMatrixConsisten())
                        {
                            //записываем в бд
                            mMatrixDataBaseModel.insert(result)
                        }
                        else
                        {

                            //иначе записываем в кэщ бд
                            mMatrixDataBaseModel.addToCache(result)
                        }
                    }
                    return result
                }

                override fun onPostExecute(result: MatrixGroup)
                {
                    viewState.addToRecyclerView(result)
                }
            }.execute()
        }catch (e : Exception)
        {
            viewState.showToast(e.toString())
        }
    }


    @SuppressLint("StaticFieldLeak")
    fun onTimesClick(firstMatrix : String , secondMatrix : String)
    {
        try
        {

            object : AsyncTask<Void , Void , MatrixGroup>(){

                override fun doInBackground(vararg params: Void?): MatrixGroup
                {
                    //сохраняем время начала операции
                    val time = java.util.GregorianCalendar()
                    time.timeInMillis = System.currentTimeMillis()

                    //умнажаем матрицы
                    val result = mMatrixModel.times(mMatrixModel.createMatrix(firstMatrix) , mMatrixModel.createMatrix(secondMatrix))

                    //устанавливаем время начала операции
                    result.time = time

                    doAsync {
                        //если включено сохранение в бд
                        if (mSettingsModel.getMatrixConsisten())
                        {
                            //записываем в бд
                            mMatrixDataBaseModel.insert(result)
                        }
                        else
                        {

                            //иначе записываем в кэщ бд
                            mMatrixDataBaseModel.addToCache(result)
                        }
                    }
                    return result
                    //return mMatrixModel.times(mMatrixModel.createMatrix(firstMatrix) , mMatrixModel.createMatrix(secondMatrix))
                }

                override fun onPostExecute(result: MatrixGroup)
                {
                    viewState.addToRecyclerView(result)
                }
            }.execute()

        }catch (e : Exception)
        {
            viewState.showToast(e.toString())
        }
        //viewState.addToRecyclerView(mMatrixModel.times(mMatrixModel.createMatrix(firstMatrix) , mMatrixModel.createMatrix(secondMatrix)))
    }


    @SuppressLint("StaticFieldLeak")
    fun onInversClick(firstMatrix: String)
    {
        try
        {

            object : AsyncTask<Void , Void , MatrixGroup>(){

                override fun doInBackground(vararg params: Void?): MatrixGroup
                {
                    //сохраняем время
                    val time = java.util.GregorianCalendar()
                    time.timeInMillis = System.currentTimeMillis()

                    //инвертируем матрицу
                    val result = mMatrixModel.inverse(mMatrixModel.createMatrix(firstMatrix))

                    //утсанавливаем время начала операции
                    result.time = time

                    doAsync {
                        //если включено сохранение в бд
                        if (mSettingsModel.getMatrixConsisten())
                        {
                            //записываем в бд
                            mMatrixDataBaseModel.insert(result)
                        }
                        else
                        {

                            //иначе записываем в кэщ бд
                            mMatrixDataBaseModel.addToCache(result)
                        }
                    }

                    return result
                }

                override fun onPostExecute(result: MatrixGroup)
                {
                    viewState.addToRecyclerView(result)
                }
            }.execute()
        }catch (e : Exception)
        {
            viewState.showToast(e.toString())
        }
    }


    @SuppressLint("StaticFieldLeak")
    fun onDeterminantClick(firstMatrix: String)
    {
        try
        {
            object : AsyncTask<Void , Void , MatrixGroup>(){

                override fun doInBackground(vararg params: Void?): MatrixGroup
                {
                    //сохраняем время операции
                    val time = java.util.GregorianCalendar()
                    time.timeInMillis = System.currentTimeMillis()

                    //считаем определитель
                    val result = mMatrixModel.determinant(mMatrixModel.createMatrix(firstMatrix))

                    //устанавливаем время старта операции
                    result.time = time

                    doAsync {
                        //если включено сохранение в бд
                        if(mSettingsModel.getMatrixConsisten())
                        {
                            //записываем в бд
                            mMatrixDataBaseModel.insert(result)
                        }else
                        {

                            //иначе записываем в кэщ бд
                            mMatrixDataBaseModel.addToCache(result)
                        }
                    }
                    return result
                }

                override fun onPostExecute(result: MatrixGroup)
                {
                    viewState.addToRecyclerView(result)
                }
            }.execute()
        }catch (e : Exception)
        {
            viewState.showToast(e.toString())
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
        }
    }


    //загрузка из базы данных сохраненных результатов
    @SuppressLint("StaticFieldLeak")
    fun onLoadSavedInstance()
    {
        object : AsyncTask<Void,Void,List<MatrixGroup>>(){
            override fun doInBackground(vararg params: Void?): List<MatrixGroup>
            {
                return mMatrixDataBaseModel.selectAll()
            }

            override fun onPostExecute(result: List<MatrixGroup>?)
            {
                viewState.setRecyclerViewArrayList(ArrayList(result))
            }
        }.execute()

    }
}