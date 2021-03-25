package org.ornate

import org.apache.commons.lang3.RandomStringUtils
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.openqa.selenium.WebElement
import org.ornate.extension.ShouldMethodExtension
import org.ornate.testdata.ObjectFactory.mockWebElement
import ru.yandex.qatools.matchers.webdriver.DisplayedMatcher

class ShouldExtensionTest {
    private val baseElement: WebElement = mockWebElement()
    private lateinit var ornateWebElement: OrnateWebElement<*>
    private lateinit var collection: ElementsCollection<OrnateWebElement<*>>


    @Before
    fun createOrnateElementWithExtension() {
        ornateWebElement = Ornate()
            .extension(ShouldMethodExtension())
            .create(baseElement, OrnateWebElement::class.java)
    }

    @Test
    fun shouldPassOneArgumentShouldMethod() {
        Mockito.`when`(baseElement.isDisplayed).thenReturn(IS_DISPLAYED)
        ornateWebElement.should(DISPLAYED_MATCHER)
    }

    @Test
    fun shouldPassOneArgumentShouldMethodWithTimeOut() {
        Mockito.`when`(baseElement.isDisplayed).thenReturn(IS_DISPLAYED)
        ornateWebElement.should(DISPLAYED_MATCHER, TIMEOUT)
    }

    @Test
    fun shouldPassTwoArgumentShouldMethod() {
        val message = RandomStringUtils.randomAlphanumeric(10)
        Mockito.`when`(baseElement.isDisplayed).thenReturn(IS_DISPLAYED)
        ornateWebElement.should(message, DISPLAYED_MATCHER)
    }

    @Test
    fun shouldPassTwoArgumentShouldMethodWithTimeOut() {
        val message = RandomStringUtils.randomAlphanumeric(10)
        Mockito.`when`(baseElement.isDisplayed).thenReturn(IS_DISPLAYED)
        ornateWebElement.should(message, DISPLAYED_MATCHER, TIMEOUT)
    }

    @Test(expected = AssertionError::class)
    fun shouldThrowAssertionErrorInOneArgumentShouldMethod() {
        Mockito.`when`(baseElement.isDisplayed).thenReturn(NOT_DISPLAYED)
        ornateWebElement.should(DISPLAYED_MATCHER)
    }

    @Test(expected = AssertionError::class)
    fun shouldThrowAssertionErrorInTwoArgumentShouldMethod() {
        val message = RandomStringUtils.randomAlphanumeric(10)
        Mockito.`when`(baseElement.isDisplayed).thenReturn(NOT_DISPLAYED)
        ornateWebElement.should(message, DISPLAYED_MATCHER)
    }

    @Test
    fun shouldUseMethodForCollection() {
        collection = createElementsCollection(arrayOf(ornateWebElement))
        collection.should(Matchers.hasSize<WebElement>(1))
    }

    @Test(expected = AssertionError::class)
    fun shouldThrowAssertionErrorForCollection() {
        collection = createElementsCollection(arrayOf(ornateWebElement))
        collection.should(Matchers.hasSize<WebElement>(0))
    }

    @Test
    fun shouldUseMethodForCollectionElements() {
        Mockito.`when`(ornateWebElement.isDisplayed).thenReturn(IS_DISPLAYED)
        collection = createElementsCollection(arrayOf(ornateWebElement))
        collection.should(Matchers.hasItem(DISPLAYED_MATCHER))
    }

    @Test(expected = AssertionError::class)
    fun shouldThrowAssertionErrorForCollectionElements() {
        Mockito.`when`(ornateWebElement.isDisplayed).thenReturn(NOT_DISPLAYED)
        collection = createElementsCollection(arrayOf(ornateWebElement))
        collection.should(Matchers.hasItem(DISPLAYED_MATCHER))
    }

    companion object {
        private val DISPLAYED_MATCHER: Matcher<WebElement> = DisplayedMatcher.displayed()
        private const val IS_DISPLAYED = true
        private const val NOT_DISPLAYED = false
        private const val TIMEOUT = 5

        @Suppress("UNCHECKED_CAST")
        private fun createElementsCollection(elements: Array<OrnateWebElement<*>>): ElementsCollection<OrnateWebElement<*>> {
            return Ornate()
                .extension(ShouldMethodExtension())
                .create(elements.toList(), ElementsCollection::class.java) as ElementsCollection<OrnateWebElement<*>>
        }
    }
}