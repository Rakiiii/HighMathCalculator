package com.dev.smurf.highmathcalculator

import com.dev.smurf.highmathcalculator.Exceptions.TimeableException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.coroutines.CoroutineContext

suspend fun <T> withTime(context: CoroutineContext, time: GregorianCalendar, action :suspend CoroutineScope.() -> T) : T
{
    return withContext(context) {
        try
        {
            action()
        } catch (e: TimeableException)
        {
            e.time = time
            throw e
        }
    }
}