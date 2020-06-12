package com.dev.smurf.highmathcalculator.ui.adapters.PolynomialAdapters.ViewHolder

import android.view.View
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListener
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.dev.smurf.highmathcalculator.R
import com.dev.smurf.highmathcalculator.ui.POJO.MatrixGroup
import com.example.smurf.mtarixcalc.PolynomialGroup
import java.text.SimpleDateFormat

class OnPolynomialCalculationGoingViewHolder(itemView: View, maxWidth: Float) :
    PolynomialBindableViewHolder(itemView,maxWidth)
{
    var timeMatrix: TextView = itemView.findViewById(R.id.timeCalculationOnGoingMatrix)
    override fun bind(group: PolynomialGroup)
    {
        group.time.let {
            val fmt = SimpleDateFormat.getDateInstance()
            fmt.calendar = it
            timeMatrix.text = fmt.format(it.time)
        }
    }

    override fun save(polynomialGroup: PolynomialGroup)
    {}

    override fun preAnimateAddImpl(holder: RecyclerView.ViewHolder?)
    {
        itemView.translationY = -itemView.height * 2f
    }

    override fun animateAddImpl(
        holder: RecyclerView.ViewHolder?,
        listener: ViewPropertyAnimatorListener?
    )
    {
        ViewCompat.animate(itemView).apply {
            translationYBy(itemView.height * 2f)
            interpolator = FastOutLinearInInterpolator()
            duration = 500
            setListener(listener)
        }.start()
    }
}