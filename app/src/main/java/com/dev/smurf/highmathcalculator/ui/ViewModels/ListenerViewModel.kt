package com.dev.smurf.highmathcalculator.ui.ViewModels

import androidx.lifecycle.ViewModel

class ListenerViewModel<T:Any>():ViewModel()
{
    lateinit var listener : T
}
