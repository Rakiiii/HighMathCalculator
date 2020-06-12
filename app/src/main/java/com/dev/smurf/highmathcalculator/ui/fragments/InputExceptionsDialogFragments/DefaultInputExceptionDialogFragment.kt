package com.dev.smurf.highmathcalculator.ui.fragments.InputExceptionsDialogFragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.dev.smurf.highmathcalculator.R
import kotlinx.android.synthetic.main.error_dialog_layout.view.*
import org.jetbrains.anko.imageBitmap
import org.jetbrains.anko.support.v4.find

class DefaultInputExceptionDialogFragment(
    private val errorBitmap: Bitmap,
    private val listener: onFragmentInteractionListener,
    private val width : Float,
    private val height : Float,
    private val errorText : String
) : DialogFragment()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL,R.style.NewDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        val layout = layoutInflater.inflate(R.layout.error_dialog_layout,null)
        layout.errorDialogRoot.minWidth = width.toInt()
        layout.errorDialogRoot.minHeight = height.toInt()

        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        view.ivError.imageBitmap = errorBitmap
        view.btnOk.setOnClickListener {
            listener.btnOkPressed()
        }
        view.tvErrorText.text = errorText
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setTitle(R.string.errorDialogTitle)
        return dialog
    }


    interface onFragmentInteractionListener
    {
        fun btnOkPressed()
    }
}