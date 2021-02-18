package org.ornate

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Locatable
import org.ornate.testdata.ObjectFactory.mockWebElement


class OrnateWebElementMethodsTest {
    private lateinit var ornateWebElement: OrnateWebElement<*>
    private lateinit var originWebElement: WebElement

    @Before
    fun initElements() {
        originWebElement = mockWebElement()
        ornateWebElement = Ornate().create(originWebElement, OrnateWebElement::class.java)
    }

    @Test
    fun clickMethodTest() {
        ornateWebElement.click()
        Mockito.verify(originWebElement, Mockito.times(1))?.click()
    }

    @Test
    fun submitMethodTest() {
        ornateWebElement.submit()
        Mockito.verify(originWebElement, Mockito.times(1))?.submit()
    }

    @Test
    fun sendKeysMethodTest() {
        ornateWebElement.sendKeys()
        Mockito.verify(originWebElement, Mockito.times(1))?.sendKeys()
    }

    @Test
    fun clearMethodTest() {
        ornateWebElement.clear()
        Mockito.verify(originWebElement, Mockito.times(1))?.clear()
    }

    @Test
    fun tagNameMethodTest() {
        ornateWebElement.tagName
        Mockito.verify(originWebElement, Mockito.times(1))?.tagName
    }

    @Test
    fun attributeMethodTest() {
        ornateWebElement.getAttribute("")
        Mockito.verify(originWebElement, Mockito.times(1))?.getAttribute("")
    }

    @Test
    fun isSelectedMethodTest() {
        ornateWebElement.isSelected
        Mockito.verify(originWebElement, Mockito.times(1))?.isSelected
    }

    @Test
    fun isEnabledMethodTest() {
        ornateWebElement.isEnabled
        Mockito.verify(originWebElement, Mockito.times(1))?.isEnabled
    }

    @Test
    fun textMethodTest() {
        ornateWebElement.text
        Mockito.verify(originWebElement, Mockito.times(1))?.text
    }

    @Test
    fun findElementsMethodTest() {
        ornateWebElement.findElements(By.xpath(""))
        Mockito.verify(originWebElement, Mockito.times(1))?.findElements(By.xpath(""))
    }

    @Test
    fun findElementMethodTest() {
        ornateWebElement.findElement(By.xpath(""))
        Mockito.verify(originWebElement, Mockito.times(1))?.findElement(By.xpath(""))
    }

    @Test
    fun isDisplayedMethodTest() {
        ornateWebElement.isDisplayed
        Mockito.verify(originWebElement, Mockito.times(1))?.isDisplayed
    }

    @Test
    fun locationMethodTest() {
        ornateWebElement.location
        Mockito.verify(originWebElement, Mockito.times(1))?.location
    }

    @Test
    fun sizeMethodTest() {
        ornateWebElement.size
        Mockito.verify(originWebElement, Mockito.times(1))?.size
    }

    @Test
    fun rectMethodTest() {
        ornateWebElement.rect
        Mockito.verify(originWebElement, Mockito.times(1))?.rect
    }

    @Test
    fun cssValueMethodTest() {
        ornateWebElement.getCssValue("")
        Mockito.verify(originWebElement, Mockito.times(1))?.getCssValue("")
    }

    @Test
    fun coordinatesMethodTest() {
        ornateWebElement.coordinates
        Mockito.verify(originWebElement as Locatable?, Mockito.times(1))?.coordinates
    }

    @Test
    fun toStringMethodTest() {
        assertThat(ornateWebElement.toString()).isEqualTo(originWebElement.toString())
    }

    @Test
    fun hashCodeMethodTest() {
        assertThat(ornateWebElement.hashCode()).isEqualTo(originWebElement.hashCode())
    }
}
