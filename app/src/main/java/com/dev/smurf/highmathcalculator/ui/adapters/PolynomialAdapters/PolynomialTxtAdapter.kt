package com.example.smurf.mtarixcalc

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.SpannableStringBuilder
import android.view.*
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dev.smurf.highmathcalculator.R
import java.text.SimpleDateFormat

class PolynomialTxtAdapter(val context: Context, val polFirstPolynomial: EditText, val polSecPolynomial: EditText) :
    RecyclerView.Adapter<PolynomialTxtAdapter.PolynomialViewHolder>()
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
        notifyDataSetChanged()
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
        listOfPolynomials = newArrayList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PolynomialViewHolder
    {
        return PolynomialViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.polinom_expresisions, parent, false))
    }

    override fun onBindViewHolder(holder: PolynomialViewHolder, position: Int)
    {
        holder.bind(listOfPolynomials[position])

        //контекстное  меню для левого полинома
        holder.leftPolinom.setOnCreateContextMenuListener(object : View.OnCreateContextMenuListener
        {
            override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?)
            {
                if (menu != null)
                {
                    menu.add(1, 1, 1, "Paste to A").setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener
                    {
                        override fun onMenuItemClick(item: MenuItem?): Boolean
                        {
                            polFirstPolynomial.text = SpannableStringBuilder(holder.leftPolinom.text.toString())
                            return true
                        }
                    })
                    menu.add(2, 2, 2, "Paste to B").setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener
                    {
                        override fun onMenuItemClick(item: MenuItem?): Boolean
                        {
                            polSecPolynomial.text = SpannableStringBuilder(holder.leftPolinom.text.toString())
                            return true
                        }
                    })
                    menu.add(3, 3, 3, "Copy cofs").setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener
                    {
                        override fun onMenuItemClick(item: MenuItem?): Boolean
                        {
                            var clipboardManager =
                                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            var clip = ClipData.newPlainText("some", holder.leftPolinom.text.toString())
                            clipboardManager.primaryClip = clip
                            return true
                        }
                    })
                    menu.add(4, 4, 4, "Copy polinom")
                        .setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener
                        {
                            override fun onMenuItemClick(item: MenuItem?): Boolean
                            {
                                var clipboardManager =
                                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                var clip = ClipData.newPlainText("some", holder.leftPolinom.text.toString())
                                clipboardManager.primaryClip = clip
                                return true
                            }
                        })
                }
            }
        })

        //контестное меню для праавого элемента
        holder.rightPolinom.setOnCreateContextMenuListener(object : View.OnCreateContextMenuListener
        {
            override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?)
            {
                if (menu != null)
                {
                    menu.add(1, 1, 1, "Paste to A").setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener
                    {
                        override fun onMenuItemClick(item: MenuItem?): Boolean
                        {
                            polFirstPolynomial.text = SpannableStringBuilder(holder.rightPolinom.text.toString())
                            return true
                        }
                    })
                    menu.add(2, 2, 2, "Paste to B").setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener
                    {
                        override fun onMenuItemClick(item: MenuItem?): Boolean
                        {
                            polSecPolynomial.text = SpannableStringBuilder(holder.rightPolinom.text.toString())
                            return true
                        }
                    })
                    menu.add(3, 3, 3, "Copy cofs").setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener
                    {
                        override fun onMenuItemClick(item: MenuItem?): Boolean
                        {
                            var clipboardManager =
                                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            var clip = ClipData.newPlainText("some", holder.rightPolinom.text.toString())
                            clipboardManager.primaryClip = clip
                            return true
                        }
                    })
                    menu.add(4, 4, 4, "Copy polinom")
                        .setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener
                        {
                            override fun onMenuItemClick(item: MenuItem?): Boolean
                            {
                                var clipboardManager =
                                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                var clip = ClipData.newPlainText("some", holder.rightPolinom.text.toString())
                                clipboardManager.primaryClip = clip
                                return true
                            }
                        })
                }
            }
        })


        //контекстное меню для результирующего полинома
        holder.resPolinom.setOnCreateContextMenuListener(object : View.OnCreateContextMenuListener
        {
            override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?)
            {
                if (menu != null)
                {
                    menu.add(1, 1, 1, "Paste to A").setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener
                    {
                        override fun onMenuItemClick(item: MenuItem?): Boolean
                        {
                            polFirstPolynomial.text = SpannableStringBuilder(holder.resPolinom.text.toString())
                            return true
                        }
                    })
                    menu.add(2, 2, 2, "Paste to B").setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener
                    {
                        override fun onMenuItemClick(item: MenuItem?): Boolean
                        {
                            polSecPolynomial.text = SpannableStringBuilder(holder.resPolinom.text.toString())
                            return true
                        }
                    })
                    menu.add(3, 3, 3, "Copy cofs").setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener
                    {
                        override fun onMenuItemClick(item: MenuItem?): Boolean
                        {
                            var clipboardManager =
                                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            var clip = ClipData.newPlainText("some", holder.resPolinom.text.toString())
                            clipboardManager.primaryClip = clip
                            return true
                        }
                    })
                    menu.add(4, 4, 4, "Copy polinom")
                        .setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener
                        {
                            override fun onMenuItemClick(item: MenuItem?): Boolean
                            {
                                var clipboardManager =
                                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                var clip = ClipData.newPlainText("some", holder.resPolinom.text.toString())
                                clipboardManager.primaryClip = clip
                                return true
                            }
                        })
                }
            }
        })

        //контекстное меню для остаточное полинома
        holder.ostPolinom.setOnCreateContextMenuListener(object : View.OnCreateContextMenuListener
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
                                SpannableStringBuilder(holder.ostPolinom.text.toString().filter { s ->
                                    (s != 'O' ||
                                            s != 's' ||
                                            s != 't' ||
                                            s != '=')
                                }.trim())
                            return true
                        }
                    })
                    menu.add(2, 2, 2, "Paste to B").setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener
                    {
                        override fun onMenuItemClick(item: MenuItem?): Boolean
                        {
                            polSecPolynomial.text =
                                SpannableStringBuilder(holder.ostPolinom.text.toString().filter { s ->
                                    (s != 'O' ||
                                            s != 's' ||
                                            s != 't' ||
                                            s != '=')
                                }.trim())
                            return true
                        }
                    })
                    menu.add(3, 3, 3, "Copy cofs").setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener
                    {
                        override fun onMenuItemClick(item: MenuItem?): Boolean
                        {
                            var clipboardManager =
                                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            var clip = ClipData.newPlainText("some", holder.ostPolinom.text.toString().filter { s ->
                                (s != 'O' ||
                                        s != 's' ||
                                        s != 't' ||
                                        s != '=')
                            }.trim())
                            clipboardManager.primaryClip = clip
                            return true
                        }
                    })
                    menu.add(4, 4, 4, "Copy polinom")
                        .setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener
                        {
                            override fun onMenuItemClick(item: MenuItem?): Boolean
                            {
                                var clipboardManager =
                                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                var clip = ClipData.newPlainText("some", holder.ostPolinom.text.toString().filter { s ->
                                    (s != 'O' ||
                                            s != 's' ||
                                            s != 't' ||
                                            s != '=')
                                }.trim())
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
        var leftPolinom: TextView = itemView.findViewById(R.id.polLeftPolinom)
            private set
        var rightPolinom: TextView = itemView.findViewById(R.id.polRightPolinom)
            private set
        var resPolinom: TextView = itemView.findViewById(R.id.polResPolinom)
            private set
        var signPolinom: TextView = itemView.findViewById(R.id.polSignPolinom)
            private set
        var ostPolinom: TextView = itemView.findViewById(R.id.polOstPolinom)
            private set
        var timePolinom: TextView = itemView.findViewById(R.id.timePolinom)

        fun bind(PolynomialGroup: PolynomialGroup)
        {
            leftPolinom.text = PolynomialGroup.polLeftPolynomial.toString()

            if (PolynomialGroup.polRightPolynomial != null)
            {
                rightPolinom.text = (PolynomialGroup.polRightPolynomial.toString())
            }
            signPolinom.text = PolynomialGroup.polSignPolynomial
            if (PolynomialGroup.polResPolynomial != null)
            {
                resPolinom.text = PolynomialGroup.polResPolynomial.toString()
            }
            if (PolynomialGroup.polOstPolynomial != null)
            {
                ostPolinom.text = "Ost = " + PolynomialGroup.polOstPolynomial.toString()
            }
            else ostPolinom.text = ""

            PolynomialGroup.time.let {
                val fmt = SimpleDateFormat(" HH:mm:ss dd MMM yyyy")
                fmt.calendar = it
                timePolinom.text = fmt.format(it.time)
            }
        }
    }
}
