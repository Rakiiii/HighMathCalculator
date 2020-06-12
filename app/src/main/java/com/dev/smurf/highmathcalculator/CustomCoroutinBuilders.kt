package com.dev.smurf.highmathcalculator

import com.dev.smurf.highmathcalculator.Exceptions.TimeableException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.coroutines.CoroutineContext

//coroutine builder wrapper wrapper
suspend fun <T> withTime(context: CoroutineContext, time: GregorianCalendar, action :suspend CoroutineScope.() -> T) : T
{
    return withContext(context) {
        try { action() }
        //wrap error for better handling
        catch (e: TimeableException) { e.time = time;throw e }
    }
}