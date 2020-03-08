package com.example.smurf.mtarixcalc

import android.widget.EditText
import com.dev.smurf.highmathcalculator.Numbers.ComplexNumber
import com.dev.smurf.highmathcalculator.Numbers.Fraction
import com.dev.smurf.highmathcalculator.Utils.countLines
import com.dev.smurf.highmathcalculator.Utils.countWords
import com.dev.smurf.highmathcalculator.Utils.pow
import com.dev.smurf.highmathcalculator.Utils.toComplex

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
    var matrices : Array<Array<ComplexNumber>>
    private set



    //инициализация и первичное заполнение матрицы
    init
    {
        if(height == 0 || width == 0)throw Exception("MatrixSizeIs0x0")
        //инициализируем матрицу 0
        matrices  = Array<Array<ComplexNumber>>(height , { m -> Array<ComplexNumber>(width , { n -> ComplexNumber() }) })
    }


    //инициализация из строки
    constructor( txt : String) : this(
                                        _width = txt.substringBefore('\n').trim().countWords(),
                                        _heigh = txt.countLines()
                                     )
    {
        //запоминаем содержимое поля ввода
        var info : String = txt.filterNot { s -> (s == '|') }

        if(isNumber())matrices[0][0] = info.trim().toComplex()
        else
        {
        //проходим все строки матрицы
            for(i in 0 until height)
            {
                //запоминаем текущую строку
                var subLine = info.substringBefore('\n').trim()

                //удаляем текущую строку из скопированного текста
                info = info.substringAfter('\n')


                //проверяем полная ли матрица
                if (width != subLine.countWords())
                //если не полна кидаем ощибку
                    throw Exception("amount of elemnts in line is different")

                //проходим все столбцы
                for (j in 0 until width)
                {
                    //запоминаем текущий столбец
                    val subWord = subLine.substringBefore(' ').trim()

                    //удаляем текущий столбец из текущей строки
                    subLine = subLine.substringAfter(' ').trim()

                    //добовляем элемент в матрицу
                    matrices[i][j] = subWord.toComplex()
                }
            }
        }
        //матрица не пуста
        _empty = false
    }

    //иницаиализация из EditText view элемента
    constructor( txt : EditText): this(txt.text.toString())
    {}


    //инициализация диагональной матрцы размера size*size и elem по диагонали
    constructor(size : Int, elem : ComplexNumber) : this(size , size)
    {
        //заполняем матрицу
        for(i in 0 until size)matrices[i][i] = elem

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
                matrices[i][j] = m.matrices[i][j]
            }
        }

        //матрица не пуста
        _empty = false
    }

    //функция подсчета определителя
    fun determinant() : ComplexNumber
    {
        //если матрица не квадратная кидаем ошибку
        if( width == height && width != 0)
        {
            //если матрица 2х2 то считаем по формуле
            if(width == 2) return matrices[0][0]*matrices[1][1] - matrices[0][1]*matrices[1][0]
            else
            {
                //если не 2х2 то раскладываем по первой строке
                var res = ComplexNumber()

                for(i in 0 until width)
                {

                    //если сумма номеров строка:столбец четная прибавляем
                    if( i % 2 == 0)res += matrices[0][i]*this.minor(string = 0 , colume = i).determinant()

                    //если нечестная , то вычитаем

                    else res += matrices[0][i]*this.minor(string = 0 , colume = i).determinant()*-1
                }
                return res
            }
        }
        else throw Exception("matrices is not square")
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
            for(j in 0 until width)resMatrix.matrices[i][j] += secMatrix.matrices[i][j]
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
            for(j in 0 until width)resMatrix.matrices[i][j] -= secMatrix.matrices[i][j]
        }
        return resMatrix
    }

    //перегрузка опреатора *
    operator fun times(secMatrix: Matrix): Matrix
    {
        //если одна из матриц первого порядка
        if( isNumber() || secMatrix.isNumber() )
        {
            val res : Matrix
            val factor : ComplexNumber
            if(isNumber())
            {
                res = Matrix(secMatrix)
                factor = this.matrices[0][0]
            }
            else
            {
                res = Matrix(this)
                factor = secMatrix.matrices[0][0]
            }

            for(i in 0 until res.height)
            {
                for(j in 0 until res.width)
                {
                    res.matrices[i][j] = res.matrices[i][j] * factor
                }
            }

            return res
        }

        //сравниваем длинну первой матрицы с выостой второй если не равны кидаем исключение
        if(this.width != secMatrix.height)throw Exception("Line length isn't equals to column height")

        //создаем результируюшую матрицу
        val res  = Matrix(_width = secMatrix.width , _heigh = this.height)

        //умножаем матрицы
        for(i in 0 until res.height)
        {
            for(j in 0 until res.width)
            {
                for(k in 0 until width)
                {
                    res.matrices[i][j] += this.matrices[i][k]*secMatrix.matrices[k][j]
                }
            }
        }

        return res
    }


    //сложение строки по элементно с коэфицентом
    private fun plusLines(firstLine : Int , secondLine : Int , cof : ComplexNumber)
    {
        for(i in 0 until width)
        {
            matrices[firstLine][i] += matrices[secondLine][i]*cof
        }
    }

    //вычитание строк с коэффицентом по элементно
    private fun minusLines(firstLine : Int , secondLine : Int , cof : ComplexNumber)
    {
        for(i in 0 until width)
        {
            matrices[firstLine][i] -= matrices[secondLine][i]*cof
        }
    }

    //деление строки на число
    private fun dividLine(line : Int , cof : ComplexNumber)
    {
        for(i in 0 until width)
            matrices[line][i] /= cof
    }


    //умножение матрицы на число
    fun matrixTimesNumber(number : ComplexNumber):Matrix
    {
        //копируем матрицу
        var res = Matrix(this)

        //поэлементно умнажаем матрицу на число
        for(i in 0 until height)
        {
            for (j in 0 until width)
            {
                res.matrices[i][j] *= number
            }
        }
        return res
    }


    //деление матрицы на число
    fun matrixDivideByNumber(number : ComplexNumber):Matrix
    {
        var res = Matrix(this)
        for(i in 0 until height)
        {
            for (j in 0 until width)
            {
                res.matrices[i][j] /= number
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
                res.matrices[i][j] = matrices[j][i]
            }
        }
        return res
    }



    //получение минора матрицы за вычилом строки string и столбца colume
    fun minor(colume : Int , string : Int) : Matrix
    {

        //создаем нулевую матрицу
        var res = Matrix(width - 1 , ComplexNumber())

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
                    res.matrices[C][S] = this.matrices[i][j]
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
        if(this.determinant() == ComplexNumber())throw Exception("Determinant is 0 ")

        //если 2х2 то по формуле
        if(width == 2)
        {
            var res = Matrix(width)


            //считаем по формуле
            res.matrices[0][0] = matrices[1][1]
            res.matrices[0][1] = matrices[1][0] * -1
            res.matrices[1][1] = matrices[0][0]
            res.matrices[1][0] = matrices[0][1] * -1

            return res
        }

        //если больше чем три на три по заполняем матрицу определителями миноров
        var res = Matrix(width , ComplexNumber())

        for(i in 0 until width)
        {
            for(j in 0 until width)
            {
              res.matrices[i][j] = ComplexNumber(Fraction( (-1).pow(i+j) ) , Fraction() ) * this.minor(string = i , colume = j).determinant()
            }
        }
        return res
    }

    //поиск обратной матрицы через определитель и матрицу алгеброических дополнений
    fun invers() : Matrix
    {
        if(width != height)throw Exception("Matrix isn't square")
        if(this.determinant() == ComplexNumber())throw Exception("Determinant is 0 ")
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
                        if(this.matrices[i][j] != other.matrices[i][j])return false
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
                    res += matrices[i][j].toString() + ' '
                else res +=matrices[i][j].toString()
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
                res += matrices[i][j].toString() + ' '
                else res +=matrices[i][j].toString()
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
        for(i in 0 until height)
        {
            for(j in 0 until width)
            {
                if(matrices[i][j] != ComplexNumber())return false
            }
        }
        return true
    }

    fun isNumber() = (width == 1 && height == 1)

}