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

class polAdapter( val context: Context , val polFirstPolinom : EditText, val polSecPolinom : EditText) : RecyclerView.Adapter<polAdapter.polViewHolder>()
{

    //TODO : переписать viewHolder на фрагменты для нормального отображения

    private var listOfPolinoms : ArrayList<PolinomGroup> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): polViewHolder {
        return polViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.polinom_expresisions , parent , false) )
    }

    override fun onBindViewHolder(holder: polViewHolder, position: Int) {
        holder.bind(listOfPolinoms[position])

        holder.leftPolinom.setOnCreateContextMenuListener( object : View.OnCreateContextMenuListener
        {
            override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
                if( menu != null)
                {
                    menu.add(1 ,1 ,1, "Paste to A").
                            setOnMenuItemClickListener( object : MenuItem.OnMenuItemClickListener
                            {
                                override fun onMenuItemClick(item: MenuItem?): Boolean {
                                    polFirstPolinom.text = SpannableStringBuilder(holder.leftPolinom.text.toString())
                                    return true
                                }
                            })
                    menu.add(2,2,2,"Paste to B").
                        setOnMenuItemClickListener( object : MenuItem.OnMenuItemClickListener
                        {
                            override fun onMenuItemClick(item: MenuItem?): Boolean {
                                polSecPolinom.text = SpannableStringBuilder(holder.leftPolinom.text.toString())
                                return true
                            }
                        })
                    menu.add(3,3,3,"Copy cofs").
                        setOnMenuItemClickListener( object : MenuItem.OnMenuItemClickListener
                        {
                            override fun onMenuItemClick(item: MenuItem?): Boolean {
                                var clipboardManager = context.getSystemService( Context.CLIPBOARD_SERVICE) as ClipboardManager
                                var clip = ClipData.newPlainText("some" , holder.leftPolinom.text.toString())
                                clipboardManager.primaryClip = clip
                                return true
                            }
                        })
                    menu.add(4,4,4,"Copy polinom").
                        setOnMenuItemClickListener( object :MenuItem.OnMenuItemClickListener
                        {
                            override fun onMenuItemClick(item: MenuItem?): Boolean {
                                var clipboardManager = context.getSystemService( Context.CLIPBOARD_SERVICE) as ClipboardManager
                                var clip = ClipData.newPlainText("some" , holder.leftPolinom.text.toString())
                                clipboardManager.primaryClip = clip
                                return true
                            }
                        })
                }
            }
        })

        holder.rightPolinom.setOnCreateContextMenuListener( object : View.OnCreateContextMenuListener
        {
            override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
                if( menu != null)
                {
                    menu.add(1 ,1 ,1, "Paste to A").
                        setOnMenuItemClickListener( object : MenuItem.OnMenuItemClickListener
                        {
                            override fun onMenuItemClick(item: MenuItem?): Boolean {
                                polFirstPolinom.text = SpannableStringBuilder(holder.rightPolinom.text.toString())
                                return true
                            }
                        })
                    menu.add(2,2,2,"Paste to B").
                        setOnMenuItemClickListener( object : MenuItem.OnMenuItemClickListener
                        {
                            override fun onMenuItemClick(item: MenuItem?): Boolean {
                                polSecPolinom.text = SpannableStringBuilder(holder.rightPolinom.text.toString())
                                return true
                            }
                        })
                    menu.add(3,3,3,"Copy cofs").
                        setOnMenuItemClickListener( object : MenuItem.OnMenuItemClickListener
                        {
                            override fun onMenuItemClick(item: MenuItem?): Boolean {
                                var clipboardManager = context.getSystemService( Context.CLIPBOARD_SERVICE) as ClipboardManager
                                var clip = ClipData.newPlainText("some" , holder.rightPolinom.text.toString())
                                clipboardManager.primaryClip = clip
                                return true
                            }
                        })
                    menu.add(4,4,4,"Copy polinom").
                        setOnMenuItemClickListener( object :MenuItem.OnMenuItemClickListener
                        {
                            override fun onMenuItemClick(item: MenuItem?): Boolean {
                                var clipboardManager = context.getSystemService( Context.CLIPBOARD_SERVICE) as ClipboardManager
                                var clip = ClipData.newPlainText("some" , holder.rightPolinom.text.toString())
                                clipboardManager.primaryClip = clip
                                return true
                            }
                        })
                }
            }
        })

        holder.resPolinom.setOnCreateContextMenuListener( object : View.OnCreateContextMenuListener
        {
            override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
                if( menu != null)
                {
                    menu.add(1 ,1 ,1, "Paste to A").
                        setOnMenuItemClickListener( object : MenuItem.OnMenuItemClickListener
                        {
                            override fun onMenuItemClick(item: MenuItem?): Boolean {
                                polFirstPolinom.text = SpannableStringBuilder(holder.resPolinom.text.toString())
                                return true
                            }
                        })
                    menu.add(2,2,2,"Paste to B").
                        setOnMenuItemClickListener( object : MenuItem.OnMenuItemClickListener
                        {
                            override fun onMenuItemClick(item: MenuItem?): Boolean {
                                polSecPolinom.text = SpannableStringBuilder(holder.resPolinom.text.toString())
                                return true
                            }
                        })
                    menu.add(3,3,3,"Copy cofs").
                        setOnMenuItemClickListener( object : MenuItem.OnMenuItemClickListener
                        {
                            override fun onMenuItemClick(item: MenuItem?): Boolean {
                                var clipboardManager = context.getSystemService( Context.CLIPBOARD_SERVICE) as ClipboardManager
                                var clip = ClipData.newPlainText("some" , holder.resPolinom.text.toString())
                                clipboardManager.primaryClip = clip
                                return true
                            }
                        })
                    menu.add(4,4,4,"Copy polinom").
                        setOnMenuItemClickListener( object :MenuItem.OnMenuItemClickListener
                        {
                            override fun onMenuItemClick(item: MenuItem?): Boolean {
                                var clipboardManager = context.getSystemService( Context.CLIPBOARD_SERVICE) as ClipboardManager
                                var clip = ClipData.newPlainText("some" , holder.resPolinom.text.toString())
                                clipboardManager.primaryClip = clip
                                return true
                            }
                        })
                }
            }
        })
        holder.ostPolinom.setOnCreateContextMenuListener( object : View.OnCreateContextMenuListener
        {
            override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
                if( menu != null)
                {
                    menu.add(1 ,1 ,1, "Paste to A").
                        setOnMenuItemClickListener( object : MenuItem.OnMenuItemClickListener
                        {
                            override fun onMenuItemClick(item: MenuItem?): Boolean {
                                polFirstPolinom.text = SpannableStringBuilder(holder.ostPolinom.text.toString().
                                    filter { s -> ( s!='O' ||
                                                    s!='s' ||
                                                    s!='t' ||
                                                    s!='=')
                                }.trim())
                                return true
                            }
                        })
                    menu.add(2,2,2,"Paste to B").
                        setOnMenuItemClickListener( object : MenuItem.OnMenuItemClickListener
                        {
                            override fun onMenuItemClick(item: MenuItem?): Boolean {
                                polSecPolinom.text = SpannableStringBuilder(holder.ostPolinom.text.toString().
                                    filter { s -> ( s!='O' ||
                                            s!='s' ||
                                            s!='t' ||
                                            s!='=')
                                    }.trim())
                                return true
                            }
                        })
                    menu.add(3,3,3,"Copy cofs").
                        setOnMenuItemClickListener( object : MenuItem.OnMenuItemClickListener
                        {
                            override fun onMenuItemClick(item: MenuItem?): Boolean {
                                var clipboardManager = context.getSystemService( Context.CLIPBOARD_SERVICE) as ClipboardManager
                                var clip = ClipData.newPlainText("some" , holder.ostPolinom.text.toString().
                                    filter { s -> ( s!='O' ||
                                            s!='s' ||
                                            s!='t' ||
                                            s!='=')
                                    }.trim())
                                clipboardManager.primaryClip = clip
                                return true
                            }
                        })
                    menu.add(4,4,4,"Copy polinom").
                        setOnMenuItemClickListener( object :MenuItem.OnMenuItemClickListener
                        {
                            override fun onMenuItemClick(item: MenuItem?): Boolean {
                                var clipboardManager = context.getSystemService( Context.CLIPBOARD_SERVICE) as ClipboardManager
                                var clip = ClipData.newPlainText("some" , holder.ostPolinom.text.toString().
                                    filter { s -> ( s!='O' ||
                                            s!='s' ||
                                            s!='t' ||
                                            s!='=')
                                    }.trim())
                                clipboardManager.primaryClip = clip
                                return true
                            }
                        })
                }
            }
        })

    }

    override fun getItemCount(): Int {
        return listOfPolinoms.size
    }

    fun addElement(PolinomGroup: PolinomGroup)
    {
        listOfPolinoms.add( 0 , PolinomGroup)
        notifyDataSetChanged()
    }

    fun clear()
    {
        listOfPolinoms.clear()
        notifyDataSetChanged()
    }

    fun removeElement(position: Int)
    {
        listOfPolinoms.removeAt(position)
        notifyDataSetChanged()
    }

    fun getData( pos : Int) = listOfPolinoms[pos]

    fun restoreItem(item : PolinomGroup, position : Int)
    {
        listOfPolinoms.add(position , item)
        notifyItemInserted(position)
    }

    fun getList() = listOfPolinoms

    fun setList( newArrayList: ArrayList<PolinomGroup>)
    {
        listOfPolinoms = newArrayList
        notifyDataSetChanged()
    }

    class polViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        var leftPolinom : TextView = itemView.findViewById(R.id.polLeftPolinom)
        private set
        var rightPolinom : TextView = itemView.findViewById(R.id.polRightPolinom)
        private set
        var resPolinom : TextView = itemView.findViewById(R.id.polResPolinom)
        private set
        var signPolinom : TextView = itemView.findViewById(R.id.polSignPolinom)
        private set
        var ostPolinom : TextView = itemView.findViewById(R.id.polOstPolinom)
        private set
        var timePolinom : TextView = itemView.findViewById(R.id.timePolinom)

        fun bind(PolinomGroup : PolinomGroup)
        {
            leftPolinom.text = PolinomGroup.polLeftPolinom.toString()
            if(!PolinomGroup.isRoots && PolinomGroup.polRightPolinom != null && PolinomGroup.polResPolinom != null)
            {
                rightPolinom.text = (PolinomGroup.polRightPolinom.toString() + '\n' + '=')
                signPolinom.text = PolinomGroup.polSignPolinom
                resPolinom.text = PolinomGroup.polResPolinom.toString()
                if (PolinomGroup.polOstPolinom != null)
                    ostPolinom.text = "Ost = " + PolinomGroup.polOstPolinom.toString()
                else ostPolinom.text = ""
            }
            else
            {
                if(PolinomGroup.roots != null) resPolinom.text = PolinomGroup.roots
            }

            PolinomGroup.time.let{
                var fmt = SimpleDateFormat(" HH:mm:ss dd MMM yyyy")
                fmt.calendar = it
                timePolinom.text = fmt.format(it?.time)
            }
        }
    }
}