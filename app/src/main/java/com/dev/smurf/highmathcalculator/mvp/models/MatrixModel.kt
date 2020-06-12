package com.dev.smurf.highmathcalculator.mvp.models

import com.dev.smurf.highmathcalculator.Exceptions.TimeableException
import com.dev.smurf.highmathcalculator.Matrix.Matrix
import com.dev.smurf.highmathcalculator.ui.POJO.MatrixGroup
import com.dev.smurf.highmathcalculator.withTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.*


//класс работы с матрицами
class MatrixModel
{
    //инициализация матрицы
    private fun createMatrix(obj: String): Matrix
    {
        val ret = Matrix.createMatrix(obj)
        return ret
    }


    //сложение двух матриц
    private fun plus(left: Matrix, right: Matrix): MatrixGroup
    {
        val res = left + right
        val obj = MatrixGroup(leftMatrix = left, rightMatrix = right, resMatrix = res, sign = "+")
        return obj
    }

    //разница двух матриц
    private fun minus(left: Matrix, right: Matrix): MatrixGroup
    {
        val res = left - right
        val obj = MatrixGroup(leftMatrix = left, rightMatrix = right, resMatrix = res, sign = "-")
        return obj
    }

    private fun solve(matrix: Matrix): MatrixGroup
    {
        val res = matrix.solve()
        return MatrixGroup(
            leftMatrix = matrix,
            rightMatrix = Matrix.EmptyMatrix,
            resMatrix = res,
            sign = "solve"
        )
    }


    //определитель матрицы
    private fun determinant(left: Matrix): MatrixGroup
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
    private fun times(left: Matrix, right: Matrix): MatrixGroup
    {
        val res = left * right
        val obj = MatrixGroup(leftMatrix = left, rightMatrix = right, resMatrix = res, sign = "*")
        return obj
    }


    //инвертированний матриц
    private fun inverse(left: Matrix): MatrixGroup
    {
        val res = left.inversByGauseMethod()
        val obj = MatrixGroup(
            leftMatrix = left,
            rightMatrix = Matrix.EmptyMatrix,
            resMatrix = res,
            sign = "inv"
        )
        return obj
    }

    private fun rank(leftMatrix: Matrix) : MatrixGroup
    {
        val res = leftMatrix.rank()
        val obj = MatrixGroup(
            leftMatrix = leftMatrix,
            rightMatrix = Matrix.EmptyMatrix,
            resMatrix = res,
            sign = "inv"
        )
        return obj
    }

    suspend fun MatrixRank(
        scope: CoroutineScope,
        leftMatrix: String
    ): MatrixGroup
    {
        return withContext(scope.coroutineContext + Dispatchers.Default) {
            rank(createMatrix(leftMatrix))
        }
    }


    suspend fun MatrixPlus(
        scope: CoroutineScope,
        leftMatrix: String,
        rightMatrix: String
    ): MatrixGroup
    {
        return withContext(scope.coroutineContext + Dispatchers.Default) {
            plus(createMatrix(leftMatrix), createMatrix(rightMatrix))
        }
    }

    suspend fun MatrixMinus(
        scope: CoroutineScope,
        leftMatrix: String,
        rightMatrix: String
    ): MatrixGroup
    {
        return withContext(scope.coroutineContext + Dispatchers.Default) {
            minus(createMatrix(leftMatrix), createMatrix(rightMatrix))
        }
    }

    suspend fun MatrixTimes(
        scope: CoroutineScope,
        leftMatrix: String,
        rightMatrix: String
    ): MatrixGroup
    {
        return withContext(scope.coroutineContext + Dispatchers.Default) {
            times(createMatrix(leftMatrix), createMatrix(rightMatrix))
        }
    }

    suspend fun MatrixDeterminant(scope: CoroutineScope, leftMatrix: String): MatrixGroup
    {
        return withContext(scope.coroutineContext + Dispatchers.Default) {
            determinant(createMatrix(leftMatrix))
        }
    }

    suspend fun MatrixInverse(scope: CoroutineScope, leftMatrix: String): MatrixGroup
    {
        return withContext(scope.coroutineContext + Dispatchers.Default) {
            inverse(createMatrix(leftMatrix))
        }
    }

    suspend fun MatrixSolve(
        scope: CoroutineScope,
        leftMatrix: String
    ): MatrixGroup
    {
        return withContext(scope.coroutineContext + Dispatchers.Default) {
            solve(createMatrix(leftMatrix))
        }
    }



}