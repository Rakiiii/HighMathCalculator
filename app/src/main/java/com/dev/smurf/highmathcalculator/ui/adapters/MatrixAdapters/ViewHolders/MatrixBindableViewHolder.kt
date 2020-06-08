package com.dev.smurf.highmathcalculator.ui.adapters.MatrixAdapters.ViewHolders

import android.os.Parcel
import android.os.Parcelable
import android.view.View
import android.view.animation.BounceInterpolator
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListener
import androidx.recyclerview.widget.RecyclerView
import com.dev.smurf.highmathcalculator.ui.POJO.MatrixGroup
import com.dev.smurf.highmathcalculator.ui.adapters.DefaultViewHolder.DroppedAnimatedViewHolder
import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder

abstract class MatrixBindableViewHolder constructor(itemView: View, protected val width: Float) :
    DroppedAnimatedViewHolder(itemView)
{

    abstract fun bind(group: MatrixGroup)
    abstract fun save(matrixGroup: MatrixGroup)

}