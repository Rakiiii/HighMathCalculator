package com.dev.smurf.highmathcalculator.ui.fragments.polynomialFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.dev.smurf.highmathcalculator.R
import kotlinx.android.synthetic.main.fragment_matrix_button_grid_first_page.*
import kotlinx.android.synthetic.main.fragment_polynomial_buttons_grid_first_page.*


class PolynomialButtonsGridFirstPageFragment : Fragment()
{

    private lateinit var listener: OnFragmentInteractionListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_polynomial_buttons_grid_first_page,
            container,
            false
        )
    }

    override fun onStart()
    {
        super.onStart()

        btnPolPlus.setOnClickListener {
            listener.onBtnPlusClick()
        }

        btnPolMinus.setOnClickListener {
            listener.onBtnMinusClick()
        }

        btnPolTimes.setOnClickListener {
            listener.onBtnTimesClick()
        }

        btnPolDivision.setOnClickListener {
            listener.onBtnDivisionClick()
        }

        btnPolRootsA.setOnClickListener {
            listener.onBtnRootsOfAClick()
        }

        btnPolRootsB.setOnClickListener {
            listener.onBtnRootsOfBClick()
        }

        btnSwitchBtnFragmentPolynomialFP.setOnClickListener{
            listener.onBtnSwitchFPClick()
        }
    }

    interface OnFragmentInteractionListener
    {
        fun onBtnDivisionClick()
        fun onBtnRootsOfAClick()
        fun onBtnRootsOfBClick()
        fun onBtnSwitchFPClick()
        fun onBtnPlusClick()
        fun onBtnMinusClick()
        fun onBtnTimesClick()
    }

    fun setListener( l : OnFragmentInteractionListener) : PolynomialButtonsGridFirstPageFragment
    {
        listener = l
        return this
    }

}
