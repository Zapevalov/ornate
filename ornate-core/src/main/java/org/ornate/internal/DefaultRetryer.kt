package org.ornate.internal

import org.ornate.api.Retry
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

/**
 * Retryer.
 */
class DefaultRetryer(timeout: Long, polling: Long, ignoring: List<KClass<out Throwable>?>?) : Retryer {
    private val ignoring: MutableList<KClass<out Throwable>?>? = ignoring?.toMutableList()
    private var timeout: Long
    private var polling: Long

    constructor(annotation: Retry) : this(
        annotation.timeout,
        annotation.polling,
        annotation.ignoring.toList()
    )

    fun ignore(throwable: KClass<out Throwable>) {
        ignoring?.add(throwable)
    }

    fun timeoutInMillis(millis: Long) {
        timeout = millis
    }

    override fun timeoutInSeconds(seconds: Int) {
        timeout = TimeUnit.SECONDS.toMillis(seconds.toLong())
    }

    fun polling(polling: Long) {
        this.polling = polling
    }

    override fun shouldRetry(start: Long, e: Throwable?): Boolean {
        return shouldRetry(start, timeout, polling, ignoring, e)
    }

    init {
        this.timeout = timeout
        this.polling = polling
    }
}