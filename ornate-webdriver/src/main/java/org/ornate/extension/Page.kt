package org.ornate.extension

/**
 * Page marker. Use on your [WebSite] implementation.
 */
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class Page(val url: String = "/")

