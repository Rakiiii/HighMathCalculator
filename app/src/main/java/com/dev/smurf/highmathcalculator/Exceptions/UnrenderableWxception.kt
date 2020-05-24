package com.dev.smurf.highmathcalculator.Exceptions

open class UnrenderableWxception : Exception()
{
    override val message: String?
        get() = "Matrix to wide to render in holder"
}