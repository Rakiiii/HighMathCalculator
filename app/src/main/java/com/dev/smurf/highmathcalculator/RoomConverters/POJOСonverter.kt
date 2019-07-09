package com.dev.smurf.highmathcalculator.RoomConverters

import androidx.room.TypeConverter
import com.dev.smurf.highmathcalculator.Polinoms.PolinomBase
import com.dev.smurf.highmathcalculator.Polinoms.PolinomFactory
import com.example.smurf.mtarixcalc.Matrix
import java.text.SimpleDateFormat
import java.util.*


//преобразование
class POJOСonverter
{
    companion object
    {

        //конвертация матрицы в строку
        @TypeConverter
        @JvmStatic
        fun fromMatrix(matrix: Matrix): String = matrix.toString()

        //конвертация строки в матрицу
        @TypeConverter
        @JvmStatic
        fun toMatrix(matrix: String): Matrix = Matrix(matrix)

        //конвертация времени в строку
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


        //конвертация строки во время
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

        //конвертация полинома в строку
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


        //конвертация строки к полиному
        @TypeConverter
        @JvmStatic
        fun toPolinom(polinom : String) : PolinomBase
        {
            return PolinomFactory().createPolinom(polinom)
        }
    }
}