package com.dev.smurf.highmathcalculator.ui.adapters

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.text.SpannableStringBuilder
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dev.smurf.highmathcalculator.R
import com.dev.smurf.highmathcalculator.Utils.*
import com.example.smurf.mtarixcalc.PolynomialGroup
import org.jetbrains.anko.imageBitmap
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat

class PolynomialAdapterImageView(
    val context: Context,
    val polFirstPolynomial: EditText,
    val polSecPolynomial: EditText
) :
    RecyclerView.Adapter<PolynomialAdapterImageView.PolynomialViewHolder>()
{

    //список элементов
    private var listOfPolynomials: ArrayList<PolynomialGroup> = ArrayList()


    //получить количество элементов в списке
    override fun getItemCount(): Int
    {
        return listOfPolynomials.size
    }


    //добавить новый элемент
    fun addElement(PolynomialGroup: PolynomialGroup)
    {
        listOfPolynomials.add(0, PolynomialGroup)
        notifyDataSetChanged()
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
    fun getList(): ArrayList<PolynomialGroup> = listOfPolynomials

    //заменить список элементов
    fun setList(newArrayList: ArrayList<PolynomialGroup>)
    {
        listOfPolynomials = newArrayList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PolynomialViewHolder
    {
        return PolynomialAdapterImageView.PolynomialViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.polynomial_expressions_imageview,
                parent,
                false
            )
        )


    }

    override fun onBindViewHolder(holder: PolynomialViewHolder, position: Int)
    {
        try
        {
            holder.bind(listOfPolynomials[position])
        }catch (e : Exception)
        {
            context.toast(e.toString())
        }
        //контекстное  меню для левого полинома
        holder.leftPolynomialImageView.setOnCreateContextMenuListener(object : View.OnCreateContextMenuListener
        {
            override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?)
            {
                if (menu != null)
                {
                    menu.add(1, 1, 1, "Paste to A").setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener
                    {
                        override fun onMenuItemClick(item: MenuItem?): Boolean
                        {
                            polFirstPolynomial.text = SpannableStringBuilder(holder.leftPolynomialValue)
                            return true
                        }
                    })
                    menu.add(2, 2, 2, "Paste to B").setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener
                    {
                        override fun onMenuItemClick(item: MenuItem?): Boolean
                        {
                            polSecPolynomial.text = SpannableStringBuilder(holder.leftPolynomialValue)
                            return true
                        }
                    })
                    menu.add(3, 3, 3, "Copy cofs").setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener
                    {
                        override fun onMenuItemClick(item: MenuItem?): Boolean
                        {
                            val clipboardManager =
                                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("some", holder.leftPolynomialValue)
                            clipboardManager.primaryClip = clip
                            return true
                        }
                    })
                    menu.add(4, 4, 4, "Copy polinom")
                        .setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener
                        {
                            override fun onMenuItemClick(item: MenuItem?): Boolean
                            {
                                val clipboardManager =
                                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("some", holder.leftPolynomialValue)
                                clipboardManager.primaryClip = clip
                                return true
                            }
                        })
                }
            }
        })

        //контестное меню для праавого элемента
        holder.rightPolynomialImageView.setOnCreateContextMenuListener(object : View.OnCreateContextMenuListener
        {
            override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?)
            {
                if (menu != null)
                {
                    menu.add(1, 1, 1, "Paste to A").setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener
                    {
                        override fun onMenuItemClick(item: MenuItem?): Boolean
                        {
                            polFirstPolynomial.text = SpannableStringBuilder(holder.rightPolynomialValue)
                            return true
                        }
                    })
                    menu.add(2, 2, 2, "Paste to B").setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener
                    {
                        override fun onMenuItemClick(item: MenuItem?): Boolean
                        {
                            polSecPolynomial.text = SpannableStringBuilder(holder.rightPolynomialValue)
                            return true
                        }
                    })
                    menu.add(3, 3, 3, "Copy cofs").setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener
                    {
                        override fun onMenuItemClick(item: MenuItem?): Boolean
                        {
                            val clipboardManager =
                                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("some", holder.rightPolynomialValue)
                            clipboardManager.primaryClip = clip
                            return true
                        }
                    })
                    menu.add(4, 4, 4, "Copy polinom")
                        .setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener
                        {
                            override fun onMenuItemClick(item: MenuItem?): Boolean
                            {
                                val clipboardManager =
                                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("some", holder.rightPolynomialValue)
                                clipboardManager.primaryClip = clip
                                return true
                            }
                        })
                }
            }
        })


        //контекстное меню для результирующего полинома
        holder.resultPolynomialImageView.setOnCreateContextMenuListener(object : View.OnCreateContextMenuListener
        {
            override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?)
            {
                if (menu != null)
                {
                    menu.add(1, 1, 1, "Paste to A").setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener
                    {
                        override fun onMenuItemClick(item: MenuItem?): Boolean
                        {
                            polFirstPolynomial.text = SpannableStringBuilder(holder.resultPolynomialValue)
                            return true
                        }
                    })
                    menu.add(2, 2, 2, "Paste to B").setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener
                    {
                        override fun onMenuItemClick(item: MenuItem?): Boolean
                        {
                            polSecPolynomial.text = SpannableStringBuilder(holder.resultPolynomialValue)
                            return true
                        }
                    })
                    menu.add(3, 3, 3, "Copy cofs").setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener
                    {
                        override fun onMenuItemClick(item: MenuItem?): Boolean
                        {
                            val clipboardManager =
                                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("some", holder.resultPolynomialValue)
                            clipboardManager.primaryClip = clip
                            return true
                        }
                    })
                    menu.add(4, 4, 4, "Copy polinom")
                        .setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener
                        {
                            override fun onMenuItemClick(item: MenuItem?): Boolean
                            {
                                val clipboardManager =
                                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("some", holder.resultPolynomialValue)
                                clipboardManager.primaryClip = clip
                                return true
                            }
                        })
                }
            }
        })

        //контекстное меню для остаточное полинома
        holder.remainderPolynomialImageView.setOnCreateContextMenuListener(object : View.OnCreateContextMenuListener
        {
            override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?)
            {
                if (menu != null)
                {
                    menu.add(1, 1, 1, "Paste to A").setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener
                    {
                        override fun onMenuItemClick(item: MenuItem?): Boolean
                        {
                            polFirstPolynomial.text =
                                SpannableStringBuilder(holder.remainderPolynomialValue)
                            return true
                        }
                    })
                    menu.add(2, 2, 2, "Paste to B").setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener
                    {
                        override fun onMenuItemClick(item: MenuItem?): Boolean
                        {
                            polSecPolynomial.text =
                                SpannableStringBuilder(holder.remainderPolynomialValue)
                            return true
                        }
                    })
                    menu.add(3, 3, 3, "Copy cofs").setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener
                    {
                        override fun onMenuItemClick(item: MenuItem?): Boolean
                        {
                            val clipboardManager =
                                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("some", holder.remainderPolynomialValue)
                            clipboardManager.primaryClip = clip
                            return true
                        }
                    })
                    menu.add(4, 4, 4, "Copy polinom")
                        .setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener
                        {
                            override fun onMenuItemClick(item: MenuItem?): Boolean
                            {
                                val clipboardManager =
                                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("some", holder.remainderPolynomialValue)
                                clipboardManager.primaryClip = clip
                                return true
                            }
                        })
                }
            }
        })


    }

    class PolynomialViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
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

        var remainderPolynomialImageView: ImageView = itemView.findViewById(R.id.remainderPolynomialIM)
            private set

        var signumImageView: ImageView = itemView.findViewById(R.id.operationSignPolynomialIM)

        var timePolynomial: TextView = itemView.findViewById(R.id.timePolynomialIM)
            private set


        fun bind(polynomialGroup: PolynomialGroup)
        {
            save(polynomialGroup)

            val blackPainter = CanvasRenderSpecification.createBlackPainter()

            polynomialGroup.polLeftPolynomial.let {
                val leftBitmap = Bitmap.createBitmap(
                    blackPainter.getPolynomialWidth(it).toInt(),
                    blackPainter.getPolynomialHigh(it).toInt(),
                    Bitmap.Config.ARGB_8888
                )

                val canvas = Canvas(leftBitmap)

                if (polynomialGroup.roots == null)
                {
                    canvas.drawPolynomial(
                        polynomialGroup.polLeftPolynomial, CanvasRenderSpecification.x,
                        CanvasRenderSpecification.y,
                        blackPainter
                    )
                }
                else
                {
                    canvas.drawEquation(
                        it, CanvasRenderSpecification.x,
                        CanvasRenderSpecification.y,
                        blackPainter
                    )
                }


                //testPolynomial.imageBitmap = leftBitmap
                leftPolynomialImageView.imageBitmap = leftBitmap


            }

            polynomialGroup.polRightPolynomial.let {
                if (it != null)
                {
                    val rightBitmap = Bitmap.createBitmap(
                        blackPainter.getPolynomialWidth(it).toInt(),
                        blackPainter.getPolynomialHigh(it).toInt(),
                        Bitmap.Config.ARGB_8888
                    )

                    val canvas = Canvas(rightBitmap)

                    if (polynomialGroup.roots == null)
                        canvas.drawPolynomial(
                            it, CanvasRenderSpecification.x,
                            CanvasRenderSpecification.y,
                            blackPainter
                        )
                    else canvas.drawEquation(
                        it, CanvasRenderSpecification.x,
                        CanvasRenderSpecification.y,
                        blackPainter
                    )


                    rightPolynomialImageView.imageBitmap = rightBitmap
                }
            }

            polynomialGroup.polResPolynomial.let {
                if (it != null)
                {
                    val resultBitmap = Bitmap.createBitmap(
                        blackPainter.getPolynomialWidth(it).toInt(),
                        blackPainter.getPolynomialHigh(it).toInt(),
                        Bitmap.Config.ARGB_8888
                    )

                    val canvas = Canvas(resultBitmap)

                    if (polynomialGroup.roots == null)
                        canvas.drawPolynomial(
                            it, CanvasRenderSpecification.x,
                            CanvasRenderSpecification.y,
                            blackPainter
                        )
                    else
                        canvas.drawPolynomialRoots(
                            polynomialGroup.roots!!, CanvasRenderSpecification.x,
                            CanvasRenderSpecification.y,
                            blackPainter
                        )



                    resultPolynomialImageView.imageBitmap = resultBitmap
                }
            }

            polynomialGroup.polOstPolynomial.let {
                if (it != null)
                {
                    val remainderBitmap = Bitmap.createBitmap(
                        blackPainter.getPolynomialWidth(it).toInt(),
                        blackPainter.getPolynomialHigh(it).toInt(),
                        Bitmap.Config.ARGB_8888
                    )

                    val canvas = Canvas(remainderBitmap)

                    if (polynomialGroup.roots == null)
                        canvas.drawPolynomial(
                            it, CanvasRenderSpecification.x,
                            CanvasRenderSpecification.y,
                            blackPainter
                        )

                    remainderPolynomialImageView.imageBitmap = remainderBitmap
                }
            }

            polynomialGroup.polSignPolynomial.let {
                val arr = FloatArray(it.length)
                blackPainter.getTextWidths(it, arr)
                val width = arr.sum()
                val signumBitmap = Bitmap.createBitmap(
                    width.toInt() + blackPainter.getPolynomialWidth(polynomialGroup.polResPolynomial!!).toInt(),
                    CanvasRenderSpecification.getLetterHigh(blackPainter).toInt(),
                    Bitmap.Config.ARGB_8888
                )

                val canvas = Canvas(signumBitmap)

                canvas.drawText(it, CanvasRenderSpecification.x, CanvasRenderSpecification.getLetterHigh(blackPainter)/2, blackPainter)

                signumImageView.imageBitmap = signumBitmap
            }

            polynomialGroup.time.let {
                val fmt = SimpleDateFormat(" HH:mm:ss dd MMM yyyy")
                fmt.calendar = it
                timePolynomial.text = fmt.format(it.time)
            }


        }

        fun save(polynomialGroup: PolynomialGroup)
        {
            leftPolynomialValue = polynomialGroup.polLeftPolynomial.toString()

            rightPolynomialValue =
                if (polynomialGroup.polRightPolynomial != null) polynomialGroup.polRightPolynomial.toString() else ""

            resultPolynomialValue =
                if (polynomialGroup.polResPolynomial != null) polynomialGroup.polResPolynomial.toString() else ""

            remainderPolynomialValue =
                if (polynomialGroup.polOstPolynomial != null) polynomialGroup.polOstPolynomial.toString() else ""
        }

    }
}