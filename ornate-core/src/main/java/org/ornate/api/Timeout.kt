package org.ornate.api

/**
 * This annotation marks the time parameter (int).
 * Framework will wait fixed number of seconds to fulfill
 * some condition and
 * if condition will not execute then it will throw exception
 */

@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@kotlin.annotation.Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Timeout