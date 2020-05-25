package com.dev.smurf.highmathcalculator.Matrix

import com.dev.smurf.highmathcalculator.Exceptions.*
import com.dev.smurf.highmathcalculator.Exceptions.MatrixSerializeExceptions.DifferentAmountOfElementsInMatrixLineException
import com.dev.smurf.highmathcalculator.Exceptions.MatrixSerializeExceptions.WrongElemntAtMatrixInputException
import com.dev.smurf.highmathcalculator.Exceptions.MatrixSerializeExceptions.WrongSymbolAtMatrixInputException
import com.dev.smurf.highmathcalculator.Numbers.ComplexNumber
import com.dev.smurf.highmathcalculator.Numbers.Fraction
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

        fun createDiagonlMatrix(size: Int, elem: ComplexNumber): Matrix
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
            if (str == "")return
            val matrixString =
                str.filterNot { s ->
                    (s in '0'..'9') || (s == '\n') || (s == ' ') || (s == '(')
                            || (s == ')') || (s == 'i') || (s == '+') || (s == '-') || (s == '/')
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
                    if (element.fulfilCofs().isNotComplexNumber()) throw WrongElemntAtMatrixInputException(
                        str,
                        element
                    )
                }
            }
        }

        fun createMatrix(str: String): Matrix
        {
            isMatrix(str)
            if(str == "")return EmptyMatrix



            val lines = str.trim { s -> s == ' ' || s == '\n' }.fields("\n")
            val matrixHeight = lines.size
            val matrxWidth  = lines[0].trim { s -> s == ' ' || s == '\n' }.fields(" ").size

            val  matrix= Array(matrixHeight){arrayOf<ComplexNumber>() }
                for(line in lines.indices)
            {
                val elements = lines[line].trim { s -> s == ' ' || s == '\n' }.fields(" ")
                val nextMatrixLine = Array<ComplexNumber>(matrxWidth){ComplexNumber()}
                for(element in elements.indices)
                {
                    nextMatrixLine[element] = elements[element].fulfilCofs().toComplexNumber()
                }
                matrix[line] = nextMatrixLine
            }


            return Matrix(width = matrxWidth,height = matrixHeight, m = matrix)
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

    //функция подсчета определителя
    fun determinant(): ComplexNumber
    {
        //если матрица не квадратная кидаем ошибку
        if (width == height && width > 0)
        {
            //если матрица 2х2 то считаем по формуле
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
    private fun plusLines(firstLine: Int, secondLine: Int, cof: ComplexNumber)
    {
        for (i in 0 until width)
        {
            matrices[firstLine][i] += matrices[secondLine][i] * cof
        }
    }

    //вычитание строк с коэффицентом по элементно
    private fun minusLines(firstLine: Int, secondLine: Int, cof: ComplexNumber)
    {
        for (i in 0 until width)
        {
            matrices[firstLine][i] -= matrices[secondLine][i] * cof
        }
    }

    //деление строки на число
    private fun dividLine(line: Int, cof: ComplexNumber)
    {
        for (i in 0 until width)
            matrices[line][i] /= cof
    }


    //умножение матрицы на число
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
        val res = createDiagonlMatrix(width - 1, ComplexNumber())

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
        val res = createDiagonlMatrix(width, ComplexNumber())

        for (i in 0 until width)
        {
            for (j in 0 until width)
            {
                res.matrices[i][j] =
                    ComplexNumber(Fraction((-1).pow(i + j)), Fraction()) * this.minor(
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