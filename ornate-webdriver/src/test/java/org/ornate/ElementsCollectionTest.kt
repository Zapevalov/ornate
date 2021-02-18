package org.ornate

import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.collection.IsCollectionWithSize
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.mockito.Mockito
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.ornate.extension.FindBy
import org.ornate.extension.FindByCollectionExtension
import org.ornate.testdata.ObjectFactory.mockOrnateWebElement
import org.ornate.testdata.ObjectFactory.mockWebElement
import java.util.ArrayList
import java.util.concurrent.atomic.AtomicInteger

class ElementsCollectionTest {
    private lateinit var parent: WebElement
    private lateinit var collection: ElementsCollection<WebElement>

    @Before
    fun setUp() {
        parent = mockWebElement()
        collection = Mockito.mock(ElementsCollection::class.java) as ElementsCollection<WebElement>
    }

    @Test
    fun shouldFindElementsCollection() {
        Mockito.`when`(parent.findElements(By.xpath(SELECTOR))).thenReturn(collection)
        Mockito.`when`(collection.size).thenReturn(DEFAULT_SIZE)
        val parentElement: ParentElement = Ornate().extension(FindByCollectionExtension())
            .create(parent, ParentElement::class.java)

        assertThat(parentElement.collection().size).isEqualTo(DEFAULT_SIZE)
    }

    @Test(expected = NullPointerException::class)
    fun shouldNotInteractWithoutResult() {
        Mockito.`when`(parent.findElements(By.xpath(SELECTOR))).thenReturn(null)
        Mockito.`when`(collection.size).thenReturn(DEFAULT_SIZE)
        val parentElement: ParentElement = Ornate().extension(FindByCollectionExtension())
            .create(parent, ParentElement::class.java)
        assertThat(parentElement.collection().size).isEqualTo(DEFAULT_SIZE)
    }

    @Test
    fun shouldFindNestedElementFromCollection() {
        val listElement: OrnateWebElement<WebElement> = mockOrnateWebElement() as OrnateWebElement<WebElement>
        val block: OrnateWebElement<WebElement> = mockOrnateWebElement() as OrnateWebElement<WebElement>

        Mockito.`when`(parent.findElements(By.xpath(SELECTOR))).thenReturn(collection)
        Mockito.`when`(collection.size).thenReturn(DEFAULT_SIZE)
        Mockito.`when`(collection[0]).thenReturn(listElement)
        Mockito.`when`(listElement.findElement(By.xpath(SELECTOR))).thenReturn(block)
        Mockito.`when`(block.isDisplayed).thenReturn(true)

        val parentElement: ParentElement = Ornate(WebDriverConfiguration(Mockito.mock(WebDriver::class.java)))
            .create(parent, ParentElement::class.java)
        val element = parentElement.collection()[0]
        assertThat(element.block().isDisplayed).isEqualTo(true)
    }

    @Test
    fun shouldRefreshCollectionWhenWaiting() {
        val listElement: WebElement = mockWebElement()
        val count = AtomicInteger()
        Mockito.`when`(parent.findElements(By.xpath(SELECTOR)))
            .then {
                if (count.incrementAndGet() > 3) {
                    listOf(listElement)
                } else {
                    arrayListOf()
                }
            }
        val parentElement: ParentElement = Ornate(WebDriverConfiguration(Mockito.mock(WebDriver::class.java)))
            .create(parent, ParentElement::class.java)
        parentElement.collection()
            .should(IsCollectionWithSize.hasSize<WebElement>(1))
    }

    internal interface ParentElement : OrnateWebElement<WebElement> {
        @FindBy(SELECTOR)
        fun collection(): ElementsCollection<ListElement>
    }

    internal interface ListElement : OrnateWebElement<WebElement> {
        @FindBy(SELECTOR)
        fun block(): OrnateWebElement<WebElement>
    }

    companion object {
        private const val SELECTOR = "//div"
        private const val DEFAULT_SIZE = 1
    }
}
