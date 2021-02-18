package org.ornate.extension

/**
 * FindBy extension marker.
 */
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class FindBy(val value: String, val selector: Selector = Selector.XPATH)