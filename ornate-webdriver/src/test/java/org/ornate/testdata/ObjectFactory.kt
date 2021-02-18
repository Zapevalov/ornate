package org.ornate.testdata

import org.openqa.selenium.WebDriver
import org.mockito.Mockito
import org.openqa.selenium.interactions.HasInputDevices
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Locatable
import org.ornate.OrnateWebElement

object ObjectFactory {
    fun mockWebDriver(): WebDriver {
        return Mockito.mock(WebDriver::class.java, Mockito.withSettings().extraInterfaces(HasInputDevices::class.java))
    }

    fun mockWebElement(): WebElement {
        return Mockito.mock(WebElement::class.java, Mockito.withSettings().extraInterfaces(Locatable::class.java))
    }

    fun mockOrnateWebElement(): OrnateWebElement<WebElement> {
        return Mockito.mock(OrnateWebElement::class.java, Mockito.withSettings()) as OrnateWebElement<WebElement>
    }
}