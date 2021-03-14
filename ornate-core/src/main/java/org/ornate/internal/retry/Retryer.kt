package org.ornate.internal.retry

import kotlin.Throws
import kotlin.reflect.KClass
import java.lang.InterruptedException

/**
 * Retryer
 *
 * Some cases needs in rechecks: findElements, wait to load pages, wait elements, etc
 * For this situations use this interface what realizes such classes like CustomRetryer.class and DefaultRetryer.class
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
        //TODO remove because ".any()" has this check
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