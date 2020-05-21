package com.dev.smurf.highmathcalculator.ui.fragments.matrixFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.dev.smurf.highmathcalculator.R
import kotlinx.android.synthetic.main.fragment_matrix_button_grid_first_page.btnSwitchBtnFragment
import kotlinx.android.synthetic.main.fragment_matrix_button_grid_second_page.*

class MatrixButtonGridFragmentSecondPage : Fragment()
{

    private lateinit var listener : onFragmentInteractionListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_matrix_button_grid_second_page, container, false)
    }

    override fun onStart()
    {
        super.onStart()

        btnSwitchBtnFragment.setOnClickListener {
            listener.btnSwitchSPClicked()
        }

        btnRank.setOnClickListener {
            listener.btnRankClicked()
        }

        btnPositive.setOnClickListener {
            listener.btnPositiveClicked()
        }

        btnNegative.setOnClickListener {
            listener.btnNegativeClicked()
        }

        btnSolveSystem.setOnClickListener {
            listener.btnSolveSystemClicked()
        }

        btnEigenvalue.setOnClickListener {
            listener.btnEighnvalueClicked()
        }

        btnEigenvector.setOnClickListener {
            listener.btnEighnvectorClicked()
        }
    }

    fun setListener(l : onFragmentInteractionListener) : MatrixButtonGridFragmentSecondPage
    {
        listener = l
        return this
    }


    interface onFragmentInteractionListener
    {
        fun btnRankClicked()
        fun btnPositiveClicked()
        fun btnNegativeClicked()
        fun btnSolveSystemClicked()
        fun btnEighnvectorClicked()
        fun btnEighnvalueClicked()
        fun btnSwitchSPClicked()
    }
}
