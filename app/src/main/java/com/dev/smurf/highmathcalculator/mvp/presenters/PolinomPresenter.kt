package com.dev.smurf.highmathcalculator.mvp.presenters

import android.annotation.SuppressLint
import android.os.AsyncTask
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.dev.smurf.highmathcalculator.CalculatorApplication
import com.dev.smurf.highmathcalculator.mvp.models.PolinomDataBaseModel
import com.dev.smurf.highmathcalculator.mvp.models.PolinomModel
import com.dev.smurf.highmathcalculator.mvp.models.SettingsModel
import com.dev.smurf.highmathcalculator.mvp.views.PolinomViewInterface
import com.example.smurf.mtarixcalc.PolinomGroup
import org.jetbrains.anko.doAsync
import javax.inject.Inject

@InjectViewState
class PolinomPresenter : MvpPresenter<PolinomViewInterface>()
{
    /*
     * вставка зависимостей
     */

    @Inject
    lateinit var mPolinomDataBaseModel: PolinomDataBaseModel
    init
    {
        CalculatorApplication.graph.inject(this)
    }

    @Inject
    lateinit var mPolinomModel : PolinomModel

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


    /*
     * Релизация операций с полиномами
     */

    //нажатие кнопки плюс
    @SuppressLint("StaticFieldLeak")
    fun onPlusClick(left : String , right : String )
    {
        try
        {

          object : AsyncTask<Void , Void , PolinomGroup>(){
              override fun doInBackground(vararg params: Void?): PolinomGroup
              {
                  //сохранение время начала операции
                  val time = java.util.GregorianCalendar()
                  time.timeInMillis = System.currentTimeMillis()

                  //складываем полиномы
                  val result = mPolinomModel.plus(left = left , right = right)

                  //устанавливаем время страта операции
                  result.time = time

                  doAsync {

                      //если включена запись в бд
                      if(mSettingsModel.getPolinomConsisten())
                      {

                          //записываем в бд
                          mPolinomDataBaseModel.insert(result)

                      }
                      else
                      {
                          //пишем в сache бд
                          mPolinomDataBaseModel.addToCache(result)

                      }
                  }

                  return result
              }

              override fun onPostExecute(result: PolinomGroup)
              {
                  viewState.addToPolinomRecyclerView(result)
              }
          }
        }
        catch (e : Exception)
        {
            viewState.showToast(e.toString().substringAfter(':'))
        }
    }

    //нажатие на кнопку минус
    @SuppressLint("StaticFieldLeak")
    fun onMinusClick(left : String , right : String)
    {
        try
        {
            object : AsyncTask<Void , Void , PolinomGroup>() {
                override fun doInBackground(vararg params: Void?): PolinomGroup
                {
                    //сохранеяем время страта операции
                    val time = java.util.GregorianCalendar()
                    time.timeInMillis = System.currentTimeMillis()

                    //вычитаем полиномы
                    val result = mPolinomModel.minus(left = left , right = right)

                    //устанавливаем время старта операции
                    result.time = time

                    doAsync {

                        //если включена запись в бд
                        if(mSettingsModel.getPolinomConsisten())
                        {

                            //записываем в бд
                            mPolinomDataBaseModel.insert(result)

                        }
                        else
                        {
                            //пишем в сache бд
                            mPolinomDataBaseModel.addToCache(result)

                        }
                    }


                    return result
                }

                override fun onPostExecute(result: PolinomGroup)
                {
                    viewState.addToPolinomRecyclerView(result)
                }
            }.execute()
        }
        catch (e : Exception)
        {
            viewState.showToast(e.toString().substringAfter(':'))
        }
    }

