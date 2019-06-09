package com.dev.smurf.highmathcalculator.mvp.models

import com.example.smurf.mtarixcalc.Matrix
import com.example.smurf.mtarixcalc.matrixGroup

class MatrixModel
{
    fun createMatrix(obj : String) : Matrix
    {
        var ret = Matrix(obj)
        return ret
    }

    fun plus(left : Matrix , right : Matrix) : matrixGroup
    {
        var res = left + right
        var obj = matrixGroup(leftMatrix = left , rightMatrix = right , resMatrix = res , sign = "+")
        return obj
    }

    fun minus(left : Matrix , right : Matrix) :matrixGroup
    {
        var res = left - right
        var obj = matrixGroup(leftMatrix = left , rightMatrix = right , resMatrix = res , sign = "-")
        return obj
    }

    fun determinant(left : Matrix) : matrixGroup
    {
        var res = left.determinant()
        var obj = matrixGroup(leftMatrix = left , rightMatrix = Matrix() , resMatrix = Matrix(1,res) , sign = "det")
        return obj
    }

    fun times(left : Matrix , right: Matrix) : matrixGroup
    {
        var res = left * right
        var obj = matrixGroup(leftMatrix = left , rightMatrix = right , resMatrix = res , sign = "*")
        return obj
    }

    fun invers(left : Matrix) : matrixGroup
    {
        var res = left.invers()
        var obj = matrixGroup(leftMatrix = left , rightMatrix = Matrix() , resMatrix = res , sign = "inv")
        return obj
    }


}