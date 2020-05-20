package com.dev.smurf.highmathcalculator

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.dev.smurf.highmathcalculator.mvp.presenters.MainPresenter
import com.dev.smurf.highmathcalculator.mvp.views.MainViewInterface
import com.dev.smurf.highmathcalculator.ui.adapters.ViewPagerFragmentStateAdapter
import com.dev.smurf.highmathcalculator.ui.fragments.matrixFragment.MatrixFragment
import com.dev.smurf.highmathcalculator.ui.fragments.polynomialFragment.PolynomialFragment
import com.dev.smurf.highmathcalculator.ui.fragments.settingsFragment.SettingBottomSheetDialogFragment
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter


class MainActivity : MvpAppCompatActivity(), MainViewInterface , SettingBottomSheetDialogFragment.onFragmentInteractionListener{

    //добовляем mainPresenter
    @InjectPresenter
    lateinit var mMainPresenter : MainPresenter


    //private lateinit var mNavigationController : NavController

    private val mSettingBottomSheetDialogFragment =
        SettingBottomSheetDialogFragment()

    private val mViewPagerFragmentStateAdapter = ViewPagerFragmentStateAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = ContextCompat.getColor(this,R.color.white)

        setContentView(R.layout.activity_main)

        //mNavigationController = Navigation.findNavController(this,R.id.nav_host_fragment)

        mViewPagerFragmentStateAdapter.addNewFragment(MatrixFragment())
        mViewPagerFragmentStateAdapter.addNewFragment(PolynomialFragment())

        mainViewPager.adapter = mViewPagerFragmentStateAdapter

        supportActionBar?.hide()

        bottomNavView.setNavigationChangeListener{ _, position ->
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

        mainViewPager.setOnTouchListener { _, _ ->
            return@setOnTouchListener true
        }

    }




    //Установка фрагмента с матрицами
    override fun setMatrixFragment()
    {
        mainViewPager.setCurrentItem(0,true)
    }

    //установка фрагмента с полиномами
    override fun setPolinomFragment()
    {
        mainViewPager.setCurrentItem(1,true)
    }


    //утсановка фрагмента с настройками
    override fun setSettingsFragment()
    {
        mSettingBottomSheetDialogFragment.show(supportFragmentManager,mSettingBottomSheetDialogFragment.tag)
    }

    override fun updateSettings()
    {
        mViewPagerFragmentStateAdapter.callUpdateSettings()
    }
}
