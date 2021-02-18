package org.ornate.api

import kotlin.reflect.KClass

@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@kotlin.annotation.Target(AnnotationTarget.FUNCTION)
annotation class Retry(
    val timeout: Long = 15000L,
    val polling: Long = 1000L,
    val ignoring: Array<KClass<out Throwable>> = []
)