package com.dev.smurf.highmathcalculator.ui.adapters.MatrixAdapters


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.dev.smurf.highmathcalculator.Matrix.Matrix
import com.dev.smurf.highmathcalculator.R
import com.dev.smurf.highmathcalculator.ui.POJO.MatrixGroup
import com.dev.smurf.highmathcalculator.ui.adapters.ContextMenuListener
import com.dev.smurf.highmathcalculator.ui.adapters.MatrixAdapters.ViewHolders.MatrixBindableViewHolder
import com.dev.smurf.highmathcalculator.ui.adapters.MatrixAdapters.ViewHolders.MatrixViewHolderMatrix
import com.dev.smurf.highmathcalculator.ui.adapters.MatrixAdapters.ViewHolders.OnMatrixCalculationGoingViewHolder
import com.dev.smurf.highmathcalculator.ui.adapters.MatrixAdapters.ViewHolders.RoundingProgressBarViewHolderMatrix


class MatrixAdapterImageView(
    val context: Context,
    val firstMatrix: EditText,
    val secondMatrix: EditText,
    val width: Float,
    val onMatrixClickedListener : MutableLiveData<String>
) :
    RecyclerView.Adapter<MatrixBindableViewHolder>()
{

    private var loading = false
    private var dropAnimations = false

    fun startLoading()
    {
        if (!loading)
        {
            dropAnimations = false
            loading = true
            listOfMatrices.add(
                0,
                MatrixGroup(
                    leftMatrix = Matrix.EmptyMatrix,
                    resMatrix = Matrix.EmptyMatrix,
                    rightMatrix = Matrix.EmptyMatrix,
                    sign = MatrixGroup.LOADING,
                    time = java.util.GregorianCalendar()
                )
            )
            notifyItemInserted(0)
        }
    }

    fun stopLoading()
    {
        if (loading)
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
        dropAnimations = true
        notifyItemInserted(0)
    }

    fun stopCalculation(group: MatrixGroup)
    {
        for (i in listOfMatrices.indices)
        {
            if (listOfMatrices[i].time.timeInMillis == group.time.timeInMillis)
            {
                Log.d("solved@","stoped time ${listOfMatrices[i].time.timeInMillis}")
                listOfMatrices[i] = group
                notifyItemChanged(i)
                break
            }
        }
    }

    fun removeCalculation(group: MatrixGroup)
    {
        for (i in listOfMatrices.indices)
        {
            if (listOfMatrices[i].time.timeInMillis == group.time.timeInMillis)
            {
                listOfMatrices.removeAt(i)
                notifyItemRemoved(i)
                break
            }
        }
    }

    fun removeAllCalculation()
    {
        listOfMatrices = listOfMatrices.filterNot { s -> s.sign == MatrixGroup.CALCULATION }.toMutableList()
    }

    //очистка списка элементов
    fun clear()
    {
        listOfMatrices = ArrayList()
        notifyDataSetChanged()
    }


    //удаление элемента на позици position
    fun removeElement(position: Int)
    {
        listOfMatrices.removeAt(position)
        notifyItemRemoved(position)
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
        notifyItemRangeChanged(0, listOfMatrices.size - 1)
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
        return when
        {
            (listOfMatrices[position].leftMatrix == Matrix.EmptyMatrix && listOfMatrices[position].sign == MatrixGroup.LOADING) -> 0
            (listOfMatrices[position].leftMatrix == Matrix.EmptyMatrix && listOfMatrices[position].sign == MatrixGroup.CALCULATION) -> 2
            else -> 1
        }
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
            2 -> OnMatrixCalculationGoingViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.calulation_on_going_viewholder,
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
        holderMatrix.doDropAnimations = dropAnimations
        if(holderMatrix is OnMatrixCalculationGoingViewHolder)
        {
            holderMatrix.bind(listOfMatrices[position])
        }
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

            holderMatrix.leftMatrix.setOnClickListener {
                if(holderMatrix.leftMatrixValue.isNotEmpty())onMatrixClickedListener.value = holderMatrix.leftMatrixValue
            }

            holderMatrix.rightMatrix.setOnClickListener {
                if(holderMatrix.rightMatrixValue.isNotEmpty())onMatrixClickedListener.value = holderMatrix.rightMatrixValue
            }

            holderMatrix.resMatrix.setOnClickListener {
                if(holderMatrix.resMatrixValue.isNotEmpty())onMatrixClickedListener.value = holderMatrix.resMatrixValue
            }

        }
    }
}
