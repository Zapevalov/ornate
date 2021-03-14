package org.ornate.annotation

/**
 * Params marker
 * use when we are needed parameterised block, or element
 *
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class Param(val value: String)
