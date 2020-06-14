package com.dev.smurf.highmathcalculator.ui.fragments.ExtraInformationFragments

import android.os.Bundle
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import com.dev.smurf.highmathcalculator.R
import kotlinx.android.synthetic.main.full_matrix_dialog_layout.view.*
import kotlinx.android.synthetic.main.input_dialog_fragment_layout.view.*

class InputDialogFragment(
    private val output: MutableLiveData<SpannableString>,
    private val width: Float,
    private val height: Float
) : DialogFragment()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.NewDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        val layout = layoutInflater.inflate(R.layout.input_dialog_fragment_layout, null)
        layout.inputDialogRoot.minWidth = width.toInt()
        layout.inputDialogRoot.minHeight = height.toInt()
        return layout
    }


    fun btn1()
    {
    }

    fun btn2()
    {
    }

    fun btn3()
    {
    }

    fun btn4()
    {
    }

    fun btn5()
    {
    }

    fun btn6()
    {
    }

    fun btn7()
    {
    }

    fun btn8()
    {
    }

    fun btn9()
    {
    }

    fun btn0()
    {
    }
}