package com.dev.smurf.highmathcalculator.ui.adapters


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dev.smurf.highmathcalculator.CanvasExtension.CanvasRenderSpecification
import com.dev.smurf.highmathcalculator.CanvasExtension.drawComplex
import com.dev.smurf.highmathcalculator.CanvasExtension.drawMatrixInBrackets
import com.dev.smurf.highmathcalculator.CanvasExtension.drawMatrixInLines
import com.dev.smurf.highmathcalculator.R
import com.dev.smurf.highmathcalculator.Utils.*
import com.example.smurf.mtarixcalc.MatrixGroup
import org.jetbrains.anko.imageBitmap
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat


class MatrixAdapterImageView(val context: Context, val firstMatrix: EditText, val secondMatrix: EditText) :
    RecyclerView.Adapter<MatrixAdapterImageView.MatrixViewHolder>()
{

    //списое элементов
    private var listOfMatrices: ArrayList<MatrixGroup> = ArrayList()

    //добавление нового элеменат
    fun addNewElem(group: MatrixGroup)
    {
        listOfMatrices.add(0, group)
        //Log.d("RV@" , "adding new elem")
        notifyDataSetChanged()
    }

    //очистка списка элементов
    fun clear()
    {
        listOfMatrices.clear()
        //Log.d("RV@" , "clear list")
        notifyDataSetChanged()
    }


    //удаление элемента на позици position
    fun removeElement(position: Int)
    {
        listOfMatrices.removeAt(position)
        //Log.d("RV@" , "remove from position:" + position.toString())
        notifyDataSetChanged()
    }

    //получить элемент из позиции position
    fun getData(position: Int): MatrixGroup
    {
        //Log.d("RV@" , "get data")
        return listOfMatrices[position]
    }

    //вставить элемент MatrixGroup в позицию position
    fun restoreItem(position: Int, MatrixGroup: MatrixGroup)
    {
        listOfMatrices.add(position, MatrixGroup)
        //Log.d("RV@" , "restore item")
        notifyItemInserted(position)
    }

    //установить новый список элементов
    fun setList(newArray: ArrayList<MatrixGroup>)
    {
        listOfMatrices = newArray
        //Log.d("RV@" , "set list")
        notifyDataSetChanged()
    }

    //получение всего списка элементов
    fun getList() = listOfMatrices

    //получить количество элементов
    override fun getItemCount(): Int
    {
        return listOfMatrices.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatrixAdapterImageView.MatrixViewHolder
    {
        return MatrixViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.matrix_expressions_imageview,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: MatrixViewHolder, position: Int)
    {
        try
        {
            holder.bind(listOfMatrices[position])
        }catch (e : Exception)
        {
            context.toast(listOfMatrices[position].leftMatrix.toString())
            var stackTarce = ""
            for(i in e.stackTrace)
            {
                stackTarce += i.toString()
            }
            firstMatrix.text = SpannableStringBuilder(stackTarce)
        }
        //листенер для контекстного меню на левую матрицу
        holder.leftMatrix.setOnCreateContextMenuListener( ContextMenuListener(context,firstMatrix,secondMatrix,holder.leftMatrixValue) )



        holder.resMatrix.setOnCreateContextMenuListener( ContextMenuListener(context,firstMatrix,secondMatrix,holder.resMatrixValue) )

        holder.rightMatrix.setOnCreateContextMenuListener( ContextMenuListener(context,firstMatrix,secondMatrix,holder.rightMatrixValue) )

    }


    class MatrixViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView)
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

            group.leftMatrix.let {

                val matrixSize = if (group.sign == "det") blackPainter.getMatrixInLinesSize(it)
                else blackPainter.getMatrixInBracketsSize(it)


                val mBitmap = Bitmap.createBitmap(
                    matrixSize.first.toInt(),
                    matrixSize.second.toInt(),
                    Bitmap.Config.ARGB_8888
                )


                val canvas = Canvas(mBitmap)


                if (group.sign == "det")
                {
                    canvas.drawMatrixInLines(it, CanvasRenderSpecification.x, CanvasRenderSpecification.y, blackPainter)
                }
                else
                {
                    canvas.drawMatrixInBrackets(
                        it,
                        CanvasRenderSpecification.x,
                        CanvasRenderSpecification.y,
                        blackPainter
                    )
                }

                leftMatrix.imageBitmap = mBitmap
            }

            if (!group.rightMatrix.isEmpty())
            {
                val matrixSize = blackPainter.getMatrixInBracketsSize(group.rightMatrix)
                val mBitmap = Bitmap.createBitmap(
                    matrixSize.first.toInt(),
                    matrixSize.second.toInt(),
                    Bitmap.Config.ARGB_8888
                )

                val canvas = Canvas(mBitmap)

                canvas.drawMatrixInBrackets(
                    group.rightMatrix,
                    CanvasRenderSpecification.x,
                    CanvasRenderSpecification.y,
                    blackPainter
                )

                rightMatrix.imageBitmap = mBitmap
            }


            val mBitmapResMatrix: Bitmap

            if (group.resMatrix.isNumber())
            {

                mBitmapResMatrix = Bitmap.createBitmap(
                    blackPainter.getComplexNumberWidth(group.resMatrix.matrices[0][0]).toInt(),
                    blackPainter.getComplexNumberHigh(group.resMatrix.matrices[0][0]).toInt(),
                    Bitmap.Config.ARGB_8888
                )


                val canvasRes = Canvas(mBitmapResMatrix)

                canvasRes.drawComplex(
                    group.resMatrix.matrices[0][0],
                    CanvasRenderSpecification.x,
                    CanvasRenderSpecification.y,
                    blackPainter
                )

            }
            else
            {
                val matrixSize = blackPainter.getMatrixInBracketsSize(group.resMatrix)

                mBitmapResMatrix = Bitmap.createBitmap(
                    matrixSize.first.toInt(),
                    matrixSize.second.toInt(),
                    Bitmap.Config.ARGB_8888
                )


                val canvasRes = Canvas(mBitmapResMatrix)

                canvasRes.drawMatrixInBrackets(
                    group.resMatrix,
                    CanvasRenderSpecification.x,
                    CanvasRenderSpecification.y,
                    blackPainter
                )
            }
            if (group.rightMatrix.isEmpty())
            {
                rightMatrix.imageBitmap = mBitmapResMatrix

            }
            else resMatrix.imageBitmap = mBitmapResMatrix

            val arr = FloatArray(group.sign.length)
            blackPainter.getTextWidths(group.sign, arr)


            val mBitmap = Bitmap.createBitmap(
                arr.sum().toInt(),
                CanvasRenderSpecification.getLetterHigh(blackPainter).toInt(),
                Bitmap.Config.ARGB_8888
            )

            val canvas = Canvas(mBitmap)

            canvas.drawText(
                group.sign,
                CanvasRenderSpecification.x,
                CanvasRenderSpecification.getLetterHigh(blackPainter),
                blackPainter
            )

            sign.imageBitmap = mBitmap


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


    }
}