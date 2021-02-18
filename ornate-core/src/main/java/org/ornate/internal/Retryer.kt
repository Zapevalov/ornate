package org.ornate.internal

import kotlin.Throws
import kotlin.reflect.KClass
import java.lang.InterruptedException

/**
 * Retryer.
 */
interface Retryer {
    @Throws(Throwable::class)
    fun shouldRetry(start: Long, e: Throwable?): Boolean

    @JvmDefault
    fun shouldRetry(
        start: Long,
        timeout: Long,
        polling: Long,
        ignoring: List<KClass<out Throwable>?>?,
        e: Throwable?
    ): Boolean {
        if(ignoring.isNullOrEmpty()) return false

        if(!ignoring.any { it!!.isInstance(e) && start + timeout < System.currentTimeMillis() }){
            try {
                Thread.sleep(polling)
                return true
            } catch (i: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }
        return false
    }

    fun timeoutInSeconds(seconds: Int)
}