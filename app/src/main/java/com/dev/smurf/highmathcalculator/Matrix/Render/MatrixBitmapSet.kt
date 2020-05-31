package com.dev.smurf.highmathcalculator.Matrix.Render

import android.graphics.Bitmap

//represent set of bitmap
data class MatrixBitmapSet(
    var leftMatrixBitmap: Bitmap,
    var rightMatrixBitmap: Bitmap,
    var signBitmap: Bitmap,
    var equalsSignBitmap: Bitmap,
    var resultMatrixBitmap: Bitmap
)
{
    companion object
    {
        fun getEmptyBitmapSet() =
            MatrixBitmapSet(
                getEmptyBitmap(),
                getEmptyBitmap(),
                getEmptyBitmap(),
                getEmptyBitmap(),
                getEmptyBitmap()
            )

        fun getEmptyBitmap() = Bitmap.createBitmap(
            1, 1,
            MatrixRenderInHolderStrategyConstracter.bitmapConfig
        )

    }
}