package org.ornate

import org.junit.Test
import org.mockito.Mockito
import org.openqa.selenium.By
import org.openqa.selenium.NotFoundException
import org.openqa.selenium.WebElement
import org.ornate.api.Retry
import org.ornate.context.RetryerContext
import org.ornate.annotation.FindBy
import org.ornate.internal.retry.CustomRetryer
import org.ornate.internal.retry.DefaultRetryer
import org.ornate.testdata.ObjectFactory.mockWebDriver
import org.ornate.testdata.ObjectFactory.mockWebElement

class FindByRetrierTest {

    @Test(expected = NotFoundException::class)
    fun retryChildFind() {
        val parentOrigin: WebElement = mockWebElement()
        val childOrigin: WebElement = mockWebElement()
        Mockito.`when`(parentOrigin.findElement(By.xpath("//div")))
            .thenThrow(NotFoundException())
        Mockito.`when`(childOrigin.isDisplayed)
            .thenThrow(NotFoundException())
        val ornate = Ornate(WebDriverConfiguration(mockWebDriver()))
        val parent: ParentElement = ornate.create(parentOrigin, ParentElement::class.java)
        parent.child().isDisplayed
    }

    @Test(expected = NotFoundException::class)
    fun shouldSetGlobalRetryThroughCustomRetryer() {
        val parentOrigin: WebElement = mockWebElement()
        Mockito.`when`(parentOrigin.isDisplayed).thenThrow(NotFoundException())
        val ornate: Ornate = Ornate(WebDriverConfiguration(mockWebDriver()))
            .context(
                RetryerContext(
                    CustomRetryer(3000L, 1000L, listOf(Throwable::class))
                )
            )
        val parent: ParentElement = ornate.create(parentOrigin, ParentElement::class.java)
        parent.isDisplayed
    }

    @Test(expected = NotFoundException::class)
    fun shouldSetGlobalRetryThroughDefaultRetryer() {
        val parentOrigin: WebElement = mockWebElement()
        Mockito.`when`(parentOrigin.isDisplayed).thenThrow(NotFoundException())
        val ornate: Ornate = Ornate(WebDriverConfiguration(mockWebDriver()))
            .context(RetryerContext(DefaultRetryer()))
        val parent: ParentElement = ornate.create(parentOrigin, ParentElement::class.java)
        parent.isDisplayed
    }

    internal interface ParentElement : OrnateWebElement<WebElement> {
        @Retry(timeout = 8000)
        @FindBy("//div")
        fun child(): NestedElement
    }

    internal interface NestedElement : OrnateWebElement<WebElement> {
       override fun isDisplayed(): Boolean
    }
}