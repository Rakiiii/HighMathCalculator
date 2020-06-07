package com.dev.smurf.highmathcalculator.ui.CustomRecylerViewLayoutManagers

import android.content.Context
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager

class LayoutManagerWithOffableScroll(context: Context?) : LinearLayoutManager(context)
{
    var isVerticalScrollEnable = false

    override fun canScrollVertically(): Boolean
    {
        return isVerticalScrollEnable && super.canScrollVertically()
    }
}