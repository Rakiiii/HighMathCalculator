package com.dev.smurf.highmathcalculator.Matrix

import android.util.Log
import com.dev.smurf.highmathcalculator.Exceptions.*
import com.dev.smurf.highmathcalculator.Exceptions.MatrixSerializeExceptions.DifferentAmountOfElementsInMatrixLineException
import com.dev.smurf.highmathcalculator.Exceptions.MatrixSerializeExceptions.WrongAmountOfBracketsInMatrixException
import com.dev.smurf.highmathcalculator.Exceptions.MatrixSerializeExceptions.WrongElemntAtMatrixInputException
import com.dev.smurf.highmathcalculator.Exceptions.MatrixSerializeExceptions.WrongSymbolAtMatrixInputException
import com.dev.smurf.highmathcalculator.Exceptions.PolynomialSerializeExceptions.DifferentMatrixHeight
import com.dev.smurf.highmathcalculator.Exceptions.PolynomialSerializeExceptions.DifferentMatrixWidth
import com.dev.smurf.highmathcalculator.Numbers.ComplexNumber
import com.dev.smurf.highmathcalculator.Numbers.Fraction
import com.dev.smurf.highmathcalculator.Polynomials.ExponentialPolynomial
import com.dev.smurf.highmathcalculator.StringsExtension.*

