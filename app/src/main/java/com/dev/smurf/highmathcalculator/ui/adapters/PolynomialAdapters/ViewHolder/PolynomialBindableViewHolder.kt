package com.dev.smurf.highmathcalculator.ui.adapters.PolynomialAdapters.ViewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dev.smurf.highmathcalculator.ui.adapters.DefaultViewHolder.DroppedAnimatedViewHolder
import com.example.smurf.mtarixcalc.PolynomialGroup

abstract class PolynomialBindableViewHolder(itemView : View, val width : Float):
    DroppedAnimatedViewHolder(itemView)
{
    abstract fun bind(polynomialGroup: PolynomialGroup)
    abstract fun save(polynomialGroup: PolynomialGroup)
}