package com.example.smurf.mtarixcalc

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.dev.smurf.highmathcalculator.RoomConverters.POJOConverter
import java.util.*

@Entity()
@TypeConverters(POJOConverter::class)
data class MatrixGroup(
    var leftMatrix: Matrix,
    var rightMatrix: Matrix,
    var sign: String,
    var resMatrix: Matrix,
    @PrimaryKey
    var time: java.util.GregorianCalendar = GregorianCalendar()
)
{
    companion object
    {
        val DET = "det"
        val INV = "inv"
        val MINUS = "-"
        val PLUS = "+"
        val TIMES = "*"
    }
}