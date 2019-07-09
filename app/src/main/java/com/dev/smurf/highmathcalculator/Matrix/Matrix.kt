package com.example.smurf.mtarixcalc

import android.widget.EditText

//реализация работы с матрицей
class Matrix( _width : Int = 2,
              _heigh : Int = 2
            )
{

    //ширина матрицы
    val width : Int = _width
    //высота матрицы
    val height : Int = _heigh

    //если матрица пустая то true
    private var _empty : Boolean = true

    //матрца
    var matrix : Array<Array<complexNumber>>
    private set



    //инициализация и первичное заполнение матрицы
    init
    {
        if(height == 0 || width == 0)throw Exception("MatrixSizeIs0x0")
        //инициализируем матрицу 0
        matrix  = Array<Array<complexNumber>>(height , { m -> Array<complexNumber>(width , { n -> complexNumber() }) })
    }


    //инициализация из строки
    constructor( txt : String) : this(
                                        _width = txt.substringBefore('\n').trim().countWords(),
                                        _heigh = txt.countLines()
                                     )
    {
        //запоминаем содержимое поля ввода
        var info : String = txt.filterNot { s -> (s == '|') }

        //проходим все строки матрицы
        for(i in 0 until height) {
            //запоминаем текущую строку
            var subLine = info.substringBefore('\n').trim()

            //удаляем текущую строку из скопированного текста
            info = info.substringAfter('\n')

            //проверяем полная ли матрица
            if (width != subLine.countWords())
            //если не полна кидаем ощибку
                throw Exception("amount of elemnts in line is different")

            //проходим все столбцы
            for (j in 0 until width) {
                //запоминаем текущий столбец
                val subWord = subLine.substringBefore(' ').trim()

                //удаляем текущий столбец из текущей строки
                subLine = subLine.substringAfter(' ').trim()

                //добовляем элемент в матрицу
                matrix[i][j] = subWord.toComplex()
            }
        }
        //матрица не пуста
        _empty = false
    }

    //иницаиализация из EditText view элемента
    constructor( txt : EditText): this(txt.text.toString())
    {}


    //инициализация диагональной матрцы размера size*size и elem по диагонали
    constructor(size : Int, elem : complexNumber) : this(size , size)
    {
        //заполняем матрицу
        for(i in 0 until size)matrix[i][i] = elem

        //матрице заполнена
        _empty = false
    }


    //конструктор копированния
    constructor(m : Matrix) : this(m.width , m.height)
    {
        //копируем матрицу
        for(i in 0 until height)
        {
            for(j in 0 until width)
            {
                matrix[i][j] = m.matrix[i][j]
            }
        }

        //матрица не пуста
        _empty = false
    }

    //функция подсчета определителя
    fun determinant() : complexNumber
    {
        //если матрица не квадратная кидаем ошибку
        if( width == height && width != 0)
        {
            //если матрица 2х2 то считаем по формуле
            if(width == 2) return matrix[0][0]*matrix[1][1] - matrix[0][1]*matrix[1][0]
            else
            {
                //если не 2х2 то раскладываем по первой строке
                var res = complexNumber()

                for(i in 0 until width)
                {

                    //если сумма номеров строка:столбец четная прибавляем
                    if( i % 2 == 0)res += matrix[0][i]*this.minor(string = 0 , colume = i).determinant()

                    //если нечестная , то вычитаем

                    else res += matrix[0][i]*this.minor(string = 0 , colume = i).determinant()*-1
                }
                return res
            }
        }
        else throw Exception("matrix is not square")
    }


    //пергрузка опратора +
    operator fun plus(secMatrix : Matrix): Matrix
    {
        //если разного размера матрицы то кидаем исключение
        if(this.width != secMatrix.width || this.height != secMatrix.height)throw Exception("Matrixes have different size")

        //копируем матрицу
        var resMatrix = Matrix(this)

        //прибаляем к скопированной матрице вторую матрицу по элементно
        for(i in 0 until height)
        {
            for(j in 0 until width)resMatrix.matrix[i][j] += secMatrix.matrix[i][j]
        }
        return resMatrix
    }

    //пергрузка опратора -
    operator fun minus(secMatrix : Matrix): Matrix
    {
        //если разного размера матрицы то кидаем исключение
        if(this.width != secMatrix.width || this.height != secMatrix.height)throw Exception("Matrixes have different size")

        //копируем матрицу
        var resMatrix = Matrix(this)

        //поэлементно вычетаем элементы второй матрицы из первой
        for(i in 0 until height)
        {
            for(j in 0 until width)resMatrix.matrix[i][j] -= secMatrix.matrix[i][j]
        }
        return resMatrix
    }

    //перегрузка опреатора *
    operator fun times(secMatrix: Matrix): Matrix
    {

        //сравниваем длинну первой матрицы с выостой второй если не равны кидаем исключение
        if(this.width != secMatrix.height)throw Exception("Line length isn't equals to column height")

        //создаем результируюшую матрицу
        var res  = Matrix(_width = secMatrix.width , _heigh = this.height)

        //умножаем матрицы
        for(i in 0 until res.height)
        {
            for(j in 0 until res.width)
            {
                for(k in 0 until width)
                {
                    res.matrix[i][j] += this.matrix[i][k]*secMatrix.matrix[k][j]
                }
            }
        }

        return res
    }


    //сложение строки по элементно с коэфицентом
    private fun plusLines(firstLine : Int , secondLine : Int , cof : complexNumber)
    {
        for(i in 0 until width)
        {
            matrix[firstLine][i] += matrix[secondLine][i]*cof
        }
    }

    //вычитание строк с коэффицентом по элементно
    private fun minusLines(firstLine : Int , secondLine : Int , cof : complexNumber)
    {
        for(i in 0 until width)
        {
            matrix[firstLine][i] -= matrix[secondLine][i]*cof
        }
    }

    //деление строки на число
    private fun dividLine(line : Int , cof : complexNumber)
    {
        for(i in 0 until width)
            matrix[line][i] /= cof
    }


    //умножение матрицы на число
    fun matrixTimesNumber(number : complexNumber):Matrix
    {
        //копируем матрицу
        var res = Matrix(this)

        //поэлементно умнажаем матрицу на число
        for(i in 0 until height)
        {
            for (j in 0 until width)
            {
                res.matrix[i][j] *= number
            }
        }
        return res
    }


    //деление матрицы на число
    fun matrixDivideByNumber(number : complexNumber):Matrix
    {
        var res = Matrix(this)
        for(i in 0 until height)
        {
            for (j in 0 until width)
            {
                res.matrix[i][j] /= number
            }
        }
        return res
    }


    //транспонированние матрицы
    fun trans() : Matrix
    {
        var res = Matrix(_width = height, _heigh =  width)
        for(i in 0 until res.height)
        {
            for(j in  0 until res.width)
            {
                res.matrix[i][j] = matrix[j][i]
            }
        }
        return res
    }



    //получение минора матрицы за вычилом строки string и столбца colume
    fun minor(colume : Int , string : Int) : Matrix
    {

        //создаем нулевую матрицу
        var res = Matrix(width - 1 , complexNumber())

        //два счетчика для движение по минору матрицы
        var C : Int = 0
        var S : Int = 0

        for(i in 0 until width)
        {
            for(j in 0 until width)
            {
                if (i != string && j != colume)
                {
                    //если счетчик не принадлежит ни указанной строке ,ни столбцу
                    //добавляем в минор
                    res.matrix[C][S] = this.matrix[i][j]
                    S++
                }

            }
            if(i != string)
            {
                //по заполнению строки обнуляем счетчик
                S = 0
                C++
            }
        }
        return res
    }


    //матрица алгеброических дополнений
    fun minorMatrix() : Matrix
    {
        if(width != height)throw Exception("Matrix isn't square")
        if(this.determinant() == complexNumber())throw Exception("Determinant is 0 ")

        //если 2х2 то по формуле
        if(width == 2)
        {
            var res = Matrix(width)


            //считаем по формуле
            res.matrix[0][0] = matrix[1][1]
            res.matrix[0][1] = matrix[1][0] * -1
            res.matrix[1][1] = matrix[0][0]
            res.matrix[1][0] = matrix[0][1] * -1

            return res
        }

        //если больше чем три на три по заполняем матрицу определителями миноров
        var res = Matrix(width , complexNumber())

        for(i in 0 until width)
        {
            for(j in 0 until width)
            {
              res.matrix[i][j] = complexNumber(fraction( (-1).pow(i+j) ) , fraction() ) * this.minor(string = i , colume = j).determinant()
            }
        }
        return res
    }

    //поиск обратной матрицы через определитель и матрицу алгеброических дополнений
    fun invers() : Matrix
    {
        if(width != height)throw Exception("Matrix isn't square")
        if(this.determinant() == complexNumber())throw Exception("Determinant is 0 ")
        return  this.minorMatrix().trans().matrixDivideByNumber(this.determinant())
    }

    //перегрузка оператора сравнения
    override operator fun equals(other : Any?) : Boolean
    {
        when(other)
        {
            is Matrix ->
            {
                if(this.height != other.height || this.width != other.width)return false
                for(i in 0 until height)
                {
                    for(j in 0 until width)
                        if(this.matrix[i][j] != other.matrix[i][j])return false
                }
                return true
            }
            else -> return false
        }
    }

    //перегрузка приведения к строке
    override fun toString(): String
    {
        var res : String = ""

        for( i in 0 until height)
        {
            for(j in 0 until width)
            {
                if(j != width - 1)
                    res += matrix[i][j].toString() + ' '
                else res +=matrix[i][j].toString()
            }
            if(i != height-1)
            {
                res += '\n'
            }
        }

        return res
    }


    fun toString(sym : Char = ' '): String
    {
        var res : String = sym.toString()

        for( i in 0 until height)
        {
            for(j in 0 until width)
            {
                if(j != width - 1)
                res += matrix[i][j].toString() + ' '
                else res +=matrix[i][j].toString()
            }
            if(i != height-1)
            {
                res += sym; res += '\n';res +=sym
            }
            else res+= sym
        }

        return res
    }


    fun isEmpty() : Boolean
    {
        return _empty
    }



   /* fun sumOfMainMinors( order : Int) : complexNumber
    {
        if( order >= width )throw Exception("MatrixTooSmallException")
        if(order == width)return this.determinant()
        var res = complexNumber(fraction(1,0))
        for(i in 0 until binCofs( width , order))
        {

        }
    }*/

    /*fun quadratinqRedaction() : Matrix
    {
        if(width != height)throw Exception("Matrix is not square")
        if(this.trans() != this)throw Exception("Matrix is not semetric")
        when(width)
        {
            3->
            {
                var inv1 = this.matrix
            }
            4->
            {

            }
            else -> throw Exception("Polinoms have to many params")
        }
    }*/
}