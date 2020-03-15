package com.dev.smurf.highmathcalculator.ui.adapters

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.SpannableStringBuilder
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.EditText

class ContextMenuListener(val context: Context, val firstEditText: EditText, val secondEditText: EditText, msg : String) : View.OnCreateContextMenuListener
{

    val value = msg.filterNot { s -> s == '=' }
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?)
    {
        if (menu != null)
        {
            menu.add(0, 0, 0, "Paste to A")
                .setOnMenuItemClickListener( MatrixOnMenuItemClickListener(firstEditText,value) )
            menu.add(1, 1, 1, "Paste to B")
                .setOnMenuItemClickListener( MatrixOnMenuItemClickListener(secondEditText,value) )
            menu.add(2, 2, 2, "Copy").setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener
            {
                override fun onMenuItemClick(item: MenuItem?): Boolean
                {
                    val clipboardManager =
                        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip =
                        ClipData.newPlainText("some",value)
                    clipboardManager.primaryClip = clip
                    return true
                }
            })
        }
    }

    class MatrixOnMenuItemClickListener(val matrix: EditText , val value : String) : MenuItem.OnMenuItemClickListener
    {
        override fun onMenuItemClick(item: MenuItem?): Boolean
        {
            matrix.text =
                SpannableStringBuilder(value)
            return true
        }
    }
}