//реализация работы с матрицей
open class Matrix private constructor(
    val width: Int,
    val height: Int,
    m: Array<Array<ComplexNumber>>
)
{

    object EmptyMatrix : Matrix(0, 0, Array(0) { Array(0) { ComplexNumber() } })

    //матрца
    var matrices: Array<Array<ComplexNumber>>
        private set

    init
    {
        matrices = m
    }


    companion object
    {
        @Deprecated(message = "createMatrix must be used instead, for normal input format control")
        fun createMatrixx(txt: String): Matrix
        {
            if (txt == "") return EmptyMatrix
            val matrixWidth = txt.substringBefore('\n').trim().countWords()

            val matrixHeight = txt.countLines()

            val matrices = Array(matrixHeight) { Array(matrixWidth) { ComplexNumber() } }
            //запоминаем содержимое поля ввода
            var info: String = txt.filterNot { s -> (s == '|') }

            if (matrixWidth == 1 && matrixHeight == 1) matrices[0][0] =
                info.trim().toComplexNumber()
            else
            {
                //проходим все строки матрицы
                for (i in 0 until matrixHeight)
                {
                    //запоминаем текущую строку
                    var subLine = info.substringBefore('\n').trim()

                    //удаляем текущую строку из скопированного текста
                    info = info.substringAfter('\n')


                    //проверяем полная ли матрица
                    if (matrixWidth != subLine.countWords())
                    //если не полна кидаем ощибку
                        throw DifferentAmountOfElementsInMatrixLineException(
                            input = txt,
                            unrecognizedPart = subLine
                        )

                    //проходим все столбцы
                    for (j in 0 until matrixWidth)
                    {
                        //запоминаем текущий столбец
                        val subWord = subLine.substringBefore(' ').trim()

                        //удаляем текущий столбец из текущей строки
                        subLine = subLine.substringAfter(' ').trim()

                        //добовляем элемент в матрицу
                        matrices[i][j] = subWord.toComplexNumber()
                    }
                }
            }
            return Matrix(width = matrixWidth, height = matrixHeight, m = matrices)
        }

        fun createDiagonalMatrix(size: Int, elem: ComplexNumber): Matrix
        {
            val matrixWidth = size
            val matrixHeight = size
            val matrices = Array(matrixHeight) { Array(matrixWidth) { ComplexNumber() } }

            for (i in 0 until size) matrices[i][i] = elem.Copy()

            return Matrix(width = matrixWidth, height = matrixHeight, m = matrices)
        }

        fun createMatrixCopy(m: Matrix): Matrix
        {
            return Matrix(m)
        }

        fun createMatrixAsNumber(number: ComplexNumber) =
            Matrix(1, 1, Array(1) { Array(1) { number } })

        fun isMatrix(str: String)
        {
            if (str == "") return

            val amountOfLeftBrackets = str.count { s -> s == '(' }
            val amountOfRightBrackets = str.count { s -> s == ')' }
            if (amountOfLeftBrackets != amountOfRightBrackets) throw WrongAmountOfBracketsInMatrixException(
                str,
                if (amountOfLeftBrackets > amountOfRightBrackets) "(" else ")"
            )

            val matrixString =
                str.filterNot { s ->
                    (s in '0'..'9') || (s == '\n') || (s == ' ') || (s == '(')
                            || (s == ')') || (s == 'i') || (s == '+')
                            || (s == '-') || (s == '/') || (s == '.')
                }
            if (matrixString != "") throw WrongSymbolAtMatrixInputException(str, matrixString)

            val lines = str.trim { s -> s == ' ' || s == '\n' }.fields("\n")

            var lineLength = -1

            for (line in lines)
            {
                val elements = line.trim { s -> s == ' ' || s == '\n' }.fields(" ")
                if (lineLength == -1) lineLength = elements.size
                if (lineLength != -1 && elements.size != lineLength) throw DifferentAmountOfElementsInMatrixLineException(
                    str,
                    line
                )

                for (element in elements)
                {
                    if (element.fulfilCofs()
                            .isNotComplexNumber()
                    ) throw WrongElemntAtMatrixInputException(
                        str,
                        element
                    )
                }
            }
        }

        fun createMatrix(str: String): Matrix
        {
            isMatrix(str)
            if (str == "") return EmptyMatrix


            val lines = str.trim { s -> s == ' ' || s == '\n' }.fields("\n")
            val matrixHeight = lines.size
            val matrxWidth = lines[0].trim { s -> s == ' ' || s == '\n' }.fields(" ").size

            val matrix = Array(matrixHeight) { arrayOf<ComplexNumber>() }
            for (line in lines.indices)
            {
                val elements = lines[line].trim { s -> s == ' ' || s == '\n' }.fields(" ")
                val nextMatrixLine = Array<ComplexNumber>(matrxWidth) { ComplexNumber() }
                for (element in elements.indices)
                {
                    nextMatrixLine[element] = elements[element].fulfilCofs().toComplexNumber()
                }
                matrix[line] = nextMatrixLine
            }


            return Matrix(width = matrxWidth, height = matrixHeight, m = matrix)
        }

    }

    private constructor() : this(2, 2, Array(2) { Array(2) { ComplexNumber() } })

    //конструктор копированния
    private constructor(m: Matrix) : this(
        m.width,
        m.height,
        Array(m.width) { Array(m.height) { ComplexNumber() } })
    {
        //копируем матрицу
        for (i in 0 until height)
        {
            for (j in 0 until width)
            {
                matrices[i][j] = m.matrices[i][j]
            }
        }
    }


    operator fun get(i: Int, j: Int) = matrices[i][j]
    operator fun get(i: Int) = matrices[i]

    fun columnIndices() = (0 until width)
    fun rowIndices() = (0 until height)

    fun Number() = this[0, 0]

    //функция подсчета определителя
    fun determinant(): ComplexNumber
    {
        //если матрица не квадратная кидаем ошибку
        if (width == height && width > 0)
        {
            //если матрица 2х2 то считаем по формуле
            Log.d(
                "matrix@",
                "first ${(matrices[0][0] * matrices[1][1])} second ${matrices[0][1] * matrices[1][0]}"
            )
            if (width == 2) return matrices[0][0] * matrices[1][1] - matrices[0][1] * matrices[1][0]
            else
            {
                //если не 2х2 то раскладываем по первой строке
                var res = ComplexNumber()

                for (i in 0 until width)
                {

                    //если сумма номеров строка:столбец четная прибавляем
                    if (i % 2 == 0) res += matrices[0][i] * this.minor(string = 0, columne = i)
                        .determinant()

                    //если нечестная , то вычитаем

                    else res += matrices[0][i] * this.minor(string = 0, columne = i)
                        .determinant() * -1
                }
                return res
            }
        }
        else throw MatrixNotSquareException()
    }


    //пергрузка опратора +
    operator fun plus(secMatrix: Matrix): Matrix
    {
        //если разного размера матрицы то кидаем исключение
        if (this.width != secMatrix.width || this.height != secMatrix.height) throw WrongMatrixSizeException()

        //копируем матрицу
        val resMatrix = Matrix(this)

        //прибаляем к скопированной матрице вторую матрицу по элементно
        for (i in 0 until height)
        {
            for (j in 0 until width) resMatrix.matrices[i][j] += secMatrix.matrices[i][j]
        }
        return resMatrix
    }

    //пергрузка опратора -
    operator fun minus(secMatrix: Matrix): Matrix
    {
        //если разного размера матрицы то кидаем исключение
        if (this.width != secMatrix.width || this.height != secMatrix.height) throw WrongMatrixSizeException()

        //копируем матрицу
        val resMatrix = Matrix(this)

        //поэлементно вычетаем элементы второй матрицы из первой
        for (i in 0 until height)
        {
            for (j in 0 until width) resMatrix.matrices[i][j] -= secMatrix.matrices[i][j]
        }
        return resMatrix
    }

    //перегрузка опреатора *
    operator fun times(secMatrix: Matrix): Matrix
    {
        //если одна из матриц первого порядка
        if (isNumber() || secMatrix.isNumber())
        {
            val res: Matrix
            val factor: ComplexNumber
            if (isNumber())
            {
                res = Matrix(secMatrix)
                factor = this.matrices[0][0]
            }
            else
            {
                res = Matrix(this)
                factor = secMatrix.matrices[0][0]
            }

            for (i in 0 until res.height)
            {
                for (j in 0 until res.width)
                {
                    res.matrices[i][j] = res.matrices[i][j] * factor
                }
            }

            return res
        }

        //сравниваем длинну первой матрицы с выостой второй если не равны кидаем исключение
        if (this.width != secMatrix.height) throw  MatrixIsNotTimeableException()

        //создаем результируюшую матрицу
        val res = Matrix(
            width = secMatrix.width,
            height = this.height,
            m = Array(this.height) { Array(secMatrix.width) { ComplexNumber() } })

        //умножаем матрицы
        for (i in 0 until res.height)
        {
            for (j in 0 until res.width)
            {
                for (k in 0 until width)
                {
                    res.matrices[i][j] += this.matrices[i][k] * secMatrix.matrices[k][j]
                }
            }
        }

        return res
    }


    //сложение строки по элементно с коэфицентом
    @Deprecated("Use MatrixLine.plus instead")
    private fun plusLines(firstLine: Int, secondLine: Int, cof: ComplexNumber)
    {
        for (i in 0 until width)
        {
            matrices[firstLine][i] += matrices[secondLine][i] * cof
        }
    }

    //вычитание строк с коэффицентом по элементно
    @Deprecated("Use MatrixLine.minus instead")
    private fun minusLines(firstLine: Int, secondLine: Int, cof: ComplexNumber)
    {
        for (i in 0 until width)
        {
            matrices[firstLine][i] -= matrices[secondLine][i] * cof
        }
    }

    //деление строки на число
    @Deprecated("Use MatrixLine.div instead")
    private fun dividLine(line: Int, cof: ComplexNumber)
    {
        for (i in 0 until width)
            matrices[line][i] /= cof
    }


    //умножение матрицы на число
    @Deprecated("Use MatrixLine.times instead")
    fun matrixTimesNumber(number: ComplexNumber): Matrix
    {
        //копируем матрицу
        val res = Matrix(this)

        //поэлементно умнажаем матрицу на число
        for (i in 0 until height)
        {
            for (j in 0 until width)
            {
                res.matrices[i][j] *= number
            }
        }
        return res
    }


    //деление матрицы на число
    fun matrixDivideByNumber(number: ComplexNumber): Matrix
    {
        val res = Matrix(this)
        for (i in 0 until height)
        {
            for (j in 0 until width)
            {
                res.matrices[i][j] /= number
            }
        }
        return res
    }


    //транспонированние матрицы
    fun trans(): Matrix
    {
        val res = Matrix(
            width = height,
            height = width,
            m = Array(width) { Array(height) { ComplexNumber() } })
        for (i in 0 until res.height)
        {
            for (j in 0 until res.width)
            {
                res.matrices[i][j] = matrices[j][i]
            }
        }
        return res
    }


    //получение минора матрицы за вычилом строки string и столбца colume
    fun minor(columne: Int, string: Int): Matrix
    {

        //создаем нулевую матрицу
        val res = createDiagonalMatrix(width - 1, ComplexNumber())

        //два счетчика для движение по минору матрицы
        var c = 0
        var s = 0

        for (i in 0 until width)
        {
            for (j in 0 until width)
            {
                if (i != string && j != columne)
                {
                    //если счетчик не принадлежит ни указанной строке ,ни столбцу
                    //добавляем в минор
                    res.matrices[c][s] = this.matrices[i][j]
                    s++
                }

            }
            if (i != string)
            {
                //по заполнению строки обнуляем счетчик
                s = 0
                c++
            }
        }
        return res
    }


    //матрица алгеброических дополнений
    fun minorMatrix(): Matrix
    {
        if (width != height) throw MatrixNotSquareException()
        if (this.determinant() == ComplexNumber()) throw ZeroDeterminantException()

        //если 2х2 то по формуле
        if (width == 2)
        {
            val res = Matrix(width, width, Array(width) { Array(width) { ComplexNumber() } })


            //считаем по формуле
            res.matrices[0][0] = matrices[1][1]
            res.matrices[0][1] = matrices[1][0] * -1
            res.matrices[1][1] = matrices[0][0]
            res.matrices[1][0] = matrices[0][1] * -1

            return res
        }

        //если больше чем три на три по заполняем матрицу определителями миноров
        val res = createDiagonalMatrix(width, ComplexNumber())

        for (i in 0 until width)
        {
            for (j in 0 until width)
            {
                res.matrices[i][j] =
                    ComplexNumber(Fraction((-1L).pow((i + j).toLong())), Fraction()) * this.minor(
                        string = i,
                        columne = j
                    ).determinant()
            }
        }
        return res
    }

    //поиск обратной матрицы через определитель и матрицу алгеброических дополнений
    fun invers(): Matrix
    {
        if (width != height) throw MatrixNotSquareException()
        if (this.determinant() == ComplexNumber()) throw ZeroDeterminantException()
        return this.minorMatrix().trans().matrixDivideByNumber(this.determinant())
    }

    fun extendedMatrix(): Matrix
    {
        if (width != height) throw MatrixNotSquareException()
        val extendedMatrixArray = Array(height) { i ->
            Array(width * 2) { j ->
                if (j < width) this[i, j] else if (j - width == i) ComplexNumber(1) else ComplexNumber()
            }
        }

        return Matrix(m = extendedMatrixArray, width = width * 2, height = height)
    }

    fun inversByGauseMethod(): Matrix
    {
        if (width != height) throw MatrixNotSquareException()
        if (this.determinant() == ComplexNumber()) throw ZeroDeterminantException()

        val gauseFrom = extendedMatrix().gauseMethod()

        val inversedMatrixArray =
            Array(height) { i -> Array(width) { j -> gauseFrom[i, j + width] } }
        return Matrix(width = width, height = height, m = inversedMatrixArray)
    }

    fun rank(): Matrix
    {
        var rank = 0
        val gauseFrom = gauseMethod()

        for (i in gauseFrom.rowIndices())
        {
            var zeroCounter = 0
            gauseFrom[i].map { if (it == ComplexNumber()) zeroCounter++ }

            if (zeroCounter != width) rank++
        }

        return Matrix(width = 1, height = 1, m = Array(1) { Array(1) { ComplexNumber(rank) } })
    }

    //перегрузка оператора сравнения
    override operator fun equals(other: Any?): Boolean
    {
        when (other)
        {
            is Matrix ->
            {
                if (this.height != other.height || this.width != other.width) return false
                for (i in 0 until height)
                {
                    for (j in 0 until width)
                        if (this.matrices[i][j] != other.matrices[i][j]) return false
                }
                return true
            }
            else -> return false
        }
    }

    //перегрузка приведения к строке
    override fun toString(): String
    {
        var res = ""

        for (i in 0 until height)
        {
            for (j in 0 until width)
            {
                if (j != width - 1)
                    res += matrices[i][j].toString() + ' '
                else res += matrices[i][j].toString()
            }
            if (i != height - 1)
            {
                res += '\n'
            }
        }

        return res
    }

    fun gauseMethod(): Matrix
    {
        val betterMatrixRepresentation = MatrixLine.createArrayOfMatrixLines(this)

        for (majorLine in betterMatrixRepresentation.indices)
        {
            betterMatrixRepresentation[majorLine] =
                betterMatrixRepresentation[majorLine] / (betterMatrixRepresentation[majorLine][majorLine])
            for (minorLine in betterMatrixRepresentation.indices)
            {
                if (majorLine != minorLine)
                {
                    betterMatrixRepresentation[minorLine] =
                        betterMatrixRepresentation[minorLine] -
                                (betterMatrixRepresentation[majorLine] * betterMatrixRepresentation[minorLine][majorLine])
                }
            }
        }

        val resultArray =
            Array(betterMatrixRepresentation.size) { i -> Array(betterMatrixRepresentation[i].length) { j -> betterMatrixRepresentation[i][j] } }
        return Matrix(width = width, height = height, m = resultArray)
    }


    //return fronbeus matrix getted by Danilevski Method
    fun getFronbeusMatrixByDanilevskiMethod(): Matrix
    {
        if (isNotSquare()) throw  MatrixNotSquareException()
        var subMatrix = Matrix.createMatrixCopy(this)
        for (i in (subMatrix.height - 1) downTo 1)
        {
            if (subMatrix[i][i - 1] == ComplexNumber())
            {
                var isNonZeroFounded = false
                for (k in (0 until i - 1))
                {
                    if (subMatrix[i][k] != ComplexNumber())
                    {
                        subMatrix = subMatrix.switchColumns(k, i - 1).switchRows(k, i - 1)
                        isNonZeroFounded = true
                        break
                    }
                }
                if (!isNonZeroFounded)
                {
                    val k = i - 1
                    val topLeftAngel =
                        subMatrix.getSubMatrix(Pair(0, 0), Pair(k, k))
                            .getFronbeusMatrixByDanilevskiMethod()
                    val rightAngel =
                        subMatrix.getSubMatrix(Pair(0, k + 1), Pair(k, subMatrix.width - 1))
                    val bottomLeftAngel = subMatrix.getSubMatrix(
                        Pair(k + 1, 0),
                        Pair(subMatrix.height - 1, k)
                    )
                    val bottomRightAngel = subMatrix.getSubMatrix(
                        Pair(k + 1, k + 1),
                        Pair(subMatrix.height - 1, subMatrix.width - 1)
                    )

                    return topLeftAngel.horizontalConcatenation(rightAngel).verticalConcatenation(
                        bottomLeftAngel.horizontalConcatenation(bottomRightAngel)
                    )
                }
            }
            val B = Matrix.createDiagonalMatrix(subMatrix.width, ComplexNumber(1))

            for (j in 0 until B.width)
            {
                if (j != i - 1) B.matrices[i - 1][j] =
                    ComplexNumber() - (subMatrix[i][j] / subMatrix[i][i - 1])
                else B.matrices[i - 1][j] = ComplexNumber(1) / subMatrix[i][i - 1]
            }

            val inversB = B.invers()

            subMatrix = subMatrix * B

            subMatrix = inversB * subMatrix
        }

        return subMatrix
    }

    fun getMoveToFronbeusMatrix(): Matrix
    {
        if (isNotSquare()) throw  MatrixNotSquareException()
        var subMatrix = Matrix.createMatrixCopy(this)
        var overAllB = createDiagonalMatrix(width, ComplexNumber(1))
        for (i in (subMatrix.height - 1) downTo 1)
        {
            if (subMatrix[i][i - 1] == ComplexNumber())
            {
                var isNonZeroFounded = false
                for (k in (0 until i - 1))
                {
                    if (subMatrix[i][k] != ComplexNumber())
                    {
                        subMatrix = subMatrix.switchColumns(k, i - 1).switchRows(k, i - 1)
                        isNonZeroFounded = true
                        break
                    }
                }
                if (!isNonZeroFounded)
                {
                    val k = i - 1
                    val topLeftAngel =
                        subMatrix.getSubMatrix(Pair(0, 0), Pair(k, k))
                            .getMoveToFronbeusMatrix()
                    val rightAngel =
                        createDiagonalMatrix(width, ComplexNumber(1)).getSubMatrix(
                            Pair(0, k + 1),
                            Pair(k, subMatrix.width - 1)
                        )
                    val bottomLeftAngel = createDiagonalMatrix(width, ComplexNumber()).getSubMatrix(
                        Pair(k + 1, 0),
                        Pair(subMatrix.height - 1, k)
                    )
                    val bottomRightAngel =
                        createDiagonalMatrix(width, ComplexNumber(1)).getSubMatrix(
                            Pair(k + 1, k + 1),
                            Pair(subMatrix.height - 1, subMatrix.width - 1)
                        )

                    return overAllB * topLeftAngel.horizontalConcatenation(rightAngel)
                        .verticalConcatenation(
                            bottomLeftAngel.horizontalConcatenation(bottomRightAngel))
                }
            }
            val B = Matrix.createDiagonalMatrix(subMatrix.width, ComplexNumber(1))

            for (j in 0 until B.width)
            {
                if (j != i - 1) B.matrices[i - 1][j] =
                    ComplexNumber() - (subMatrix[i][j] / subMatrix[i][i - 1])
                else B.matrices[i - 1][j] = ComplexNumber(1) / subMatrix[i][i - 1]
            }

            overAllB = overAllB * B

            val inversB = B.invers()

            subMatrix = subMatrix * B

            subMatrix = inversB * subMatrix
        }

        return overAllB
    }

    fun eigenValue() : Matrix
    {
        if(width != height)throw MatrixNotSquareException()
        if(width > 2) throw NonpermanentException("Matrix is too big for normal solving")
        val fronbeusMatrix = getFronbeusMatrixByDanilevskiMethod()

        val polString = "x^2+"+fronbeusMatrix[0,0].toString()+"x"+fronbeusMatrix[0,1].toString()

        val polynomail = ExponentialPolynomial.createExponentialPolynomial(polString)

        val roots = polynomail.solveInNumbers()

        val resultArray = Array(height){ p-> Array(1){roots[p]} }

        return Matrix(width=1,height = height,m = resultArray)
    }

    fun eigenVectors() : Matrix
    {
        val number = eigenValue()

        val resultArray = Array(height){ i -> Array(width){ j -> number[j,0].pow(i)} }

        return Matrix(width = width,height = height,m = resultArray)
    }

    private fun switchColumns(k: Int, m: Int): Matrix
    {
        val newMatrix = createMatrixCopy(this)
        for (i in newMatrix.rowIndices())
        {
            val tmp = newMatrix.matrices[i][k]
            newMatrix.matrices[i][k] = newMatrix.matrices[i][m]
            newMatrix.matrices[i][m] = tmp
        }

        return newMatrix
    }

    private fun switchRows(k: Int, m: Int): Matrix
    {
        val newMatrix = createMatrixCopy(this)
        for (i in newMatrix.columnIndices())
        {
            val tmp = newMatrix.matrices[k][i]
            newMatrix.matrices[k][i] = newMatrix.matrices[m][i]
            newMatrix.matrices[m][i] = tmp
        }

        return newMatrix
    }

    //first is vertical second is horizontal
    //start is first element , end is last element indeces
    private fun getSubMatrix(start: Pair<Int, Int>, end: Pair<Int, Int>): Matrix
    {
        if (start.first < 0 || start.second < 0 || end.first >= height || end.second >= width || end.second - start.second <= 0 || end.first - start.first <= 0) throw WrongDataException()
        val subArray =
            Array(end.first - start.first) { i -> Array(end.second - start.second) { j -> this[start.first + i][start.second + j] } }

        return Matrix(
            width = end.second - start.second,
            height = end.first - start.first,
            m = subArray
        )
    }

    fun horizontalConcatenation(matrix: Matrix): Matrix
    {
        if (height != matrix.height) throw DifferentMatrixHeight()
        val resultArray =
            Array(height) { i -> Array(width + matrix.width) { j -> if (j < width) this[i][j] else matrix[i][j - width] } }

        return Matrix(width = width + matrix.width, height = height, m = resultArray)
    }

    fun verticalConcatenation(matrix: Matrix): Matrix
    {
        if (width != matrix.width) throw DifferentMatrixWidth()
        val resultArray =
            Array(height + matrix.height) { i -> Array(width) { j -> if (i < height) this[i][j] else matrix[i - height][j] } }
        return Matrix(width = width, height = height + matrix.height, m = resultArray)
    }

    fun isSquare() = (width == height)
    fun isNotSquare() = !isSquare()

    fun solve(): Matrix
    {
        //todo::add solving non square matrix (multiple solutions) with better
        if (width - 1 != height) throw MatrixNotSquareException()
        val gauseForm = gauseMethod()

        val resultMatrix =
            Array(height) { pos -> Array(1) { gauseForm[pos].last() } }

        return Matrix(width = 1, height = height, m = resultMatrix)
    }


    fun toString(sym: Char = ' '): String
    {
        var res: String = sym.toString()

        for (i in 0 until height)
        {
            for (j in 0 until width)
            {
                if (j != width - 1)
                    res += matrices[i][j].toString() + ' '
                else res += matrices[i][j].toString()
            }
            if (i != height - 1)
            {
                res += sym; res += '\n';res += sym
            }
            else res += sym
        }

        return res
    }


    fun isEmpty(): Boolean
    {
        return width == 0 && height == 0
    }

    fun isNumber() = (width == 1 && height == 1)

}