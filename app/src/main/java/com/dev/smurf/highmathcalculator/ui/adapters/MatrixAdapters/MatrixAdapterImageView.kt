package com.dev.smurf.highmathcalculator.ui.adapters.MatrixAdapters


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.dev.smurf.highmathcalculator.Matrix.Matrix
import com.dev.smurf.highmathcalculator.R
import com.dev.smurf.highmathcalculator.ui.POJO.MatrixGroup
import com.dev.smurf.highmathcalculator.ui.adapters.ContextMenuListener
import com.dev.smurf.highmathcalculator.ui.adapters.MatrixAdapters.ViewHolders.MatrixBindableViewHolder
import com.dev.smurf.highmathcalculator.ui.adapters.MatrixAdapters.ViewHolders.MatrixViewHolderMatrix
import com.dev.smurf.highmathcalculator.ui.adapters.MatrixAdapters.ViewHolders.RoundingProgressBarViewHolderMatrix


class MatrixAdapterImageView(
    val context: Context,
    val firstMatrix: EditText,
    val secondMatrix: EditText,
    val width: Float
) :
    RecyclerView.Adapter<MatrixBindableViewHolder>()
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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatrixBindableViewHolder
    {
        return when (viewType)
        {
            0 -> RoundingProgressBarViewHolderMatrix(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.rounding_progress_bar_view_holder,
                    parent,
                    false
                ), width
            )
            else -> MatrixViewHolderMatrix(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.matrix_expressions_imageview,
                    parent,
                    false
                ), width
            )
        }
    }


    override fun onBindViewHolder(holderMatrix: MatrixBindableViewHolder, position: Int)
    {

        if (holderMatrix is MatrixViewHolderMatrix)
        {
            holderMatrix.bind(listOfMatrices[position])

            //листенер для контекстного меню на левую матрицу
            holderMatrix.leftMatrix.setOnCreateContextMenuListener(
                ContextMenuListener(
                    context,
                    firstMatrix,
                    secondMatrix,
                    holderMatrix.leftMatrixValue
                )
            )

            holderMatrix.resMatrix.setOnCreateContextMenuListener(
                ContextMenuListener(
                    context,
                    firstMatrix,
                    secondMatrix,
                    holderMatrix.resMatrixValue
                )
            )

            holderMatrix.rightMatrix.setOnCreateContextMenuListener(
                ContextMenuListener(
                    context,
                    firstMatrix,
                    secondMatrix,
                    holderMatrix.rightMatrixValue
                )
            )

        }
    }
}
