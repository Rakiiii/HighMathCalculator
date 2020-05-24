package com.dev.smurf.highmathcalculator.RoomConverters

import androidx.room.TypeConverter
import com.dev.smurf.highmathcalculator.Matrix.Matrix
import com.dev.smurf.highmathcalculator.Polynomials.PolynomialBase
import com.dev.smurf.highmathcalculator.Polynomials.PolynomialFactory
import com.dev.smurf.highmathcalculator.Polynomials.PolynomialRoots
import java.text.SimpleDateFormat
import java.util.*


//преобразование
class POJOConverter
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
        fun toMatrix(matrix: String): Matrix = Matrix.createMatrix(matrix)

        //конвертация времени в строку
        @TypeConverter
        @JvmStatic
        fun fromTime(time : java.util.GregorianCalendar? ) : String
        {
            if(time != null)
            {
                val fmt = SimpleDateFormat(" HH:mm:ss dd MMM yyyy")
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
                val fmt = SimpleDateFormat(" HH:mm:ss dd MMM yyyy")
                val date = fmt.parse(time)
                val result = GregorianCalendar()
                result.time = date
                return result
            }
        }

        //конвертация полинома в строку
        @TypeConverter
        @JvmStatic
        fun fromPolynomial(polynomial : PolynomialBase?) : String
        {
            return  if(polynomial != null)polynomial.toString() else ""
        }


        //конвертация строки к полиному
        @TypeConverter
        @JvmStatic
        fun toPolynomial(polynomial : String) : PolynomialBase?
        {
            return if(polynomial != "")PolynomialFactory().createPolynomial(polynomial) else null
        }


        //конвертация корней полинома к строке
        @TypeConverter
        @JvmStatic
        fun fromPolynomialRoots(polynomialRoots : PolynomialRoots?) : String
        {
            return if(polynomialRoots != null)polynomialRoots.toString() else ""
        }

        //конвертация строки к корням полинома
        @TypeConverter
        @JvmStatic
        fun toPolynomialRoots(polynomialRoots : String) : PolynomialRoots?
        {
            return if(polynomialRoots != "")PolynomialRoots(polynomialRoots) else null
        }

    }
}