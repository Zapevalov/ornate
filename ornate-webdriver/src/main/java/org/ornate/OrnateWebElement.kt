package org.ornate

import org.hamcrest.Matcher
import org.openqa.selenium.*
import org.openqa.selenium.interactions.Coordinates
import org.openqa.selenium.interactions.Locatable
import org.openqa.selenium.internal.WrapsElement
import org.ornate.api.Timeout

/**
 * Ornate Web Element.
 * @param <T> the type of the value being boxed
</T> */
interface OrnateWebElement<T : WebElement?> : WrapsElement, WebElement, Locatable {
    /**
     * The same as [WebElement.click].
     */
    override fun click()

    /**
     * The same as [WebElement.submit].
     */
    override fun submit()

    /**
     * The same as [WebElement.sendKeys].
     */
    override fun sendKeys(vararg keysToSend: CharSequence)

    /**
     * The same as [WebElement.clear].
     */
    override fun clear()

    /**
     * The same as [WebElement.getTagName].
     */
    override fun getTagName(): String

    /**
     * The same as [WebElement.getAttribute].
     */
    override fun getAttribute(name: String): String

    /**
     * The same as [WebElement.isSelected].
     */
    override fun isSelected(): Boolean

    /**
     * The same as [WebElement.isEnabled].
     */
    override fun isEnabled(): Boolean

    /**
     * The same as [WebElement.getText].
     */
    override fun getText(): String

    /**
     * The same as [WebElement.findElements].
     */
    override fun findElements(by: By): List<WebElement>

    /**
     * The same as [WebElement.findElement].
     */
    override fun findElement(by: By): WebElement

    /**
     * The same as [WebElement.isDisplayed].
     */
    override fun isDisplayed(): Boolean

    /**
     * The same as [WebElement.getLocation].
     */
    override fun getLocation(): Point

    /**
     * The same as [WebElement.getSize].
     */
    override fun getSize(): Dimension

    /**
     * The same as [WebElement.getRect].
     */
    override fun getRect(): Rectangle

    /**
     * The same as [WebElement.getCssValue].
     */
    override fun getCssValue(propertyName: String): String

    /**
     * The same as [Locatable.getCoordinates].
     */
    override fun getCoordinates(): Coordinates

    /**
     * This method handled by the [ShouldMethodExtension].
     */
    fun should(matcher: Matcher<*>?): T

    /**
     * This method handled by the [ShouldMethodExtension].
     */
    fun should(matcher: Matcher<*>?, @Timeout timeoutInSeconds: Int?): T

    /**
     * This method handled by the [ShouldMethodExtension].
     */
    fun should(message: String?, matcher: Matcher<*>?): T

    /**
     * This method handled by the [ShouldMethodExtension].
     */
    fun should(message: String?, matcher: Matcher<*>?, @Timeout timeoutInSeconds: Int?): T

    /**
     * This method handled by the [WaitUntilMethodExtension].
     */
    fun waitUntil(matcher: Matcher<*>?): T

    /**
     * This method handled by the [WaitUntilMethodExtension].
     */
    fun waitUntil(matcher: Matcher<*>?, @Timeout timeoutInSeconds: Int?): T

    /**
     * This method handled by the [WaitUntilMethodExtension].
     */
    fun waitUntil(message: String?, matcher: Matcher<*>?): T

    /**
     * This method handled by the [WaitUntilMethodExtension].
     */
    fun waitUntil(message: String?, matcher: Matcher<*>?, @Timeout timeoutInSeconds: Int?): T

    /**
     * The same as [WrapsElement.getWrappedElement].
     */
    override fun getWrappedElement(): WebElement

    /**
     * Executes JavaScript in the context of the currently ornateWebElement.
     */
    fun executeScript(script: String?): Any?
}