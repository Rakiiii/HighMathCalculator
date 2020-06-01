package com.dev.smurf.highmathcalculator.ui.fragments.matrixFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels

import com.dev.smurf.highmathcalculator.R
import com.dev.smurf.highmathcalculator.ui.ViewModels.ListenerViewModel
import com.dev.smurf.highmathcalculator.ui.fragments.polynomialFragment.PolynomialButtonsGridFirstPageFragment
import kotlinx.android.synthetic.main.fragment_matrix_button_grid_first_page.*

class MatrixButtonGridFragmentFirstPage : Fragment()
{
    private val mListenerViewModel by viewModels<ListenerViewModel<onFragmentInteractionListener>>()

    private lateinit var listener : onFragmentInteractionListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_matrix_button_grid_first_page, container, false)
    }

    override fun onStart()
    {
        super.onStart()

        btnInvers.setOnClickListener {
            listener.btnInverseClicked()
        }

        btnDeterminant.setOnClickListener {
            listener.btnDeterminantClicked()
        }

        btnFirstTimesSecond.setOnClickListener {
            listener.btnFirstTimeSecondClicked()
        }

        btnSecondTimesFirst.setOnClickListener {
            listener.btnSecondTimesFirstClicked()
        }

        btnPlus.setOnClickListener {
            listener.btnPlusClicked()
        }

        btnMinus.setOnClickListener {
            listener.btnMinusClicked()
        }

        btnSwitchBtnFragment.setOnClickListener {
            listener.btnSwitchFPClicked()
        }
    }

    interface onFragmentInteractionListener
    {
        fun btnDeterminantClicked()
        fun btnInverseClicked()
        fun btnPlusClicked()
        fun btnMinusClicked()
        fun btnFirstTimeSecondClicked()
        fun btnSecondTimesFirstClicked()
        fun btnSwitchFPClicked()
    }

    override fun onPause()
    {
        mListenerViewModel.listener = listener
        super.onPause()
    }

    override fun onResume()
    {
        if (!::listener.isInitialized)listener = mListenerViewModel.listener
        super.onResume()
    }

    fun setListener(l : onFragmentInteractionListener) : MatrixButtonGridFragmentFirstPage
    {
        listener = l
        return this
    }

    class WrongListenerException : Exception()
    {
        override val message: String?
            get() = super.message + "Wrong Listener for MatrixButtonGridFragment"
    }
}
