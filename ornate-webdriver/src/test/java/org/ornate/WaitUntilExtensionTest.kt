package org.ornate

import org.apache.commons.lang3.RandomStringUtils
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.openqa.selenium.WebElement
import org.ornate.exception.WaitUntilException
import org.ornate.extension.WaitUntilMethodExtension
import org.ornate.testdata.ObjectFactory.mockWebElement
import ru.yandex.qatools.matchers.webdriver.DisplayedMatcher

class WaitUntilExtensionTest {
    private val baseElement: WebElement = mockWebElement()
    private lateinit var ornateWebElement: OrnateWebElement<*>
    private lateinit var collection: ElementsCollection<OrnateWebElement<*>>
    @Before
    fun createOrnateElementWithExtension() {
        ornateWebElement = Ornate()
            .extension(WaitUntilMethodExtension())
            .create(baseElement, OrnateWebElement::class.java)
    }

    @Test
    fun shouldPassOneArgumentWaitUntilMethod() {
        Mockito.`when`(baseElement.isDisplayed).thenReturn(IS_DISPLAYED)
        ornateWebElement.waitUntil(DISPLAYED_MATCHER)
    }

    @Test
    fun shouldPassTwoArgumentWaitUntilMethod() {
        val message = RandomStringUtils.randomAlphanumeric(10)
        Mockito.`when`(baseElement.isDisplayed).thenReturn(IS_DISPLAYED)
        ornateWebElement.waitUntil(message, DISPLAYED_MATCHER)
    }

    @Test(expected = WaitUntilException::class)
    fun shouldThrowExceptionInOneArgumentWaitUntilMethod() {
        Mockito.`when`(baseElement.isDisplayed).thenReturn(NOT_DISPLAYED)
        ornateWebElement.waitUntil(DISPLAYED_MATCHER)
    }

    @Test(expected = WaitUntilException::class)
    fun shouldThrowExceptionInTwoArgumentWaitUntilMethod() {
        val message = RandomStringUtils.randomAlphanumeric(10)
        Mockito.`when`(baseElement.isDisplayed).thenReturn(NOT_DISPLAYED)
        ornateWebElement.waitUntil(message, DISPLAYED_MATCHER)
    }

    @Test
    fun shouldUseMethodForCollection() {
        collection = createElementsCollection(arrayOf(ornateWebElement))
        collection.waitUntil(Matchers.hasSize<WebElement>(1))
    }

    @Test(expected = WaitUntilException::class)
    fun shouldThrowExceptionForCollection() {
        collection = createElementsCollection(arrayOf(ornateWebElement))
        collection.waitUntil(Matchers.hasSize<WebElement>(0))
    }

    @Test
    fun shouldUseMethodForCollectionElements() {
        Mockito.`when`(ornateWebElement.isDisplayed).thenReturn(IS_DISPLAYED)
        collection = createElementsCollection(arrayOf(ornateWebElement))
        collection.waitUntil(Matchers.hasItem(DISPLAYED_MATCHER))
    }

    @Test(expected = WaitUntilException::class)
    fun shouldThrowExceptionForCollectionElements() {
        Mockito.`when`(ornateWebElement.isDisplayed).thenReturn(NOT_DISPLAYED)
        collection = createElementsCollection(arrayOf(ornateWebElement))
        collection.waitUntil(Matchers.hasItem(DISPLAYED_MATCHER))
    }

    companion object {
        private val DISPLAYED_MATCHER: Matcher<WebElement> = DisplayedMatcher.displayed()
        private const val IS_DISPLAYED = true
        private const val NOT_DISPLAYED = false

        @Suppress("UNCHECKED_CAST")
        private fun createElementsCollection(elements: Array<OrnateWebElement<*>>): ElementsCollection<OrnateWebElement<*>> {
            return Ornate()
                .extension(WaitUntilMethodExtension())
                .create(elements.toList(), ElementsCollection::class.java) as ElementsCollection<OrnateWebElement<*>>
        }
    }
}