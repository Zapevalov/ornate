package org.ornate.api

import kotlin.reflect.KClass

/**
 * Interface for realization repeated checks
 *
 * @param timeout - time to complete the check
 * @param polling - check execution frequency
 * @param ignoring - an array with exceptions to be ignored at the fixed time
 */


@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@kotlin.annotation.Target(AnnotationTarget.FUNCTION)
annotation class Retry(
    val timeout: Long = 15000L,
    val polling: Long = 1000L,
    val ignoring: Array<KClass<out Throwable>> = []
)