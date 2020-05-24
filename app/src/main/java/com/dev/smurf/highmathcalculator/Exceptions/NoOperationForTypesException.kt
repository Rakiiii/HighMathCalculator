package com.dev.smurf.highmathcalculator.Exceptions

open class NoOperationForTypesException(type : String,val form : String) : WrongTypeForOperationException(type)
{
    override val message: String?
        get() = "No operation $type for $form"
}