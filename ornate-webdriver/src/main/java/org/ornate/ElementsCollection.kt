package org.ornate

import org.hamcrest.Matcher
import org.openqa.selenium.WebElement
import org.ornate.api.Timeout
import java.util.function.Function
import java.util.function.Predicate


/**
 * Ornate Web Element Collection.
 * @param <E> the type of elements in this collection
</E> */
interface ElementsCollection<E> : MutableList<E> {
    /**
     * This method handled by the [org/ornate/extension/FilterCollectionExtension].
     */
//    fun filter(predicate: Predicate<E>?): ElementsCollection<E>?

    fun filter(predicate: (E) -> Boolean): ElementsCollection<E>?

    /**
     * This method handled by the [ExtractMethodExtension].
     */
    fun <R> extract(function: Function<E, R>?): ElementsCollection<R>

    /**
     * This method handled by the [ShouldMethodExtension].
     */
    fun should(matcher: Matcher<*>?): ElementsCollection<E>?

    /**
     * This method handled by the [ShouldMethodExtension].
     */
    fun should(message: String?, matcher: Matcher<*>?): ElementsCollection<E>?

    /**
     * This method handled by the [WaitUntilMethodExtension].
     */
    fun waitUntil(matcher: Matcher<*>?): ElementsCollection<E>?

    /**
     * This method handled by the [WaitUntilMethodExtension].
     */
    fun waitUntil(matcher: Matcher<*>?, @Timeout timeoutInSeconds: Int?): ElementsCollection<E>?

    /**
     * This method handled by the [WaitUntilMethodExtension].
     */
    fun waitUntil(message: String?, matcher: Matcher<*>?): ElementsCollection<E>?

    /**
     * This method handled by the [WaitUntilMethodExtension].
     */
    fun waitUntil(message: String?, matcher: Matcher<*>?, @Timeout timeoutInSeconds: Int?): ElementsCollection<E>?
}