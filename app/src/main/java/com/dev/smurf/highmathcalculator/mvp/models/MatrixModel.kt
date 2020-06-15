package com.dev.smurf.highmathcalculator.mvp.models

import com.dev.smurf.highmathcalculator.Matrix.Matrix
import com.dev.smurf.highmathcalculator.Numbers.Fraction
import com.dev.smurf.highmathcalculator.ui.POJO.MatrixGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


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
    private fun plus(left: Matrix, right: Matrix): MatrixGroup
    {
        val res = left + right
        val obj = MatrixGroup(leftMatrix = left, rightMatrix = right, resMatrix = res, sign = MatrixGroup.PLUS)
        return obj
    }

    //разница двух матриц
    private fun minus(left: Matrix, right: Matrix): MatrixGroup
    {
        val res = left - right
        val obj = MatrixGroup(leftMatrix = left, rightMatrix = right, resMatrix = res, sign = MatrixGroup.MINUS)
        return obj
    }

    private fun solve(matrix: Matrix): MatrixGroup
    {
        val res = matrix.solve()
        return MatrixGroup(
            leftMatrix = matrix,
            rightMatrix = Matrix.EmptyMatrix,
            resMatrix = res,
            sign = MatrixGroup.SOLVE
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
            sign = MatrixGroup.DET
        )
        return obj
    }


    //умножение матриц
    private fun times(left: Matrix, right: Matrix): MatrixGroup
    {
        val res = left * right
        val obj = MatrixGroup(leftMatrix = left, rightMatrix = right, resMatrix = res, sign = MatrixGroup.TIMES)
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
            sign = MatrixGroup.INV
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
            sign = MatrixGroup.RANK
        )
        return obj
    }

    private fun positive(leftMatrix: Matrix) : MatrixGroup
    {
        val res = leftMatrix.eigenValue()
        res.matrices.map { s -> if(s[0].re <= Fraction() ) return MatrixGroup(
            leftMatrix = leftMatrix,
            rightMatrix = Matrix.EmptyMatrix,
            resMatrix = Matrix.EmptyMatrix,
            sign = MatrixGroup.POSITIVE+" False"
        ) }
        val obj = MatrixGroup(
            leftMatrix = leftMatrix,
            rightMatrix = Matrix.EmptyMatrix,
            resMatrix = Matrix.EmptyMatrix,
            sign = MatrixGroup.POSITIVE+" True"
        )
        return obj
    }
    private fun negative(leftMatrix: Matrix) : MatrixGroup
    {
        val res = leftMatrix.eigenValue()
        if((res[0,0]*res[1,0]).re >= Fraction()) return MatrixGroup(
            leftMatrix = leftMatrix,
            rightMatrix = Matrix.EmptyMatrix,
            resMatrix = Matrix.EmptyMatrix,
            sign = MatrixGroup.NEGATIVE+" True"
        )
        val obj = MatrixGroup(
            leftMatrix = leftMatrix,
            rightMatrix = Matrix.EmptyMatrix,
            resMatrix = Matrix.EmptyMatrix,
            sign = MatrixGroup.NEGATIVE+" False"
        )
        return obj
    }

    private fun eigenVectors(leftMatrix: Matrix) : MatrixGroup
    {
        val res = leftMatrix.eigenVectors()
        val obj = MatrixGroup(
            leftMatrix = leftMatrix,
            rightMatrix = Matrix.EmptyMatrix,
            resMatrix = res,
            sign = MatrixGroup.EIGENVECTOR
        )
        return obj
    }

    private fun eigenValue(leftMatrix: Matrix) : MatrixGroup
    {
        val res = leftMatrix.eigenValue()
        val obj = MatrixGroup(
            leftMatrix = leftMatrix,
            rightMatrix = Matrix.EmptyMatrix,
            resMatrix = res,
            sign = MatrixGroup.EIGENVELUE
        )
        return obj
    }

    suspend fun MatrixPositive(
        scope: CoroutineScope,
        leftMatrix: String
    ): MatrixGroup
    {
        return withContext(scope.coroutineContext + Dispatchers.Default) {
            positive(createMatrix(leftMatrix))
        }
    }

    suspend fun MatrixNegative(
        scope: CoroutineScope,
        leftMatrix: String
    ): MatrixGroup
    {
        return withContext(scope.coroutineContext + Dispatchers.Default) {
            negative(createMatrix(leftMatrix))
        }
    }

    suspend fun MatrixEigenVector(
        scope: CoroutineScope,
        leftMatrix: String
    ): MatrixGroup
    {
        return withContext(scope.coroutineContext + Dispatchers.Default) {
            eigenVectors(createMatrix(leftMatrix))
        }
    }

    suspend fun MatrixEigenValue(
        scope: CoroutineScope,
        leftMatrix: String
    ): MatrixGroup
    {
        return withContext(scope.coroutineContext + Dispatchers.Default) {
            eigenValue(createMatrix(leftMatrix))
        }
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