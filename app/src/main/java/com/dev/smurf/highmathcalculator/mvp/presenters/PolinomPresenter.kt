package com.dev.smurf.highmathcalculator.mvp.presenters

import android.os.AsyncTask
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.dev.smurf.highmathcalculator.CalculatorApplication
import com.dev.smurf.highmathcalculator.mvp.models.PolinomDataBaseModel
import com.dev.smurf.highmathcalculator.mvp.models.PolinomModel
import com.dev.smurf.highmathcalculator.mvp.views.PolinomViewInterface
import com.example.smurf.mtarixcalc.PolinomGroup
import javax.inject.Inject

@InjectViewState
class PolinomPresenter : MvpPresenter<PolinomViewInterface>()
{

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

    //нажатие кнопки плюс
    fun onPlusClick(left : String , right : String )
    {
        try
        {

          object : AsyncTask<Void , Void , PolinomGroup>(){
              override fun doInBackground(vararg params: Void?): PolinomGroup
              {
                  val time = java.util.GregorianCalendar()
                  time.timeInMillis = System.currentTimeMillis()
                  val result = mPolinomModel.plus(left = left , right = right)
                  result.time = time
                  return result
              }

              override fun onPostExecute(result: PolinomGroup)
              {
                  viewState.addToPolinomRecyclerView(result)
              }
          }

            //viewState.addToPolinomRecyclerView( mPolinomModel.plus(left = left , right = right) )
        }
        catch (e : Exception)
        {
            viewState.showToast(e.toString().substringAfter(':'))
        }
    }

    //нажатие на кнопку минус
    fun onMinusClick(left : String , right : String)
    {
        try
        {
            object : AsyncTask<Void , Void , PolinomGroup>() {
                override fun doInBackground(vararg params: Void?): PolinomGroup
                {
                    val time = java.util.GregorianCalendar()
                    time.timeInMillis = System.currentTimeMillis()
                    val result = mPolinomModel.minus(left = left , right = right)
                    result.time = time
                    return result
                }

                override fun onPostExecute(result: PolinomGroup)
                {
                    viewState.addToPolinomRecyclerView(result)
                }
            }.execute()
            //viewState.addToPolinomRecyclerView( mPolinomModel.minus(left = left , right = right) )
        }
        catch (e : Exception)
        {
            viewState.showToast(e.toString().substringAfter(':'))
        }
    }

    //нажатие на кнопку умножения
    fun onTimesClick(left: String, right: String)
    {
        try
        {
            object : AsyncTask<Void , Void , PolinomGroup>() {
                override fun doInBackground(vararg params: Void?): PolinomGroup
                {
                    val time = java.util.GregorianCalendar()
                    time.timeInMillis = System.currentTimeMillis()
                    val result = mPolinomModel.times(left = left , right = right)
                    result.time = time
                    return result
                }

                override fun onPostExecute(result: PolinomGroup)
                {
                    viewState.addToPolinomRecyclerView(result)
                }
            }.execute()
            //viewState.addToPolinomRecyclerView( mPolinomModel.times(left = left , right = right) )
        }
        catch (e : Exception)
        {
            viewState.showToast(e.toString().substringAfter(':'))
        }
    }

    //нажатие на кнопку деления
    fun onDivisionClick(left: String , right: String)
    {
        try
        {
            object : AsyncTask<Void , Void , PolinomGroup>() {
                override fun doInBackground(vararg params: Void?): PolinomGroup
                {
                    val time = java.util.GregorianCalendar()
                    time.timeInMillis = System.currentTimeMillis()
                    val result = mPolinomModel.division(left = left , right = right)
                    result.time = time
                    return result
                }

                override fun onPostExecute(result: PolinomGroup)
                {
                    viewState.addToPolinomRecyclerView(result)
                }
            }.execute()
            //viewState.addToPolinomRecyclerView( mPolinomModel.division(left = left , right = right) )
        }
        catch (e : Exception)
        {
            viewState.showToast(e.toString().substringAfter(':'))
        }
    }

    //нажатие на кнопку решения
    fun onRootsOfClick(left : String)
    {
        try
        {
            object : AsyncTask<Void , Void , PolinomGroup>() {
                override fun doInBackground(vararg params: Void?): PolinomGroup
                {
                    val time = java.util.GregorianCalendar()
                    time.timeInMillis = System.currentTimeMillis()
                    val result = mPolinomModel.getSolved(left)
                    result.time = time
                    return result
                }

                override fun onPostExecute(result: PolinomGroup)
                {
                    viewState.addToPolinomRecyclerView(result)
                }
            }.execute()
           // viewState.addToPolinomRecyclerView( mPolinomModel.getSolved(left) )
        }
        catch (e : Exception)
        {
            viewState.showToast(e.toString().substringAfter(':'))
        }
    }

}