package com.dev.smurf.highmathcalculator.ui.fragments.matrixFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer

import com.dev.smurf.highmathcalculator.R
import com.dev.smurf.highmathcalculator.ui.ViewModels.MatrixListenerViewModel
import com.dev.smurf.highmathcalculator.ui.fragments.fragmentInterfaces.Observable
import kotlinx.android.synthetic.main.fragment_matrix_button_grid_first_page.btnSwitchBtnFragment
import kotlinx.android.synthetic.main.fragment_matrix_button_grid_second_page.*

class MatrixButtonGridFragmentSecondPage(): Fragment(),Observable
{

    private lateinit var listener : onFragmentInteractionListener
    private val matrixListenerViewModel : MatrixListenerViewModel<MatrixFragment> by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_matrix_button_grid_second_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        matrixListenerViewModel.listener.observe(viewLifecycleOwner, Observer { f -> listener = f  })
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


    override fun setListener(l : Fragment)
    {
        if(l is onFragmentInteractionListener)
        {
            listener = l
            Log.d("lifecycle@","listener setted")
        }
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
