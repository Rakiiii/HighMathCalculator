package com.dev.smurf.highmathcalculator.RoomConverters

import androidx.room.TypeConverter
import com.dev.smurf.highmathcalculator.Polinoms.PolinomBase
import com.dev.smurf.highmathcalculator.Polinoms.PolinomFactory
import com.example.smurf.mtarixcalc.Matrix
import java.text.SimpleDateFormat
import java.util.*

class MatrixConverter
{
    companion object
    {

        @TypeConverter
        @JvmStatic
        fun fromMatrix(matrix: Matrix): String = matrix.toString()

        @TypeConverter
        @JvmStatic
        fun toMatrix(matrix: String): Matrix = Matrix(matrix)

        @TypeConverter
        @JvmStatic
        fun fromTime(time : java.util.GregorianCalendar? ) : String
        {
            if(time != null)
            {
                var fmt = SimpleDateFormat(" HH:mm:ss dd MMM yyyy")
                fmt.calendar = time
                return fmt.format(time.time)
            }else return ""
        }

        @TypeConverter
        @JvmStatic
        fun toTime(time : String) : java.util.GregorianCalendar?
        {
            if(time == "")return null
            else
            {
                var fmt = SimpleDateFormat(" HH:mm:ss dd MMM yyyy")
                var date = fmt.parse(time)
                val result = GregorianCalendar()
                result.time = date
                return result
            }
        }

        @TypeConverter
        @JvmStatic
        fun fromPolinom(polinom : PolinomBase) : String
        {
            if(polinom.isSolved())
            {
                return polinom.toString() + "|" + polinom.stringWithRoots()
            }
            else return polinom.toString()
        }

        @TypeConverter
        @JvmStatic
        fun toPolinom(polinom : String) : PolinomBase
        {
            return PolinomFactory().createPolinom(polinom)
        }
    }
}