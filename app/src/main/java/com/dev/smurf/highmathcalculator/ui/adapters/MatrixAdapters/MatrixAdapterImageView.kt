package com.dev.smurf.highmathcalculator.ui.adapters.MatrixAdapters


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dev.smurf.highmathcalculator.CanvasExtension.CanvasRenderSpecification
import com.dev.smurf.highmathcalculator.Matrix.Render.MatrixRenderInHolderStrategyConstracter
import com.dev.smurf.highmathcalculator.R
import com.dev.smurf.highmathcalculator.Utils.*
import com.dev.smurf.highmathcalculator.ui.POJO.MatrixGroup
import com.dev.smurf.highmathcalculator.ui.adapters.ContextMenuListener
import org.jetbrains.anko.imageBitmap
import java.text.SimpleDateFormat


class MatrixAdapterImageView(
    val context: Context,
    val firstMatrix: EditText,
    val secondMatrix: EditText,
    val width : Float
) :
    RecyclerView.Adapter<MatrixAdapterImageView.MatrixViewHolder>()
{

    //списое элементов
    private var listOfMatrices: MutableList<MatrixGroup> = ArrayList()

    //добавление нового элеменат
    fun addNewElem(group: MatrixGroup)
    {
        listOfMatrices.add(0, group)
        notifyItemInserted(0)

    }

    //очистка списка элементов
    fun clear()
    {
        listOfMatrices.clear()
        notifyDataSetChanged()
    }


    //удаление элемента на позици position
    fun removeElement(position: Int)
    {
        listOfMatrices.removeAt(position)
        notifyDataSetChanged()
    }

    //получить элемент из позиции position
    fun getData(position: Int): MatrixGroup
    {
        return listOfMatrices[position]
    }

    //вставить элемент MatrixGroup в позицию position
    fun restoreItem(position: Int, MatrixGroup: MatrixGroup)
    {
        listOfMatrices.add(position, MatrixGroup)
        notifyItemInserted(position)
    }

    //установить новый список элементов
    fun setList(newArray: MutableList<MatrixGroup>)
    {
        listOfMatrices = newArray
        notifyDataSetChanged()
    }

    //получение всего списка элементов
    fun getList() = listOfMatrices

    //получить количество элементов
    override fun getItemCount(): Int
    {
        return listOfMatrices.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatrixViewHolder
    {
        return MatrixViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.matrix_expressions_imageview,
                parent,
                false
            ),width)
    }


    override fun onBindViewHolder(holder: MatrixViewHolder, position: Int)
    {

        holder.bind(listOfMatrices[position])

        //листенер для контекстного меню на левую матрицу
        holder.leftMatrix.setOnCreateContextMenuListener(
            ContextMenuListener(
                context,
                firstMatrix,
                secondMatrix,
                holder.leftMatrixValue
            )
        )

        holder.resMatrix.setOnCreateContextMenuListener(
            ContextMenuListener(
                context,
                firstMatrix,
                secondMatrix,
                holder.resMatrixValue
            )
        )

        holder.rightMatrix.setOnCreateContextMenuListener(
            ContextMenuListener(
                context,
                firstMatrix,
                secondMatrix,
                holder.rightMatrixValue
            )
        )

    }


    class MatrixViewHolder constructor(itemView: View,val width:Float) :
        RecyclerView.ViewHolder(itemView)
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

        fun bind(group: MatrixGroup)
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

        private fun save(matrixGroup: MatrixGroup)
        {
            leftMatrixValue = matrixGroup.leftMatrix.toString()
            rightMatrixValue =
                if (matrixGroup.rightMatrix.isEmpty()) matrixGroup.resMatrix.toString() else matrixGroup.rightMatrix.toString()
            resMatrixValue = matrixGroup.resMatrix.toString()
        }
    }
}
