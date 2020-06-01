package com.dev.smurf.highmathcalculator.ui.fragments.matrixFragment

import android.content.Context
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
import kotlinx.android.synthetic.main.fragment_matrix_button_grid_first_page.*

class MatrixButtonGridFragmentFirstPage() : Fragment(),Observable
{
    private lateinit var listener : onFragmentInteractionListener
    private val matrixListenerViewModel : MatrixListenerViewModel<MatrixFragment> by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_matrix_button_grid_first_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        matrixListenerViewModel.listener.observe(viewLifecycleOwner, Observer { f -> listener = f  })
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

    override fun onAttach(context: Context)
    {
        super.onAttach(context)
    }

    override fun setListener(l : Fragment)
    {
        if(l is onFragmentInteractionListener)
        {
            listener = l
            Log.d("lifecycle@","listener setted:"+(l is MatrixFragment).toString())
        }
    }

    class WrongListenerException : Exception()
    {
        override val message: String?
            get() = super.message + "Wrong Listener for MatrixButtonGridFragment"
    }
}
