package com.dev.smurf.highmathcalculator.ui.adapters.MatrixAdapters


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dev.smurf.highmathcalculator.CanvasExtension.CanvasRenderSpecification
import com.dev.smurf.highmathcalculator.CanvasExtension.drawFractions
import com.dev.smurf.highmathcalculator.Matrix.Render.MatrixRender
import com.dev.smurf.highmathcalculator.Numbers.Fraction
import com.dev.smurf.highmathcalculator.PaintExtension.getFractionSize
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
    val width: Float
) :
    RecyclerView.Adapter<MatrixAdapterImageView.MatrixViewHolder>()
{

    //списое элементов
    private var listOfMatrices: ArrayList<MatrixGroup> = ArrayList()

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
    fun setList(newArray: ArrayList<MatrixGroup>)
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
            ), width - 140.0f
        )
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


    class MatrixViewHolder constructor(itemView: View, val width: Float) :
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

            val bitmapSet = MatrixRender.renderWithStrategy(group,width,blackPainter)
            // = MatrixRender.renderMatrixSet(group, width, blackPainter)
            leftMatrix.imageBitmap = bitmapSet.leftMatrixBitmap
            if (!bitmapSet.rightMatrixBitmap.IsEmpty()) rightMatrix.imageBitmap =
                bitmapSet.rightMatrixBitmap
            sign.imageBitmap = bitmapSet.signBitmap
            resMatrix.imageBitmap = bitmapSet.resultMatrixBitmap
            equalSign.imageBitmap = bitmapSet.equalsSignBitmap

            renderTest()

            group.time.let {
                val fmt = SimpleDateFormat(" HH:mm:ss dd MMM yyyy")
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


        fun renderTest()
        {
            val mPaint = CanvasRenderSpecification.createBlackPainter()
            val f1 = Fraction(1565, 1)
            val f1Size = mPaint.getFractionSize(f1)
            Log.d("size@",f1Size.first.toString()+ " "+f1Size.second.toString())
            val f1Bitmap = Bitmap.createBitmap(
                f1Size.first.toInt(),
                f1Size.second.toInt(),
                Bitmap.Config.ARGB_8888
            )
            Canvas(f1Bitmap).drawFractions(f1,0.0f,0.0f,mPaint)
            leftMatrix.imageBitmap = f1Bitmap


            val f2 = Fraction(-2, 536)
            val f2Size = mPaint.getFractionSize(f2)
            Log.d("sizef2@",f2Size.first.toString()+ " "+f2Size.second.toString())
            val f2t = Fraction(2, 536)
            val f2tSize = mPaint.getFractionSize(f2t)
            Log.d("sizef2@",f2tSize.first.toString()+ " "+f2tSize.second.toString())
            val f2Bitmap = Bitmap.createBitmap(
                f2Size.first.toInt(),
                f2Size.second.toInt(),
                Bitmap.Config.ARGB_8888
            )
            Canvas(f2Bitmap).drawFractions(f2,0.0f,0.0f,mPaint)
            //f2Bitmap.eraseColor(Color.BLACK)
            rightMatrix.imageBitmap =f2Bitmap

            val f3 = Fraction(1654, 536)
            val f3Size = mPaint.getFractionSize(f3)
            Log.d("size@",f3Size.first.toString()+ " "+f3Size.second.toString())
            val f3Bitmap = Bitmap.createBitmap(
                f3Size.first.toInt(),
                f3Size.second.toInt(),
                Bitmap.Config.ARGB_8888
            )
            Canvas(f3Bitmap).drawFractions(f3,0.0f,0.0f,mPaint)
            sign.imageBitmap =f3Bitmap

            val f4 = Fraction(1654, 897536)
            val f4Size = mPaint.getFractionSize(f4)
            Log.d("size@",f4Size.first.toString()+ " "+f4Size.second.toString())
            val f4Bitmap = Bitmap.createBitmap(
                f4Size.first.toInt(),
                f4Size.second.toInt(),
                Bitmap.Config.ARGB_8888
            )
            Canvas(f4Bitmap).drawFractions(f4,0.0f,0.0f,mPaint)
            resMatrix.imageBitmap = f4Bitmap
        }
    }


}