package com.example.smurf.mtarixcalc



data class polGroup( var polLeftPolinom : polinom,
                     var polRightPolinom : polinom,
                     var polSignPolinom : String,
                     var polResPolinom : polinom,
                     var polOstPolinom : polinom? = null,
                     var symb : Char = 'x')
