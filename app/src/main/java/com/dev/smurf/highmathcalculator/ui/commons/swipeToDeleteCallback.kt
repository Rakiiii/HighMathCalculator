package com.example.smurf.mtarixcalc

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.dev.smurf.highmathcalculator.R


abstract class SwipeToDeleteCallback(context : Context) : ItemTouchHelper.Callback()
{
    var mContext : Context = context

    private var mClearPaint : Paint = Paint()

    private var mBackground : ColorDrawable = ColorDrawable()

    private var backgroundColor : Int = Color.parseColor("#b80f0a")

    private var deleteDrawable : Drawable? = ContextCompat.getDrawable(context , R.drawable.ic_delete)

    private var intrinsicWidth : Int = deleteDrawable!!.intrinsicWidth


    private var intrinsicHeight : Int = deleteDrawable!!.intrinsicHeight

    override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
        return makeMovementFlags( 0 , ItemTouchHelper.LEFT)
    }

    override fun onMove(
        recyclerView: RecyclerView?,
        viewHolder: RecyclerView.ViewHolder?,
        target: RecyclerView.ViewHolder?
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas?,
        recyclerView: RecyclerView?,
        viewHolder: RecyclerView.ViewHolder?,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean)
    {

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        var itemView = viewHolder!!.itemView
        var itemHeight = itemView.height

        var isCanceled : Boolean = (dX == 0.0f) && !isCurrentlyActive

        if(isCanceled)
        {
            clearCanvas( c!! , itemView.right + dX , itemView.top.toFloat(), itemView.right.toFloat() , itemView.bottom.toFloat() )
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        mBackground.color = backgroundColor
        mBackground.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
        mBackground.draw(c)

        val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
        val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
        val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth
        val deleteIconRight = itemView.right - deleteIconMargin
        val deleteIconBottom = deleteIconTop + intrinsicHeight


        deleteDrawable!!.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
        deleteDrawable!!.draw(c)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }


    private fun clearCanvas(c: Canvas, left: Float?, top: Float?, right: Float?, bottom: Float?) {
        c.drawRect(left!!, top!!, right!!, bottom!!, mClearPaint)

    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder?): Float {
        return 0.7F
    }

}