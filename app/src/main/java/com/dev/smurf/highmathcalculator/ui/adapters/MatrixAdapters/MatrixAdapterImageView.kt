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
import com.dev.smurf.highmathcalculator.Matrix.Matrix
import com.dev.smurf.highmathcalculator.Matrix.Render.MatrixRenderInHolderStrategyConstracter
import com.dev.smurf.highmathcalculator.R
import com.dev.smurf.highmathcalculator.Utils.*
import com.dev.smurf.highmathcalculator.ui.POJO.MatrixGroup
import com.dev.smurf.highmathcalculator.ui.adapters.ContextMenuListener
import com.dev.smurf.highmathcalculator.ui.adapters.MatrixAdapters.ViewHolders.BindableViewHolder
import com.dev.smurf.highmathcalculator.ui.adapters.MatrixAdapters.ViewHolders.MatrixViewHolder
import com.dev.smurf.highmathcalculator.ui.adapters.MatrixAdapters.ViewHolders.RoundingProgressBarViewHolder
import org.jetbrains.anko.imageBitmap
import java.text.SimpleDateFormat


class MatrixAdapterImageView(
    val context: Context,
    val firstMatrix: EditText,
    val secondMatrix: EditText,
    val width: Float
) :
    RecyclerView.Adapter<BindableViewHolder>()
{

    private var loading = false

    fun startLoading()
    {
        if(!loading)
        {
            loading = true
            listOfMatrices.add(
                0,
                MatrixGroup(
                    leftMatrix = Matrix.EmptyMatrix,
                    resMatrix = Matrix.EmptyMatrix,
                    rightMatrix = Matrix.EmptyMatrix,
                    sign = "",
                    time = java.util.GregorianCalendar()
                )
            )
            notifyItemInserted(0)
        }
    }

    fun stopLoading()
    {
        if(loading)
        {
            loading = false
            listOfMatrices.removeAt(0)
            notifyItemRemoved(0)
        }
    }

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
        if (loading) newArray.add(0, listOfMatrices[0])
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

    override fun getItemViewType(position: Int): Int
    {
        return if (listOfMatrices[position].leftMatrix == Matrix.EmptyMatrix) 0 else 1
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindableViewHolder
    {
        return when (viewType)
        {
            0 -> RoundingProgressBarViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.rounding_progress_bar_view_holder,
                    parent,
                    false
                ), width
            )
            else -> MatrixViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.matrix_expressions_imageview,
                    parent,
                    false
                ), width
            )
        }
    }


    override fun onBindViewHolder(holder: BindableViewHolder, position: Int)
    {

        if (holder is MatrixViewHolder)
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
    }
}
