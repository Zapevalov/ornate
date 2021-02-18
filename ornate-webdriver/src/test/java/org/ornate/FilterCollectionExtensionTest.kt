package org.ornate

import org.hamcrest.MatcherAssert
import org.hamcrest.collection.IsCollectionWithSize
import org.junit.Test
import org.mockito.Mockito
import org.ornate.extension.FilterCollectionExtension
import org.ornate.testdata.ObjectFactory.mockOrnateWebElement
import java.util.*

class FilterCollectionExtensionTest {
    private lateinit var collection: ElementsCollection<OrnateWebElement<*>>
    private val element: OrnateWebElement<*> = mockOrnateWebElement()

    @Test
    fun shouldReturnNewCollectionAfterFilter() {
        Mockito.`when`(element.isDisplayed).thenReturn(IS_DISPLAYED)

        MatcherAssert.assertThat(
            "Should return only displayed element",
            createElementsCollection(arrayOf(element))
                .filter(OrnateWebElement<*>::isDisplayed),
            IsCollectionWithSize.hasSize<Any>(1)
        )
    }

    @Test
    fun shouldFilterElementsTwice() {
        Mockito.`when`(element.isDisplayed).thenReturn(IS_DISPLAYED)
        Mockito.`when`(element.text).thenReturn(TEXT)

        collection = createElementsCollection(arrayOf(element))
        MatcherAssert.assertThat(
            "Should filter the elements twice",
            collection
                .filter(OrnateWebElement<*>::isDisplayed)
            !!.filter{ it.text.contains(WRONG_TEXT) }, IsCollectionWithSize.hasSize(0)
        )
    }

    @Test
    fun shouldReturnEmptyCollectionIf() {
        Mockito.`when`(element.isDisplayed).thenReturn(NOT_DISPLAYED)
        collection = createElementsCollection(arrayOf(element))

        MatcherAssert.assertThat(
            "Should return empty list",
            collection.filter(OrnateWebElement<*>::isDisplayed),
            IsCollectionWithSize.hasSize(0)
        )
    }

    companion object {
        private const val IS_DISPLAYED = true
        private const val NOT_DISPLAYED = false
        private const val TEXT = "text"
        private const val WRONG_TEXT = "wrong"

        private fun createElementsCollection(elements: Array<OrnateWebElement<*>>): ElementsCollection<OrnateWebElement<*>> {
            return Ornate()
                .extension(FilterCollectionExtension())
                .create(
                    elements.toList(),
                    ElementsCollection::class.java
                ) as ElementsCollection<OrnateWebElement<*>>
        }
    }
}