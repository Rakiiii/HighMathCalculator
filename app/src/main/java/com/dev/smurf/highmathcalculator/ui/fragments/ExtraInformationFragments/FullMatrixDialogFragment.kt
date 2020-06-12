package com.dev.smurf.highmathcalculator.ui.fragments.ExtraInformationFragments

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.dev.smurf.highmathcalculator.CalculatorApplication
import com.dev.smurf.highmathcalculator.R
import com.dev.smurf.highmathcalculator.ui.adapters.ContextMenuListener
import kotlinx.android.synthetic.main.error_dialog_layout.view.*
import kotlinx.android.synthetic.main.full_matrix_dialog_layout.view.*
import org.jetbrains.anko.imageBitmap

class FullMatrixDialogFragment(
    private val matrix : String,
    private val matrixBitmap: Bitmap,
    private val width: Float,
    private val height: Float,
    private val listener : onClickListener,
    private val firstMatrix : EditText,
    private val secondMatrix : EditText
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
        val layout = layoutInflater.inflate(R.layout.full_matrix_dialog_layout, null)
        layout.fullMatrixDialogRoot.minimumWidth = width.toInt()
        layout.fullMatrixDialogRoot.minimumHeight = height.toInt()
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        view.fullMatrixIV.imageBitmap = matrixBitmap
        view.btnMatrixDialogOk.setOnClickListener {
            listener.btnMatrixDialogOkClicked()
        }

        view.fullMatrixIV.setOnCreateContextMenuListener(
            ContextMenuListener(CalculatorApplication.context,firstMatrix,secondMatrix,matrix)
        )

        val animator = view.animate()
        animator.apply {
            scaleY(1f)
            duration = 2000
            interpolator = BounceInterpolator()
        }
    }

    interface onClickListener
    {
        fun btnMatrixDialogOkClicked()
    }
}