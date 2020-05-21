package com.dev.smurf.highmathcalculator.Utils

import android.graphics.Bitmap

//if bitmap is 1x1 then it's empty
fun Bitmap.IsEmpty() = (this.width == 1 && this.height == 1)