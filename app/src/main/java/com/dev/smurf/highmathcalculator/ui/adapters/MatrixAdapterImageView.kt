package com.dev.smurf.highmathcalculator.ui.adapters


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dev.smurf.highmathcalculator.R
import com.dev.smurf.highmathcalculator.Utils.drawMatrix
import com.example.smurf.mtarixcalc.MatrixGroup
import org.jetbrains.anko.imageBitmap
import java.text.SimpleDateFormat
import kotlin.math.roundToInt


class MatrixAdapterImageView(val context: Context , val firstMatrix : EditText , val secondMatrix : EditText ) : RecyclerView.Adapter<MatrixAdapterImageView.MatrixViewHolder>()
{

    //списое элементов
    private var listOfMatrices : ArrayList<MatrixGroup> = ArrayList()

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
        //Log.d("RV@" , "remove from position:" + position.toString())
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
    fun setList( newArray : ArrayList<MatrixGroup>)
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
       return MatrixViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.matrix_expressions_imageview , parent , false ) )
    }


    override fun onBindViewHolder(holder: MatrixViewHolder, position: Int)
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
                        .setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener
                        {
                            override fun onMenuItemClick(item: MenuItem?): Boolean
                            {
                                firstMatrix.text = SpannableStringBuilder(holder.leftMatrixValue)
                                return true
                            }
                        })

                    //втсавка вл вторую матрицу
                    menu.add(1, 1, 1, "Paste to B")
                        .setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener
                        {
                            override fun onMenuItemClick(item: MenuItem?): Boolean
                            {
                                secondMatrix.text = SpannableStringBuilder(holder.leftMatrixValue)
                                return true
                            }
                        })

                    //вставка в клипбоард
                    menu.add(2,2,2,"Copy").
                        setOnMenuItemClickListener( object : MenuItem.OnMenuItemClickListener
                        {
                            override fun onMenuItemClick(item: MenuItem?): Boolean
                            {
                                val clipboard = context.getSystemService( Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("some" , holder.leftMatrixValue)
                                clipboard.primaryClip = clip
                                return true
                            }
                        })
                }
            }
        })



        holder.resMatrix.setOnCreateContextMenuListener( object : View.OnCreateContextMenuListener
        {
            override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?)
            {
                if(menu != null)
                {
                    menu.add(0,0,0,"Paste to A").
                        setOnMenuItemClickListener( object : MenuItem.OnMenuItemClickListener
                        {
                            override fun onMenuItemClick(item: MenuItem?): Boolean
                            {
                                firstMatrix.text = SpannableStringBuilder(holder.resMatrixValue)
                                return true
                            }
                        })
                    menu.add(1, 1, 1, "Paste to B")
                        .setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener
                        {
                            override fun onMenuItemClick(item: MenuItem?): Boolean
                            {
                                secondMatrix.text = SpannableStringBuilder(holder.resMatrixValue)
                                return true
                            }
                        })
                    menu.add(2,2,2,"Copy").setOnMenuItemClickListener( object : MenuItem.OnMenuItemClickListener
                    {
                        override fun onMenuItemClick(item: MenuItem?): Boolean
                        {
                            val clipboardManager  = context.getSystemService( Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("some" , holder.resMatrixValue)
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
                            override fun onMenuItemClick(item: MenuItem?): Boolean
                            {
                                firstMatrix.text = SpannableStringBuilder(holder.rightMatrixValue.filterNot { s -> s == '=' })
                                return true
                            }
                        })
                    menu.add(1, 1, 1, "Paste to B")
                        .setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener
                        {
                            override fun onMenuItemClick(item: MenuItem?): Boolean
                            {
                                secondMatrix.text = SpannableStringBuilder(holder.rightMatrixValue.filterNot { s -> s == '=' })
                                return true
                            }
                        })
                    menu.add(2,2,2,"Copy").setOnMenuItemClickListener( object : MenuItem.OnMenuItemClickListener
                    {
                        override fun onMenuItemClick(item: MenuItem?): Boolean
                        {
                            val clipboardManager  = context.getSystemService( Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("some" , holder.rightMatrixValue.filterNot { s -> s == '=' })
                            clipboardManager.primaryClip = clip
                            return true
                        }
                    })
                }
            }
        })
    }




    class MatrixViewHolder constructor(itemView : View ) : RecyclerView.ViewHolder (itemView)
    {
        val width : Float = 40.0f
        val heigh : Float = 40.0f
        val textSize : Float = 40.0f

        val zero : Float = 0.0f

        var leftMatrix : ImageView = itemView.findViewById(R.id.leftMatrix)
            private set
        var rightMatrix : ImageView = itemView.findViewById(R.id.rightMatrix)
            private set
        var resMatrix : ImageView = itemView.findViewById(R.id.resMatrix)
            private set
        var sign : ImageView = itemView.findViewById(R.id.operationSignm)
            private set
        var timeMatrix : TextView = itemView.findViewById(R.id.timeMatrix)

        lateinit var leftMatrixValue : String
            private set
        lateinit var rightMatrixValue : String
            private set
        lateinit var resMatrixValue : String
            private set

        fun bind(group : MatrixGroup)
        {

            save(group)

            var blackPainter = Paint(Color.BLACK)


            blackPainter.textSize = textSize

            blackPainter.strokeWidth = 5.0f

            group.leftMatrix.let {

                Log.d("left@" , "width:" + it.width.toString() + " height:" + it.height.toString() )


                val mBitmap = Bitmap.createBitmap((it.maxWidthInString()+1) * textSize.roundToInt() ,
                    (it.height * it.maxFractionsInColumn()) * textSize.roundToInt() ,
                    Bitmap.Config.ARGB_8888)


                val canvas = Canvas(mBitmap)


                canvas.drawMatrix( it , width , heigh , blackPainter , group.sign == "det")

                leftMatrix.imageBitmap = mBitmap
            }

            /*group.rightMatrix.let {

                    Log.d("right@" , "width:" + it.width.toString() + " height:" + it.height.toString() )

                    if(!it.isEmpty())
                    {
                        val mBitmap = Bitmap.createBitmap(
                            (it.maxWidthInString() + 4) * textSize.roundToInt(),
                            (it.height * it.maxFractionsInColumn()) * textSize.roundToInt(),
                            Bitmap.Config.ARGB_8888
                        )


                        val canvas = Canvas(mBitmap)


                        canvas.drawMatrix(it, width, heigh, blackPainter, group.sign == "det")

                        rightMatrix.imageBitmap = mBitmap
                    }
                    else
                    {
                        val mBitmap= Bitmap.createBitmap(
                            3 * textSize.roundToInt(),
                            6 * textSize.roundToInt(),
                            Bitmap.Config.ARGB_8888 )

                        val canvas = Canvas(mBitmap)

                        canvas.drawText("=" , textSize , 3*textSize , blackPainter)

                        rightMatrix.imageBitmap = mBitmap
                    }
                }*/

            /*group.resMatrix.let {

                Log.d("res@" , "width:" + it.width.toString() + " height:" + it.height.toString() )

                val mBitmap = Bitmap.createBitmap((it.maxWidthInString()+4) * textSize.roundToInt() ,
                    (it.height * it.maxFractionsInColumn()) * textSize.roundToInt() ,
                    Bitmap.Config.ARGB_8888)


                val canvas = Canvas(mBitmap)

                if(it.isNumber())
                    canvas.drawText( it.toString() , width+textSize , heigh+textSize , blackPainter)
                else
                    canvas.drawMatrix( it , width , heigh , blackPainter , group.sign == "det")

                resMatrix.imageBitmap = mBitmap
            }*/


            group.sign.let{
                val mBitmap = Bitmap.createBitmap(
                    (it.length + 1) * textSize.roundToInt() ,
                    4*textSize.roundToInt() ,
                    Bitmap.Config.ARGB_8888)

                val canvas = Canvas(mBitmap)

                canvas.drawText(it,width,2*textSize,blackPainter)

                sign.imageBitmap = mBitmap
            }

            group.time.let{
                val fmt = SimpleDateFormat(" HH:mm:ss dd MMM yyyy")
                fmt.calendar = it
                timeMatrix.text = fmt.format(it?.time)
            }
        }

        private fun save(matrixGroup: MatrixGroup)
        {
            leftMatrixValue = matrixGroup.leftMatrix.toString()
            rightMatrixValue = matrixGroup.rightMatrix.toString()
            resMatrixValue = matrixGroup.resMatrix.toString()
        }


    }
}