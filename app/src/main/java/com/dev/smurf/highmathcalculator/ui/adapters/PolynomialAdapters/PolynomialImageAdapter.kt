package com.dev.smurf.highmathcalculator.ui.adapters.PolynomialAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.dev.smurf.highmathcalculator.Matrix.Matrix
import com.dev.smurf.highmathcalculator.Polynomials.PolynomialBase
import com.dev.smurf.highmathcalculator.R
import com.dev.smurf.highmathcalculator.ui.POJO.MatrixGroup
import com.dev.smurf.highmathcalculator.ui.adapters.ContextMenuListener
import com.dev.smurf.highmathcalculator.ui.adapters.PolynomialAdapters.ViewHolder.PolynomialBindableViewHolder
import com.dev.smurf.highmathcalculator.ui.adapters.PolynomialAdapters.ViewHolder.PolynomialImageViewHolder
import com.dev.smurf.highmathcalculator.ui.adapters.PolynomialAdapters.ViewHolder.PolynomialRoundedProgressBarViewHolder
import com.example.smurf.mtarixcalc.PolynomialGroup
import java.util.*
import kotlin.collections.ArrayList

class PolynomialImageAdapter(
    val context: Context,
    val polFirstPolynomial: EditText,
    val polSecPolynomial: EditText,
    val width: Float
) :
    RecyclerView.Adapter<PolynomialBindableViewHolder>()
{

    private var loading = false

    fun startLoading()
    {
        if (!loading)
        {
            loading = true
            listOfPolynomials.add(
                0,
                PolynomialGroup(
                    polLeftPolynomial = PolynomialBase.EmptyPolynomial,
                    polRightPolynomial = PolynomialBase.EmptyPolynomial,
                    polResPolynomial = PolynomialBase.EmptyPolynomial,
                    polOstPolynomial = PolynomialBase.EmptyPolynomial,
                    polSignPolynomial = "",
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
            listOfPolynomials.removeAt(0)
            notifyItemRemoved(0)
        }
    }

    //список элементов
    private var listOfPolynomials: MutableList<PolynomialGroup> = ArrayList()


    //получить количество элементов в списке
    override fun getItemCount(): Int
    {
        return listOfPolynomials.size
    }


    //добавить новый элемент
    fun addElement(PolynomialGroup: PolynomialGroup)
    {
        listOfPolynomials.add(0, PolynomialGroup)
        notifyItemInserted(0)
    }

    //очиститть список элементов
    fun clear()
    {
        listOfPolynomials = ArrayList()
        notifyDataSetChanged()
    }

    //удалить элемент на позиции
    fun removeElement(position: Int)
    {
        listOfPolynomials.removeAt(position)
        notifyDataSetChanged()
    }


    //получить элемент на похиции
    fun getData(pos: Int): PolynomialGroup = listOfPolynomials[pos]


    //востановить элемент item на позицию position
    fun restoreItem(item: PolynomialGroup, position: Int)
    {
        listOfPolynomials.add(position, item)
        notifyItemInserted(position)
    }


    //получить список элементов
    fun getList(): MutableList<PolynomialGroup> = listOfPolynomials

    //заменить список элементов
    fun setList(newArrayList: MutableList<PolynomialGroup>)
    {
        if (loading) newArrayList.add(0, listOfPolynomials[0])
        listOfPolynomials = newArrayList
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int
    {
        return when
        {
            (listOfPolynomials[position].polLeftPolynomial == PolynomialBase.EmptyPolynomial
                    && listOfPolynomials[position].polRightPolynomial == PolynomialBase.EmptyPolynomial
                    ) -> 0
            loading && position == 0 -> 0
            else -> 1
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PolynomialBindableViewHolder
    {
        return when (viewType)
        {
            0 -> PolynomialRoundedProgressBarViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.rounding_progress_bar_view_holder,
                    parent,
                    false
                ), width
            )
            else ->
                PolynomialImageViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.polynomial_expressions_imageview,
                        parent,
                        false
                    ), width
                )
        }

    }

    override fun onBindViewHolder(holder: PolynomialBindableViewHolder, position: Int)
    {

        if (holder is PolynomialImageViewHolder)
        {
            holder.bind(listOfPolynomials[position])
            //контекстное  меню для левого полинома
            holder.leftPolynomialImageView.setOnCreateContextMenuListener(
                ContextMenuListener(
                    context,
                    polFirstPolynomial,
                    polSecPolynomial,
                    holder.leftPolynomialValue
                )
            )


            //контестное меню для праавого элемента
            holder.rightPolynomialImageView.setOnCreateContextMenuListener(
                ContextMenuListener(
                    context,
                    polFirstPolynomial,
                    polSecPolynomial,
                    holder.rightPolynomialValue
                )
            )


            //контекстное меню для результирующего полинома
            holder.resultPolynomialImageView.setOnCreateContextMenuListener(
                ContextMenuListener(
                    context,
                    polFirstPolynomial,
                    polSecPolynomial,
                    holder.resultPolynomialValue
                )
            )

            //контекстное меню для остаточное полинома
            holder.remainderPolynomialImageView.setOnCreateContextMenuListener(
                ContextMenuListener(
                    context,
                    polFirstPolynomial,
                    polSecPolynomial,
                    holder.remainderPolynomialValue
                )
            )
        }

    }

}