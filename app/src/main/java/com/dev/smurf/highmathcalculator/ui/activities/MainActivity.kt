package com.dev.smurf.highmathcalculator

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.dev.smurf.highmathcalculator.CanvasExtension.CanvasRenderSpecification
import com.dev.smurf.highmathcalculator.CanvasExtension.drawFractions
import com.dev.smurf.highmathcalculator.Numbers.Fraction
import com.dev.smurf.highmathcalculator.PaintExtension.getFractionSize
import com.dev.smurf.highmathcalculator.mvp.presenters.MainPresenter
import com.dev.smurf.highmathcalculator.mvp.views.MainViewInterface
import com.dev.smurf.highmathcalculator.ui.adapters.ViewPagersAdapters.ViewPagerFragmentStateAdapter
import com.dev.smurf.highmathcalculator.ui.fragments.matrixFragment.MatrixFragment
import com.dev.smurf.highmathcalculator.ui.fragments.polynomialFragment.PolynomialFragment
import com.dev.smurf.highmathcalculator.ui.fragments.settingsFragment.SettingBottomSheetDialogFragment
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import org.jetbrains.anko.imageBitmap


class MainActivity : MvpAppCompatActivity(), MainViewInterface,
    SettingBottomSheetDialogFragment.onFragmentInteractionListener
{

    //добовляем mainPresenter
    @InjectPresenter
    lateinit var mMainPresenter: MainPresenter


    //private lateinit var mNavigationController : NavController

    private val mSettingBottomSheetDialogFragment =
        SettingBottomSheetDialogFragment()

    private val mViewPagerFragmentStateAdapter =
        ViewPagerFragmentStateAdapter(
            this
        )

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        setContentView(R.layout.activity_main)

        //mNavigationController = Navigation.findNavController(this,R.id.nav_host_fragment)

        mViewPagerFragmentStateAdapter.addNewFragment(MatrixFragment())
        mViewPagerFragmentStateAdapter.addNewFragment(PolynomialFragment())

        mainViewPager.adapter = mViewPagerFragmentStateAdapter


        supportActionBar?.hide()

        bottomNavView.setNavigationChangeListener { _, position ->
            when (position)
            {
                0 ->
                {
                    mMainPresenter.setMatrixFragment()
                }
                1 ->
                {
                    mMainPresenter.setPolinonFragment()
                }
            }
        }

        settingsButton.setOnClickListener {
            mMainPresenter.setSettingsFragment()
        }

        //mainViewPager.isUserInputEnabled=false
        mainViewPager.setOnTouchListener { _, _ ->
            return@setOnTouchListener true
        }

        mainViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback()
        {
            override fun onPageSelected(position: Int)
            {
                super.onPageSelected(position)
                when (position)
                {
                    0 -> bottomNavView.setCurrentActiveItem(0)
                    1 -> bottomNavView.setCurrentActiveItem(1)
                }
            }
        })

    }


    //Установка фрагмента с матрицами
    override fun setMatrixFragment()
    {
        mainViewPager.beginFakeDrag()
        val point = Point()
        windowManager.defaultDisplay.getSize(point)
        mainViewPager.fakeDragBy((point.x.toFloat() + 10.0f))
        mainViewPager.endFakeDrag()

        //mainViewPager.setCurrentItem(0,false)
    }

    //установка фрагмента с полиномами
    override fun setPolinomFragment()
    {
        mainViewPager.beginFakeDrag()
        val point = Point()
        windowManager.defaultDisplay.getSize(point)
        mainViewPager.fakeDragBy(-(point.x.toFloat() + 10.0f))
        mainViewPager.endFakeDrag()
        //mainViewPager.setCurrentItem(1,false)

    }


    //утсановка фрагмента с настройками
    override fun setSettingsFragment()
    {
        mSettingBottomSheetDialogFragment.show(
            supportFragmentManager,
            mSettingBottomSheetDialogFragment.tag
        )
    }

    override fun updateSettings()
    {
        mViewPagerFragmentStateAdapter.callUpdateSettings()
    }

}
