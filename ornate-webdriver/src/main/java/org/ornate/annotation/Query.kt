package org.ornate.annotation

/**
 * Query marker
 * Use to mark params on your [WebSite] implementation
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class Query(val value: String)
