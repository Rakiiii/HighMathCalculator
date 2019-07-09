package com.dev.smurf.highmathcalculator.RoomConverters

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class TimeConverter
{

    @TypeConverter
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
}