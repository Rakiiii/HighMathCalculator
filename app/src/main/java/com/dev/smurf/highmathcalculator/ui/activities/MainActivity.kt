package com.dev.smurf.highmathcalculator

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.dev.smurf.highmathcalculator.mvp.presenters.MainPresenter
import com.dev.smurf.highmathcalculator.mvp.views.MainViewInterface
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter


class MainActivity : MvpAppCompatActivity(), MainViewInterface {

    //добовляем mainPresenter
    @InjectPresenter
    lateinit var mMainPresenter : MainPresenter


    private lateinit var mNavigationController : NavController



    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = ContextCompat.getColor(this,R.color.white)

        setContentView(R.layout.activity_main)

        mNavigationController = Navigation.findNavController(this,R.id.nav_host_fragment)

        supportActionBar?.hide()

        /*
        bottomNavView.setOnNavigationItemSelectedListener (object : BottomNavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean
            {
                when(item.itemId)
                {
                    R.id.matrixCalcBtn->
                    {
                        //setMatrixFragment()
                        mMainPresenter.setMatrixFragment()
                        return true
                    }
                    R.id.polinomicCalculationBtn->
                    {
                        //setPolinomFragment()
                        mMainPresenter.setPolinonFragment()
                        return true
                    }
                    R.id.settingsBtn->
                    {
                        mMainPresenter.setSettingsFragment()
                        return true
                    }
                    else ->return false
                }
            }
        })*/
        bottomNavView.setNavigationChangeListener{ view, position ->
            when( position){
                0->{
                    mMainPresenter.setMatrixFragment()
                }
                1->{
                    mMainPresenter.setPolinonFragment()
                }
            }
        }

        settingsButton.setOnClickListener {
            mMainPresenter.setSettingsFragment()
        }

    }




    //Установка фрагмента с матрицами
    override fun setMatrixFragment()
    {
        //mNavigationController.navigate(R.id.matrixFragment)
        mNavigationController.navigate(R.id.action_polinomFragment_to_matrixFragment)
    }

    //установка фрагмента с полиномами
    override fun setPolinomFragment()
    {
        //mNavigationController.navigate(R.id.polinomFragment)
        mNavigationController.navigate(R.id.action_matrixFragment_to_polinomFragment)

    }


    //утсановка фрагмента с настройками
    override fun setSettingsFragment()
    {
        mNavigationController.navigate(R.id.settingFragment)

    }

}
