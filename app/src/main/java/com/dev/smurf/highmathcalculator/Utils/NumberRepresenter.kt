package com.dev.smurf.highmathcalculator.Utils

import android.graphics.Color


//fast render of number in RunnableMatrixView
//set pixels alot faster then any other way
//todo:: remove as unnessasery legacy [better solution must be found]
class NumberRepresenter
{
    companion object
    {
        var color : Int = Color.BLACK
        val numberHigh = 20
        val numberWidth = 16

        fun GetNumberRepresentation(num : Int) : IntArray
        {
            val number = IntArray(numberHigh* numberWidth)
            when (num)
            {
                0->{
                                                       number[6] =color;number[7] =color;number[8] =color;number[9] =color
                                        number[21] =color;number[22] =color;number[23] =color;number[24] =color;number[25] =color;number[26] =color
                            number[36] =color;number[37] =color;number[38] =color;                           number[41] =color;number[42] =color;number[43] =color
                    number[51] =color;number[52] =color;number[53] =color;                                                       number[58] =color;number[59] =color;number[60] =color
                    number[67] =color;number[68] =color;number[69] =color;                                                       number[74] =color;number[75] =color;number[76] =color
                    number[83] =color;number[84] =color;number[85] =color;                                                       number[90] =color;number[91] =color;number[92] =color
                    number[99] =color;number[100] =color;number[101] =color;                                                     number[106] =color;number[107] =color;number[108] =color
                    number[115] =color;number[116] =color;number[117] =color;                                                    number[122] =color;number[123] =color;number[124] =color
                    number[131] =color;number[132] =color;number[133] =color;                                                    number[138] =color;number[139] =color;number[140] =color
                    number[147] =color;number[148] =color;number[149] =color;                                                    number[154] =color;number[155] =color;number[156] =color
                    number[163] =color;number[164] =color;number[165] =color;                                                    number[170] =color;number[171] =color;number[172] =color
                    number[179] =color;number[180] =color;number[181] =color;                                                    number[186] =color;number[187] =color;number[188] =color
                    number[195] =color;number[196] =color;number[197] =color;                                                    number[202] =color;number[203] =color;number[204] =color
                    number[211] =color;number[212] =color;number[213] =color;                                                    number[218] =color;number[219] =color;number[220] =color
                    number[227] =color;number[228] =color;number[229] =color;                                                    number[234] =color;number[235] =color;number[236] =color
                    number[243] =color;number[244] =color;number[245] =color;                                                    number[250] =color;number[251] =color;number[252] =color
                    number[259] =color;number[260] =color;number[261] =color;                                                    number[266] =color;number[267] =color;number[268] =color
                                    number[276] =color;number[277] =color;number[278] =color;                number[281] =color;number[282] =color;number[283] =color;
                                                    number[293] =color;number[294] =color;number[295] =color;number[296] =color;number[297] =color;number[298] =color;
                                                                    number[310] =color;number[311] =color;number[312] =color;number[313] =color;
                }
                1->{
                                                                                        number[7] =color;number[8] =color
                                                                        number[22] =color;number[23] =color;number[24] =color;number[25] =color
                                                        number[37] =color;number[38] =color;number[39] =color;number[40] =color;number[41] =color;number[42] =color
                                        number[52] =color;number[53] =color;number[54] =color;number[55] =color;number[56] =color;number[57] =color;number[58] =color
                    number[67] =color;number[68] =color;number[69] =color;number[70] =color;number[71] =color;number[72] =color;number[73] =color;number[74] =color
                    number[67+16] =color;number[68+16] =color;number[69+16] =color;number[70+16] =color;number[71+16] =color;number[72+16] =color;number[73+16] =color;number[74+16] =color
                    number[67+32] =color;number[68+32] =color;                      number[70+32] =color;number[71+32] =color;number[72+32] =color;number[73+32] =color;number[74+32] =color
                    number[67+48] =color;                                           number[70+48] =color;number[71+48] =color;number[72+48] =color;number[73+48] =color;number[74+48] =color
                                                                                    number[118+16] =color;number[119+16] =color;number[120+16] =color;number[121+16] =color;number[122+16] =color
                                                                                    number[118+16*2] =color;number[119+16*2] =color;number[120+16*2] =color;number[121+16*2] =color;number[122+16*2] =color
                                                                                    number[118+16*3] =color;number[119+16*3] =color;number[120+16*3] =color;number[121+16*3] =color;number[122+16*3] =color
                                                                                    number[118+16*4] =color;number[119+16*4] =color;number[120+16*4] =color;number[121+16*4] =color;number[122+16*4] =color
                                                                                    number[118+16*5] =color;number[119+16*5] =color;number[120+16*5] =color;number[121+16*5] =color;number[122+16*5] =color
                                                                                    number[118+16*6] =color;number[119+16*6] =color;number[120+16*6] =color;number[121+16*6] =color;number[122+16*6] =color
                                                                                    number[118+16*7] =color;number[119+16*7] =color;number[120+16*7] =color;number[121+16*7] =color;number[122+16*7] =color
                                                                                    number[118+16*8] =color;number[119+16*8] =color;number[120+16*8] =color;number[121+16*8] =color;number[122+16*8] =color
                                                                                    number[118+16*9] =color;number[119+16*9] =color;number[120+16*9] =color;number[121+16*9] =color;number[122+16*9] =color
                                                                                    number[118+16*10] =color;number[119+16*10] =color;number[120+16*10] =color;number[121+16*10] =color;number[122+16*10] =color
                    number[275] =color;number[276] =color;number[277] =color;number[278] =color;number[279] =color;number[280] =color;number[281] =color;number[282] =color;number[283] =color;number[284] =color
                    number[275+16] =color;number[276+16] =color;number[277+16] =color;number[278+16] =color;number[279+16] =color;number[280+16] =color;number[281+16] =color;number[282+16] =color;number[283+16] =color;number[284+16] =color

                }
                2->{

                }
                3->{

                }
                4->{

                }
                5-> {
                }
                6->{

                }
                7->{
                }
                8->{

                }
                9->{


                }
                else -> {
                    throw Exception("WrongNumberError")
                }
            }
            for(i in number.indices)
            {
                if(number[i] != color) number[i] = Color.WHITE
            }
                return number

        }
    }
}