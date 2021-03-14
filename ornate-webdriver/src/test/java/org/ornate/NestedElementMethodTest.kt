package org.ornate

import org.assertj.core.api.Assertions
import org.junit.Test
import org.mockito.Mockito
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.ornate.annotation.FindBy
import org.ornate.extension.FindByExtension
import org.ornate.extension.ToStringMethodExtension
import org.ornate.testdata.ObjectFactory.mockWebElement

class NestedElementMethodTest {
    @Test
    fun shouldFindNestedElement() {
        val parent: WebElement = mockWebElement()
        val child: WebElement = mockWebElement()
        Mockito.`when`(parent.findElement(By.xpath(CHILD_SELECTOR))).thenReturn(child)
        val leaf: WebElement = mockWebElement()
        Mockito.`when`(child.findElement(By.xpath(LEAF_SELECTOR))).thenReturn(leaf)

        val parentElement: ParentElement = Ornate()
            .extension(FindByExtension())
            .extension(ToStringMethodExtension())
            .create(parent, ParentElement::class.java)

        Assertions.assertThat(parentElement.child()).isNotNull
        parentElement.child().isDisplayed
        Mockito.verify(child).isDisplayed

        Assertions.assertThat(parentElement.child().leaf()).isNotNull
        parentElement.child().leaf().isDisplayed
        Mockito.verify(leaf).isDisplayed
    }

    internal interface ParentElement : OrnateWebElement<WebElement> {
        @FindBy(CHILD_SELECTOR)
        fun child(): ChildElement
    }

    internal interface ChildElement : OrnateWebElement<WebElement> {
        @FindBy(LEAF_SELECTOR)
        fun leaf(): LeafElement
    }

    internal interface LeafElement : OrnateWebElement<WebElement>

    companion object {
        private const val CHILD_SELECTOR = "//div"
        private const val LEAF_SELECTOR = "//div"
    }
}