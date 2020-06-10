package com.dev.smurf.highmathcalculator.ui.adapters.MatrixAdapters.ViewHolders

import android.view.View
import android.widget.TextView
import com.dev.smurf.highmathcalculator.R
import com.dev.smurf.highmathcalculator.ui.POJO.MatrixGroup
import java.text.SimpleDateFormat

class OnCalculationGoingViewHolder(itemView: View, width: Float) :
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
}