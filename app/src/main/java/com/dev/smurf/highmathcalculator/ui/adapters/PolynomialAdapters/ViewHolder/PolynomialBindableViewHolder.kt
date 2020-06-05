package com.dev.smurf.highmathcalculator.ui.adapters.PolynomialAdapters.ViewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.smurf.mtarixcalc.PolynomialGroup

abstract class PolynomialBindableViewHolder(itemView : View, val width : Float): RecyclerView.ViewHolder(itemView)
{
    abstract fun bind(polynomialGroup: PolynomialGroup)
    abstract fun save(polynomialGroup: PolynomialGroup)
}