    //нажатие на кнопку умножения
    @SuppressLint("StaticFieldLeak")
    fun onTimesClick(left: String, right: String)
    {
        try
        {
            object : AsyncTask<Void , Void , PolinomGroup>() {
                override fun doInBackground(vararg params: Void?): PolinomGroup
                {
                    //сохраняем время начала операции
                    val time = java.util.GregorianCalendar()
                    time.timeInMillis = System.currentTimeMillis()

                    //умножаем полиномы
                    val result = mPolinomModel.times(left = left , right = right)

                    //устанавливаем врема старта операции
                    result.time = time

                    doAsync {

                        //если включена запись в бд
                        if(mSettingsModel.getPolinomConsisten())
                        {

                            //записываем в бд
                            mPolinomDataBaseModel.insert(result)

                        }
                        else
                        {
                            //пишем в сache бд
                            mPolinomDataBaseModel.addToCache(result)

                        }
                    }

                    return result
                }

                override fun onPostExecute(result: PolinomGroup)
                {
                    viewState.addToPolinomRecyclerView(result)
                }
            }.execute()
        }
        catch (e : Exception)
        {
            viewState.showToast(e.toString().substringAfter(':'))
        }
    }

    //нажатие на кнопку деления
    @SuppressLint("StaticFieldLeak")
    fun onDivisionClick(left: String , right: String)
    {
        try
        {
            object : AsyncTask<Void , Void , PolinomGroup>() {
                override fun doInBackground(vararg params: Void?): PolinomGroup
                {
                    //сохраняем время начала операции
                    val time = java.util.GregorianCalendar()
                    time.timeInMillis = System.currentTimeMillis()

                    //делим полиномы
                    val result = mPolinomModel.division(left = left , right = right)

                    //устанавливаем время начала операции
                    result.time = time

                    doAsync {

                        //если включена запись в бд
                        if(mSettingsModel.getPolinomConsisten())
                        {

                            //записываем в бд
                            mPolinomDataBaseModel.insert(result)

                        }
                        else
                        {
                            //пишем в сache бд
                            mPolinomDataBaseModel.addToCache(result)

                        }
                    }

                    return result
                }

                override fun onPostExecute(result: PolinomGroup)
                {
                    viewState.addToPolinomRecyclerView(result)
                }
            }.execute()
        }
        catch (e : Exception)
        {
            viewState.showToast(e.toString().substringAfter(':'))
        }
    }

    //нажатие на кнопку решения
    @SuppressLint("StaticFieldLeak")
    fun onRootsOfClick(left : String)
    {
        try
        {
            object : AsyncTask<Void , Void , PolinomGroup>() {
                override fun doInBackground(vararg params: Void?): PolinomGroup
                {
                    //сохраняем время начала операции
                    val time = java.util.GregorianCalendar()
                    time.timeInMillis = System.currentTimeMillis()

                    //решаем уравнение описываеммое полиномом
                    val result = mPolinomModel.getSolved(left)

                    //устанавливаем время начала выполнения операции
                    result.time = time

                    doAsync {

                        //если включена запись в бд
                        if(mSettingsModel.getPolinomConsisten())
                        {

                            //записываем в бд
                            mPolinomDataBaseModel.insert(result)

                        }
                        else
                        {
                            //пишем в сache бд
                            mPolinomDataBaseModel.addToCache(result)

                        }
                    }

                    return result
                }

                override fun onPostExecute(result: PolinomGroup)
                {
                    viewState.addToPolinomRecyclerView(result)
                }
            }.execute()
        }
        catch (e : Exception)
        {
            viewState.showToast(e.toString().substringAfter(':'))
        }
    }

    /*
     * Реализация работы с бд
     */

    //удаление из базы данных
    fun deleteFromDb(polinomGroup: PolinomGroup)
    {
        doAsync {
            mPolinomDataBaseModel.delete(polinomGroup)
            mPolinomDataBaseModel.deleteFromDbCache(polinomGroup)
        }
    }

    //загрузка созраненного в бд состояния
    @SuppressLint(value = ["StaticFieldLeak"])
    fun onLoadSavedInstance()
    {
        object : AsyncTask<Void,Void,List<PolinomGroup>>(){

            override fun doInBackground(vararg params: Void?): List<PolinomGroup>
            {
                return mPolinomDataBaseModel.selectAll()
            }

            override fun onPostExecute(result: List<PolinomGroup>?)
            {
                viewState.setRecyclerViewList(ArrayList(result!!))
            }
        }.execute()
    }


}