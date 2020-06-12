package com.dev.smurf.highmathcalculator.ui.adapters.MatrixAdapters.ViewHolders

import android.view.View
import android.view.animation.BounceInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListener
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.dev.smurf.highmathcalculator.R
import com.dev.smurf.highmathcalculator.ui.POJO.MatrixGroup
import java.text.SimpleDateFormat

class OnMatrixCalculationGoingViewHolder(itemView: View, width: Float) :
    MatrixBindableViewHolder(itemView, width)
{
    var timeMatrix: TextView = itemView.findViewById(R.id.timeCalculationOnGoingMatrix)
    override fun bind(group: MatrixGroup)
    {
        group.time.let {
            val fmt = SimpleDateFormat.getDateInstance()
            fmt.calendar = it
            timeMatrix.text = fmt.format(it.time)
        }
    }

    override fun save(matrixGroup: MatrixGroup)
    {
    }

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