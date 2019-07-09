package com.dev.smurf.highmathcalculator

//import android.support.design.widget.NavigationView
//import com.google.android.material.navigation.NavigationView
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.dev.smurf.highmathcalculator.R.id.*
import com.dev.smurf.highmathcalculator.mvp.presenters.MainPresenter
import com.dev.smurf.highmathcalculator.mvp.views.MainViewInterface
import com.dev.smurf.highmathcalculator.ui.fragments.MatrixFragment
import com.dev.smurf.highmathcalculator.ui.fragments.PolinomFragment
import com.dev.smurf.highmathcalculator.ui.fragments.SettingFragment
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : com.dev.smurf.highmathcalculator.moxyTmpAMdroisdXSupport.MvpAppCompatActivity(), MainViewInterface {

    //добовляем mainPresenter
    @InjectPresenter
    lateinit var mMainPresenter : MainPresenter

    //фрагмент с матрицами
    private lateinit var mMatrixFragment : MatrixFragment

    //фрагмент с полиномом
    private lateinit var mPolinomFragment : PolinomFragment

    //фрагмент с настройками
    private lateinit var mSettingFragment: SettingFragment

    //менеджер фрагментов
    private lateinit var mFragmentTransaction: androidx.fragment.app.FragmentTransaction

    //переменная для выезжающего дровера
    private lateinit var mToggler : ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFragments()

        mToggler  = ActionBarDrawerToggle(this, matrixDrawerLayout, R.string.open ,R.string.close)

        matrixDrawerLayout.addDrawerListener(mToggler)

        mToggler.syncState()

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //обработчик нажатия в навигационном дровере
        matrixNavigationView.setNavigationItemSelectedListener( object : NavigationView.OnNavigationItemSelectedListener
        {
            override fun onNavigationItemSelected(item: MenuItem): Boolean
            {
                when(item.itemId)
                {
                    matrixCalcBtn->
                    {
                        //setMatrixFragment()
                        mMainPresenter.setMatrixFragment()
                        return true
                    }
                    polinomicCalculationBtn->
                    {
                        //setPolinomFragment()
                        mMainPresenter.setPolinonFragment()
                        return true
                    }
                    settingsBtn->
                    {
                        mMainPresenter.setSettingsFragment()
                        return true
                    }
                    aboutBtn->
                    {
                        return true
                    }
                    else ->return false
                }
            }
        })

    }

    //инициализация фрагментов
    fun initFragments()
    {
        mMatrixFragment = MatrixFragment()
        mPolinomFragment = PolinomFragment()
        mSettingFragment = SettingFragment()
        mFragmentTransaction = supportFragmentManager.beginTransaction()
                //fragmentManager.beginTransaction()
        mFragmentTransaction.add(R.id.fragmentFrame,mMatrixFragment)
        mFragmentTransaction.commit()
    }


    //Установка фрагмента с матрицами
    override fun setMatrixFragment()
    {

        //если фрагмент не утсновлен
        if(!mMatrixFragment.isInLayout)
        {
            //начинаем транзакцию
            mFragmentTransaction = supportFragmentManager.beginTransaction()
                    //fragmentManager.beginTransaction()

            //заменем установленный фрагмент на матричный фрагмент
            mFragmentTransaction.replace(R.id.fragmentFrame, mMatrixFragment)

            //добавляем заменный фрагмент в стек
            mFragmentTransaction.addToBackStack(null)

            //применяем транзакцию
            mFragmentTransaction.commit()
        }

        //закрываем дровер
        matrixDrawerLayout.closeDrawers()
    }

    //установка фрагмента с полиномами
    override fun setPolinomFragment()
    {
        //если фрагмент не установлен
        if (!mPolinomFragment.isInLayout)
        {
            //начинаем транзакцию
            mFragmentTransaction = supportFragmentManager.beginTransaction()
                    //fragmentManager.beginTransaction()

            //заменяем установленный фрагмент на фрагмент с полиномами
            mFragmentTransaction.replace(R.id.fragmentFrame, mPolinomFragment)

            //добавляем заменяемый фрагмент в стек
            mFragmentTransaction.addToBackStack(null)

            //применяем транзакцию
            mFragmentTransaction.commit()
        }

        //закрываем дровер
        matrixDrawerLayout.closeDrawers()
    }


    //утсановка фрагмента с настройками
    override fun setSettingsFragment()
    {
        //если фрагмент не установлен
        if(!mSettingFragment.isInLayout)
        {
            //начинаем транзакцию
            mFragmentTransaction = supportFragmentManager.beginTransaction()
                    //fragmentManager.beginTransaction()

            //заменем установленный фрагмент на фрагмент настроек
            mFragmentTransaction.replace(R.id.fragmentFrame, mSettingFragment)

            //добовляем заменный фрагмент в бэкстек
            mFragmentTransaction.addToBackStack(null)

            //применяем транзакцию
            mFragmentTransaction.commit()
        }

        //закрываем дровер
        matrixDrawerLayout.closeDrawers()
    }


    //выбор комнонета в дровере
    override fun onOptionsItemSelected(item: MenuItem?): Boolean
    {
        if(mToggler.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
    }

}
