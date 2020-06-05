package com.dev.smurf.highmathcalculator.ui.adapters.MatrixAdapters.ViewHolders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dev.smurf.highmathcalculator.ui.POJO.MatrixGroup

abstract class BindableViewHolder constructor(itemView: View,protected val width: Float) :
    RecyclerView.ViewHolder(itemView)
{
    abstract fun bind(group: MatrixGroup)
    abstract fun save(matrixGroup: MatrixGroup)
}