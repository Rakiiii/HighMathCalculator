package com.dev.smurf.highmathcalculator.ui.adapters.MatrixAdapters

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.SpannableStringBuilder
import android.view.*
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dev.smurf.highmathcalculator.R
import com.dev.smurf.highmathcalculator.ui.POJO.MatrixGroup
import java.text.SimpleDateFormat


class MatrixAdapter(val context: Context, val firstMatrix : EditText, val secondMatrix : EditText ) : RecyclerView.Adapter<MatrixAdapter.matrixViewHolder>()
{

        //списое элементов
        private var listOfMatrices : MutableList<MatrixGroup> = ArrayList()

        //добавление нового элеменат
        fun addNewElem(group : MatrixGroup)
        {
            listOfMatrices.add(0 ,group)
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
        fun removeElement( position: Int)
        {
            listOfMatrices.removeAt(position)
            //Log.d("RV@" , "remove from position")
            notifyDataSetChanged()
        }

        //получить элемент из позиции position
        fun getData( position: Int) : MatrixGroup
        {
            //Log.d("RV@" , "get data")
            return listOfMatrices[position]
        }

        //вставить элемент MatrixGroup в позицию position
        fun restoreItem(position: Int, MatrixGroup: MatrixGroup)
        {
            listOfMatrices.add(position , MatrixGroup)
            //Log.d("RV@" , "restore item")
            notifyItemInserted(position)
        }

        //установить новый список элементов
        fun setList( newArray : MutableList<MatrixGroup>)
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



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): matrixViewHolder
    {
        return matrixViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.matrix_expressions, parent, false)
        )
    }

    override fun onBindViewHolder(holder: matrixViewHolder, position: Int)
    {
        holder.bind(listOfMatrices[position])


        //листенер для контекстного меню на левую матрицу
        holder.leftMatrix.setOnCreateContextMenuListener( object : View.OnCreateContextMenuListener
        {
            override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?)
            {
                if(menu != null)
                {
                    //вставка в первую матрицу
                    menu.add(0, 0, 0, "Paste to A")
                        .setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener {
                            override fun onMenuItemClick(item: MenuItem?): Boolean {
                                firstMatrix.text = SpannableStringBuilder(holder.leftMatrix.text.toString())
                                return true
                            }
                        })

                    //втсавка вл вторую матрицу
                    menu.add(1, 1, 1, "Paste to B")
                        .setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener {
                            override fun onMenuItemClick(item: MenuItem?): Boolean {
                                secondMatrix.text = SpannableStringBuilder(holder.leftMatrix.text.toString())
                                return true
                            }
                        })

                    //вставка в клипбоард
                    menu.add(2,2,2,"Copy").
                        setOnMenuItemClickListener( object : MenuItem.OnMenuItemClickListener
                        {
                            override fun onMenuItemClick(item: MenuItem?): Boolean {
                                val clipboard = context.getSystemService( Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("some" , holder.leftMatrix.text.toString())
                                clipboard.primaryClip = clip
                                return true
                            }
                        })
                }
            }
        })



        holder.resMatrix.setOnCreateContextMenuListener( object : View.OnCreateContextMenuListener
        {
            override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
                if(menu != null)
                {
                    menu.add(0,0,0,"Paste to A").
                        setOnMenuItemClickListener( object : MenuItem.OnMenuItemClickListener
                        {
                            override fun onMenuItemClick(item: MenuItem?): Boolean {
                                firstMatrix.text = SpannableStringBuilder(holder.resMatrix.text.toString())
                                return true
                            }
                        })
                    menu.add(1, 1, 1, "Paste to B")
                        .setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener {
                            override fun onMenuItemClick(item: MenuItem?): Boolean {
                                secondMatrix.text = SpannableStringBuilder(holder.resMatrix.text.toString())
                                return true
                            }
                        })
                    menu.add(2,2,2,"Copy").setOnMenuItemClickListener( object : MenuItem.OnMenuItemClickListener
                    {
                        override fun onMenuItemClick(item: MenuItem?): Boolean {
                            val clipboardManager  = context.getSystemService( Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("some" , holder.resMatrix.text.toString())
                            clipboardManager.primaryClip = clip
                            return true
                        }
                    })
                }
            }

        })

        holder.rightMatrix.setOnCreateContextMenuListener( object  : View.OnCreateContextMenuListener
        {
            override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
                if(menu != null)
                {
                    menu.add(0,0,0,"Paste to A").
                        setOnMenuItemClickListener( object : MenuItem.OnMenuItemClickListener
                        {
                            override fun onMenuItemClick(item: MenuItem?): Boolean {
                                firstMatrix.text = SpannableStringBuilder(holder.rightMatrix.text.toString().filterNot { s -> s == '=' })
                                return true
                            }
                        })
                    menu.add(1, 1, 1, "Paste to B")
                        .setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener {
                            override fun onMenuItemClick(item: MenuItem?): Boolean {
                                secondMatrix.text = SpannableStringBuilder(holder.rightMatrix.text.toString().filterNot { s -> s == '=' })
                                return true
                            }
                        })
                    menu.add(2,2,2,"Copy").setOnMenuItemClickListener( object : MenuItem.OnMenuItemClickListener
                    {
                        override fun onMenuItemClick(item: MenuItem?): Boolean {
                            val clipboardManager  = context.getSystemService( Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("some" , holder.rightMatrix.text.toString().filterNot { s -> s == '=' })
                            clipboardManager.primaryClip = clip
                            return true
                        }
                    })
                }
            }
        })
    }




        class matrixViewHolder constructor(itemView : View ) : RecyclerView.ViewHolder (itemView)
        {




            var leftMatrix : TextView = itemView.findViewById(R.id.leftMatrix)
                private set
            var rightMatrix : TextView = itemView.findViewById(R.id.rightMatrix)
                private set
            var resMatrix : TextView = itemView.findViewById(R.id.resMatrix)
                private set
            var sign : TextView = itemView.findViewById(R.id.operationSignm)
                private set
            var timeMatrix : TextView = itemView.findViewById(R.id.timeMatrix)


            fun bind(group : MatrixGroup)
            {
                leftMatrix.text = group.leftMatrix.toString('|')
                if(!group.rightMatrix.isEmpty())
                {
                    rightMatrix.text =(group.rightMatrix.toString('|').substringBefore('\n') + "    =")
                                if(!group.rightMatrix.isNumber())
                                {
                                    rightMatrix.text = rightMatrix.text.toString() + "\n"    +
                                    group.rightMatrix.toString('|').substringAfter('\n')
                                }
                }
                resMatrix.text = group.resMatrix.toString('|')
                sign.text = group.sign

                group.time.let{
                    var fmt = SimpleDateFormat(" HH:mm:ss dd MMM yyyy")
                    fmt.calendar = it
                    timeMatrix.text = fmt.format(it?.time)
                }
            }

        }
    }