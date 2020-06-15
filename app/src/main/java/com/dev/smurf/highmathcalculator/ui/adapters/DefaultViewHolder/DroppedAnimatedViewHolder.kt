package com.dev.smurf.highmathcalculator.ui.adapters.DefaultViewHolder

import android.view.View
import android.view.animation.BounceInterpolator
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListener
import androidx.recyclerview.widget.RecyclerView
import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder

open class DroppedAnimatedViewHolder constructor(itemView: View) :
    RecyclerView.ViewHolder(itemView), AnimateViewHolder
{
    var doDropAnimations = false

    override fun animateRemoveImpl(
        holder: RecyclerView.ViewHolder?,
        listener: ViewPropertyAnimatorListener?
    )
    {
        ViewCompat.animate(itemView).apply {
            translationY(-itemView.height * 0.3f)
            alpha(0f)
            duration = 300
            setListener(listener)
        }.start()
    }

    override fun preAnimateRemoveImpl(holder: RecyclerView.ViewHolder?)
    {
    }

    override fun preAnimateAddImpl(holder: RecyclerView.ViewHolder?)
    {
        if (doDropAnimations)
        {
            itemView.translationY = -itemView.height * 2f
        }
        else
        {
            itemView.translationX = itemView.width * 1.5f
        }
    }

    override fun animateAddImpl(
        holder: RecyclerView.ViewHolder?,
        listener: ViewPropertyAnimatorListener?
    )
    {
        if (doDropAnimations)
        {
            ViewCompat.animate(itemView).apply {
                translationYBy(itemView.height * 2f)
                interpolator = BounceInterpolator()
                duration = 1000
                setListener(listener)
            }.start()
        }
        else
        {
            ViewCompat.animate(itemView).apply {
                translationXBy(-itemView.width * 1.5f)
                duration = 500
                setListener(listener)
            }.start()
        }
    }
}