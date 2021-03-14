package org.ornate

import org.apache.commons.lang3.RandomStringUtils
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.ornate.annotation.FindBy
import org.ornate.extension.FindByCollectionExtension
import org.ornate.extension.FindByExtension
import org.ornate.annotation.Param
import org.ornate.testdata.ObjectFactory.mockWebElement


class FindByParameterizedTest {
    private val parent: WebElement = mockWebElement()
    private lateinit var ornate: Ornate

    @Test
    fun shouldParameterizedFindBy() {
        Mockito.`when`(parent.findElement(ArgumentMatchers.any(By::class.java))).thenReturn(mockWebElement())
        ornate = Ornate().extension(FindByExtension())

        val param = RandomStringUtils.randomAlphanumeric(10)
        val ornateWebElement: ParentElement = ornate.create(parent, ParentElement::class.java)

        ornateWebElement.childWithName(param).isDisplayed

        Mockito.verify(parent, Mockito.times(1))
            .findElement(By.xpath("//div[$param]"))
    }

    @Test
    fun shouldParameterizedFindByCollection() {
        Mockito.`when`(parent.findElements(ArgumentMatchers.any(By::class.java)))
            .thenReturn(listOf(mockWebElement()))

        ornate = Ornate().extension(FindByCollectionExtension())
        val param = RandomStringUtils.randomAlphanumeric(10)

        val ornateWebElement: ParentElement = ornate.create(parent, ParentElement::class.java)

        ornateWebElement.elements(param).size
        Mockito.verify(parent, Mockito.times(1))
            .findElements(By.xpath("//td[$param]"))
    }

    internal interface ParentElement : OrnateWebElement<WebElement> {
        @FindBy("//div[{{ value }}]")
        fun childWithName(@Param("value") value: String?): OrnateWebElement<WebElement>

        @FindBy("//td[{{ value }}]")
        fun elements(@Param("value") value: String?): ElementsCollection<OrnateWebElement<WebElement>>
    }
}