package org.ornate

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.Mockito
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.ornate.extension.ExtractMethodExtension
import org.ornate.annotation.FindBy
import org.ornate.extension.FindByCollectionExtension
import org.ornate.testdata.ObjectFactory.mockOrnateWebElement
import org.ornate.testdata.ObjectFactory.mockWebElement
import java.util.*

@Suppress("UNCHECKED_CAST")
class ExtractCollectionExtensionTest {
    private lateinit var collection: ElementsCollection<OrnateWebElement<*>>
    private val element: OrnateWebElement<*> = mockOrnateWebElement()

    @Test
    fun shouldExtractElementsCollectionToList() {
        Mockito.`when`(element.text).thenReturn("mytext")
        collection = createElements(arrayOf(element))
        assertThat(collection.extract(OrnateWebElement<*>::getText)).contains(element.text)
    }

    @Test
    fun shouldExtractTwoTimesInARow() {
        val parent: WebElement = mockWebElement()
        val collection: ElementsCollection<WebElement> =
            Mockito.mock(ElementsCollection::class.java) as ElementsCollection<WebElement>
        val status = Mockito.mock(StatusItem::class.java, Mockito.withSettings())
        val innerElement: OrnateWebElement<*> = mockOrnateWebElement()

        Mockito.`when`(parent.text).thenReturn("text")
        Mockito.`when`(parent.findElements(By.xpath(SELECTOR))).thenReturn(collection)
        Mockito.`when`(collection.size).thenReturn(DEFAULT_SIZE)
        Mockito.`when`(collection[0]).thenReturn(status)
        Mockito.`when`<Any>(status.title()).thenReturn(innerElement)
        Mockito.`when`(innerElement.text).thenReturn("innerText")

        val parentElement: ParentElement = Ornate()
            .extension(ExtractMethodExtension())
            .extension(FindByCollectionExtension())
            .create(parent, ParentElement::class.java)

        assertThat(
            parentElement.items()
            .extract(StatusItem::title)
            .extract(OrnateWebElement<*>::getText)
        ).contains("innerText")
    }

    internal interface StatusItem : OrnateWebElement<WebElement> {
        @FindBy(SELECTOR)
        fun title(): OrnateWebElement<WebElement>
    }

    internal interface ParentElement : OrnateWebElement<WebElement> {
        @FindBy(SELECTOR)
        fun items(): ElementsCollection<StatusItem>
    }

    companion object {
        private const val SELECTOR = "//div"
        private const val DEFAULT_SIZE = 1

        private fun createElements(elements: Array<OrnateWebElement<*>>): ElementsCollection<OrnateWebElement<*>> {
            return Ornate()
                .extension(ExtractMethodExtension())
                .create(elements.toList(), ElementsCollection::class.java) as ElementsCollection<OrnateWebElement<*>>
        }
    }
}