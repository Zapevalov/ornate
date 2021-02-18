package org.ornate

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.openqa.selenium.WebElement
import org.ornate.extension.WrappedElementMethodExtension
import org.ornate.testdata.ObjectFactory.mockWebElement

class WrappedElementMethodExtensionTest {
    private lateinit var ornateWebElement: OrnateWebElement<*>
    private lateinit var originWebElement: WebElement

    @Before
    fun initElements() {
        originWebElement = mockWebElement()
        ornateWebElement = Ornate()
            .extension(WrappedElementMethodExtension())
            .create(originWebElement, OrnateWebElement::class.java)
    }

    @Test
    fun wrappedElementMethodTest() {
        val element: WebElement = ornateWebElement.wrappedElement
        element.click()
        Mockito.verify(originWebElement, Mockito.times(1)).click()
    }
}