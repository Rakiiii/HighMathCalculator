package com.example.smurf.mtarixcalc



data class polGroup( var polLeftPolinom : polinom,
                     var polRightPolinom : polinom,
                     var polSignPolinom : String,
                     var polResPolinom : polinom,
                     var polOstPolinom : polinom? = null,
                     var symb : Char = 'x')
{
    constructor() : this(polinom(0),polinom(0),"_",polinom(0))
}
