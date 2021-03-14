package org.ornate.annotation

/**
 * set specific name for function
 *
 * This may be needed to replace the more technical names of elements
 * in the report with those that are usually used by developers
 * example:
 * <pre>
 *     @Name(value = "Registration block")
 *     fun getMainBlock()
 * </pre>
 */
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class Name(val value: String)
