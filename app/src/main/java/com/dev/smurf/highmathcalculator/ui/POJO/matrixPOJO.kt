package com.dev.smurf.highmathcalculator.ui.POJO

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.dev.smurf.highmathcalculator.Matrix.Matrix
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
        val RANK = "rank"
        val EIGENVECTOR = "eigenvec"
        val EIGENVELUE = "eigenvalue"
        val POSITIVE = "is positive"
        val NEGATIVE = "is negative"
        val SOLVE = "solve"
        val CALCULATION="calculation"
        val LOADING = "loading"
    }

    fun Copy() : MatrixGroup
    {
        return MatrixGroup(
            leftMatrix = leftMatrix,
            rightMatrix = rightMatrix,
            sign = sign,
            resMatrix = resMatrix,
            time = time
        )
    }
}