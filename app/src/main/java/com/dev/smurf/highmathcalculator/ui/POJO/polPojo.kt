package com.example.smurf.mtarixcalc

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.dev.smurf.highmathcalculator.Polinoms.PolinomBase
import com.dev.smurf.highmathcalculator.RoomConverters.MatrixConverter
import java.util.*

@Entity
@TypeConverters(MatrixConverter::class)
data class PolinomGroup(var polLeftPolinom : PolinomBase,
                        var polRightPolinom : PolinomBase?,
                        var polSignPolinom : String,
                        var polResPolinom : PolinomBase?,
                        var polOstPolinom : PolinomBase? = null,
                        var isRoots : Boolean = false,
                        var roots : String? = null,
                        @PrimaryKey var time : java.util.GregorianCalendar = GregorianCalendar()
)
