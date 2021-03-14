package org.ornate.annotation

/**
 * QueryMap marker
 * Use to mark params as [Map] on your [WebSite] implementation
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class QueryMap
