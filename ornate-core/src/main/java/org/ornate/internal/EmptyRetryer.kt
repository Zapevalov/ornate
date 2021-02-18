package org.ornate.internal


import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

/**
 * Retryer with default values.
 */
class EmptyRetryer : Retryer {
    private val polling = 1000L
    private val ignoring = listOf<KClass<out Throwable>>(Throwable::class)
    private var timeout = 5000L
    override fun shouldRetry(start: Long, e: Throwable?): Boolean {
        return shouldRetry(start, timeout, polling, ignoring, e)
    }

    override fun timeoutInSeconds(seconds: Int) {
        timeout = TimeUnit.SECONDS.toMillis(seconds.toLong())
    }

}