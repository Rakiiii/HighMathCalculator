package com.dev.smurf.highmathcalculator.ui.ViewModels

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dev.smurf.highmathcalculator.ui.fragments.matrixFragment.MatrixFragment
import com.dev.smurf.highmathcalculator.ui.fragments.polynomialFragment.PolynomialFragment

class PolynomialListenerViewModel<T:Fragment>():ViewModel()
{
    val listener = MutableLiveData<T>()

    fun updateListener(f : T)
    {
        listener.value = f
    }
}

class MatrixListenerViewModel<T:Fragment>():ViewModel()
{
    val listener = MutableLiveData<T>()

    fun updateListener(f : T)
    {
        listener.value = f
    }
}
