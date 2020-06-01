package com.dev.smurf.highmathcalculator.ui.adapters.PolynomialAdapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dev.smurf.highmathcalculator.CanvasExtension.*
import com.dev.smurf.highmathcalculator.R
import com.dev.smurf.highmathcalculator.PaintExtension.getPolynomialHigh
import com.dev.smurf.highmathcalculator.PaintExtension.getPolynomialSize
import com.dev.smurf.highmathcalculator.PaintExtension.getPolynomialWidth
import com.dev.smurf.highmathcalculator.Polynomials.PolynomialBase
import com.dev.smurf.highmathcalculator.Polynomials.Render.PolynomialRenderInHolder
import com.dev.smurf.highmathcalculator.ui.adapters.ContextMenuListener
import com.example.smurf.mtarixcalc.PolynomialGroup
import org.jetbrains.anko.imageBitmap
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat

class PolynomialAdapterImageView(
    val context: Context,
    val polFirstPolynomial: EditText,
    val polSecPolynomial: EditText,
    val maxWidth: Float
) :
    RecyclerView.Adapter<PolynomialAdapterImageView.PolynomialViewHolder>()
{

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
        listOfPolynomials.clear()
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
        listOfPolynomials = newArrayList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PolynomialViewHolder
    {
        return PolynomialViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.polynomial_expressions_imageview,
                parent,
                false
            ), maxWidth
        )


    }

    override fun onBindViewHolder(holder: PolynomialViewHolder, position: Int)
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

    class PolynomialViewHolder(itemView: View, val maxWidth: Float) :
        RecyclerView.ViewHolder(itemView)
    {

        lateinit var leftPolynomialValue: String
            private set

        lateinit var rightPolynomialValue: String
            private set

        lateinit var resultPolynomialValue: String
            private set

        lateinit var remainderPolynomialValue: String
            private set


        var leftPolynomialImageView: ImageView = itemView.findViewById(R.id.leftPolynomialIM)
            private set

        var rightPolynomialImageView: ImageView = itemView.findViewById(R.id.rightPolynomialIM)
            private set

        var resultPolynomialImageView: ImageView = itemView.findViewById(R.id.resultPolynomialIM)
            private set

        var remainderPolynomialImageView: ImageView =
            itemView.findViewById(R.id.remainderPolynomialIM)
            private set

        var signumImageView: ImageView = itemView.findViewById(R.id.operationSignPolynomialIM)

        var timePolynomial: TextView = itemView.findViewById(R.id.timePolynomialIM)
            private set


        fun bind(polynomialGroup: PolynomialGroup)
        {
            save(polynomialGroup)

            val blackPainter = CanvasRenderSpecification.createBlackPainter()

            val bitmapSet = PolynomialRenderInHolder.renderWithStrategy(
                polynomialGroup,
                maxWidth,
                maxWidth * 2,
                blackPainter
            )

            leftPolynomialImageView.imageBitmap = bitmapSet.leftPolynomialBitmap
            rightPolynomialImageView.imageBitmap = bitmapSet.rightPolynomialBitmap
            resultPolynomialImageView.imageBitmap = bitmapSet.resultPolynomialBitmap
            remainderPolynomialImageView.imageBitmap = bitmapSet.remainderPolynomialBitmap
            signumImageView.imageBitmap = bitmapSet.signBitmap

            polynomialGroup.time.let {
                val fmt = SimpleDateFormat.getDateInstance()
                fmt.calendar = it
                timePolynomial.text = fmt.format(it.time)
            }


        }

        private fun save(polynomialGroup: PolynomialGroup)
        {
            leftPolynomialValue = polynomialGroup.polLeftPolynomial.toString()

            rightPolynomialValue =
                polynomialGroup.polRightPolynomial.toString()

            resultPolynomialValue =
                polynomialGroup.polResPolynomial.toString()

            remainderPolynomialValue =
                polynomialGroup.polOstPolynomial.toString()
        }

    }
}