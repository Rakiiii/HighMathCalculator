package com.dev.smurf.highmathcalculator.mvp.models

import com.dev.smurf.highmathcalculator.Matrix.Matrix
import com.dev.smurf.highmathcalculator.ui.POJO.MatrixGroup


//класс работы с матрицами
class MatrixModel
{
    //инициализация матрицы
    fun createMatrix(obj: String): Matrix
    {
        val ret = Matrix.createMatrix(obj)
        return ret
    }


    //сложение двух матриц
    fun plus(left: Matrix, right: Matrix): MatrixGroup
    {
        val res = left + right
        val obj = MatrixGroup(leftMatrix = left, rightMatrix = right, resMatrix = res, sign = "+")
        return obj
    }

    //разница двух матриц
    fun minus(left: Matrix, right: Matrix): MatrixGroup
    {
        val res = left - right
        val obj = MatrixGroup(leftMatrix = left, rightMatrix = right, resMatrix = res, sign = "-")
        return obj
    }


    //определитель матрицы
    fun determinant(left: Matrix): MatrixGroup
    {
        val res = left.determinant()
        val obj = MatrixGroup(
            leftMatrix = left,
            rightMatrix = Matrix.EmptyMatrix,
            resMatrix = Matrix.createMatrixAsNumber(res),
            sign = "det"
        )
        return obj
    }


    //умножение матриц
    fun times(left: Matrix, right: Matrix): MatrixGroup
    {
        val res = left * right
        val obj = MatrixGroup(leftMatrix = left, rightMatrix = right, resMatrix = res, sign = "*")
        return obj
    }


    //инвертированний матриц
    fun inverse(left: Matrix): MatrixGroup
    {
        val res = left.invers()
        val obj = MatrixGroup(
            leftMatrix = left,
            rightMatrix = Matrix.EmptyMatrix,
            resMatrix = res,
            sign = "inv"
        )
        return obj
    }


}