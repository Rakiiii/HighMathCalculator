package com.example.smurf.mtarixcalc

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.dev.smurf.highmathcalculator.RoomConverters.POJOСonverter
import java.util.*

@Entity()
@TypeConverters(POJOСonverter::class)
data class MatrixGroup(
    var leftMatrix : Matrix,
    var rightMatrix : Matrix,
    var sign : String,
    var resMatrix : Matrix,
    @PrimaryKey
    var time: java.util.GregorianCalendar = GregorianCalendar()
)
{}