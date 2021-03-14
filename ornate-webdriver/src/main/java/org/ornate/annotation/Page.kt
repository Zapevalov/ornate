package org.ornate.annotation

/**
 * Page marker
 * Use for [WebPage] implementation
 */
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class Page(val url: String = "/")

