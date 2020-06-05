package com.dev.smurf.highmathcalculator.ui.adapters.PolynomialAdapters.ViewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.dev.smurf.highmathcalculator.CanvasExtension.CanvasRenderSpecification
import com.dev.smurf.highmathcalculator.Polynomials.Render.PolynomialRenderInHolder
import com.dev.smurf.highmathcalculator.R
import com.example.smurf.mtarixcalc.PolynomialGroup
import org.jetbrains.anko.imageBitmap
import java.text.SimpleDateFormat

class PolynomialImageViewHolder(itemView: View,maxWidth: Float) :
PolynomialBindableViewHolder(itemView,maxWidth)
{

    lateinit var leftPolynomialValue: String
        private set

    lateinit var rightPolynomialValue: String
        private set

    lateinit var resultPolynomialValue: String
        private set

    lateinit var remainderPolynomialValue: String
        private set


    var leftPolynomialImageView: ImageView = itemView.findViewById(R.id.leftPolynomialIM)
        private set

    var rightPolynomialImageView: ImageView = itemView.findViewById(R.id.rightPolynomialIM)
        private set

    var resultPolynomialImageView: ImageView = itemView.findViewById(R.id.resultPolynomialIM)
        private set

    var remainderPolynomialImageView: ImageView =
        itemView.findViewById(R.id.remainderPolynomialIM)
        private set

    var signumImageView: ImageView = itemView.findViewById(R.id.operationSignPolynomialIM)

    var timePolynomial: TextView = itemView.findViewById(R.id.timePolynomialIM)
        private set


    override fun bind(polynomialGroup: PolynomialGroup)
    {
        save(polynomialGroup)

        val blackPainter = CanvasRenderSpecification.createBlackPainter()

        val bitmapSet = PolynomialRenderInHolder.renderWithStrategy(
            polynomialGroup,
            width,
            width * 2,
            blackPainter
        )

        leftPolynomialImageView.imageBitmap = bitmapSet.leftPolynomialBitmap
        rightPolynomialImageView.imageBitmap = bitmapSet.rightPolynomialBitmap
        resultPolynomialImageView.imageBitmap = bitmapSet.resultPolynomialBitmap
        remainderPolynomialImageView.imageBitmap = bitmapSet.remainderPolynomialBitmap
        signumImageView.imageBitmap = bitmapSet.signBitmap

        polynomialGroup.time.let {
            val fmt = SimpleDateFormat.getDateInstance()
            fmt.calendar = it
            timePolynomial.text = fmt.format(it.time)
        }


    }

    override fun save(polynomialGroup: PolynomialGroup)
    {
        leftPolynomialValue = polynomialGroup.polLeftPolynomial.toString()

        rightPolynomialValue =
            polynomialGroup.polRightPolynomial.toString()

        resultPolynomialValue =
            polynomialGroup.polResPolynomial.toString()

        remainderPolynomialValue =
            polynomialGroup.polOstPolynomial.toString()
    }

}