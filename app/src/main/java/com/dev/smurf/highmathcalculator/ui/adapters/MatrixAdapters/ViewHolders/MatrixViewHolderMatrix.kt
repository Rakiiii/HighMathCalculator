package com.dev.smurf.highmathcalculator.ui.adapters.MatrixAdapters.ViewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.dev.smurf.highmathcalculator.CanvasExtension.CanvasRenderSpecification
import com.dev.smurf.highmathcalculator.Matrix.Render.MatrixRenderInHolderStrategyConstracter
import com.dev.smurf.highmathcalculator.R
import com.dev.smurf.highmathcalculator.Utils.IsEmpty
import com.dev.smurf.highmathcalculator.ui.POJO.MatrixGroup
import org.jetbrains.anko.imageBitmap
import java.text.SimpleDateFormat

class MatrixViewHolderMatrix constructor(itemView: View, width: Float) :
    MatrixBindableViewHolder(itemView, width)
{
    //val width: Float = 40.0f

    var leftMatrix: ImageView = itemView.findViewById(R.id.leftMatrix)
        private set
    var rightMatrix: ImageView = itemView.findViewById(R.id.rightMatrix)
        private set
    var resMatrix: ImageView = itemView.findViewById(R.id.resMatrix)
        private set
    var sign: ImageView = itemView.findViewById(R.id.operationSignMatrix)
        private set
    var equalSign: ImageView = itemView.findViewById(R.id.equalsSignMatrix)
    var timeMatrix: TextView = itemView.findViewById(R.id.timeMatrix)

    lateinit var leftMatrixValue: String
        private set
    lateinit var rightMatrixValue: String
        private set
    lateinit var resMatrixValue: String
        private set

    override fun bind(group: MatrixGroup)
    {
        save(group)

        val blackPainter = CanvasRenderSpecification.createBlackPainter()

        val bitmapSet = MatrixRenderInHolderStrategyConstracter.getWorkStrategyGroup(
            group,
            width,
            blackPainter
        ).renderMatrix(group, blackPainter)


        leftMatrix.imageBitmap = bitmapSet.leftMatrixBitmap
        if (!bitmapSet.rightMatrixBitmap.IsEmpty()) rightMatrix.imageBitmap =
            bitmapSet.rightMatrixBitmap
        sign.imageBitmap = bitmapSet.signBitmap
        resMatrix.imageBitmap = bitmapSet.resultMatrixBitmap
        equalSign.imageBitmap = bitmapSet.equalsSignBitmap


        group.time.let {
            val fmt = SimpleDateFormat.getDateInstance()
            fmt.calendar = it
            timeMatrix.text = fmt.format(it.time)
        }
    }

    override fun save(matrixGroup: MatrixGroup)
    {
        leftMatrixValue = matrixGroup.leftMatrix.toString()
        rightMatrixValue =
            if (matrixGroup.rightMatrix.isEmpty()) matrixGroup.resMatrix.toString() else matrixGroup.rightMatrix.toString()
        resMatrixValue = matrixGroup.resMatrix.toString()
    }
}