package com.dev.smurf.highmathcalculator.mvp.models

import com.example.smurf.mtarixcalc.Matrix
import com.example.smurf.mtarixcalc.MatrixGroup


//класс работы с матрицами
class MatrixModel
{
    //инициализация матрицы
    fun createMatrix(obj : String) : Matrix
    {
        var ret = Matrix(obj)
        return ret
    }


    //сложение двух матриц
    fun plus(left : Matrix , right : Matrix) : MatrixGroup
    {
        var res = left + right
        var obj = MatrixGroup(leftMatrix = left , rightMatrix = right , resMatrix = res , sign = "+")
        return obj
    }

    //разница двух матриц
    fun minus(left : Matrix , right : Matrix) :MatrixGroup
    {
        var res = left - right
        var obj = MatrixGroup(leftMatrix = left , rightMatrix = right , resMatrix = res , sign = "-")
        return obj
    }


    //определитель матрицы
    fun determinant(left : Matrix) : MatrixGroup
    {
        var res = left.determinant()
        var obj = MatrixGroup(leftMatrix = left , rightMatrix = Matrix() , resMatrix = Matrix(1,res) , sign = "det")
        return obj
    }


    //умножение матриц
    fun times(left : Matrix , right: Matrix) : MatrixGroup
    {
        var res = left * right
        var obj = MatrixGroup(leftMatrix = left , rightMatrix = right , resMatrix = res , sign = "*")
        return obj
    }


    //инвертированний матриц
    fun inverse(left : Matrix) : MatrixGroup
    {
        var res = left.invers()
        var obj = MatrixGroup(leftMatrix = left , rightMatrix = Matrix() , resMatrix = res , sign = "inv")
        return obj
    }


}