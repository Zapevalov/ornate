package org.ornate

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.remote.RemoteWebDriver
import org.ornate.testdata.ObjectFactory.mockWebElement


class ExecuteJScriptMethodExtensionTest {
    private lateinit var ornateWebElement: OrnateWebElement<*>
    private lateinit var driver: WebDriver

    @Before
    fun initElements() {
        driver = Mockito.mock(
            RemoteWebDriver::class.java, Mockito.withSettings().extraInterfaces(WebDriver::class.java)
        )
        val originWebElement: WebElement = mockWebElement()
        ornateWebElement = Ornate(WebDriverConfiguration(driver))
            .create(originWebElement, OrnateWebElement::class.java)
    }

    @Test
    fun wrappedElementMethodTest() {
        val js = driver as JavascriptExecutor?
        val bodyScript = "arguments[0].click();"
        ornateWebElement.executeScript(bodyScript)
        Mockito.verify(js, Mockito.times(1))!!.executeScript(bodyScript, ornateWebElement)
    }
}