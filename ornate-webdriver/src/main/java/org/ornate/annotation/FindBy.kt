package org.ornate.annotation

import org.ornate.extension.Selector

/**
 * This annotation is maker for [FindByCollectionExtension] and [FindByExtension]
 */
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class FindBy(val value: String, val selector: Selector = Selector.XPATH)