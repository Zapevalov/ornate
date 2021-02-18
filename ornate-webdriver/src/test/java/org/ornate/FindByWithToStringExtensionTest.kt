package org.ornate

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.mockito.Mockito
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.ornate.extension.FindBy
import org.ornate.extension.FindByExtension
import org.ornate.extension.Name
import org.ornate.extension.ToStringMethodExtension
import org.ornate.testdata.ObjectFactory.mockOrnateWebElement
import org.ornate.testdata.ObjectFactory.mockWebElement

class FindByWithToStringExtensionTest {
    private lateinit var parent: WebElement
    private lateinit var ornate: Ornate

    @Before
    fun createParent() {
        parent = mockWebElement()
        ornate = Ornate()
            .extension(ToStringMethodExtension())
            .extension(FindByExtension())
    }

    @Test
    fun shouldUseNameAnnotationInFindByExtension() {
        val ornateWebElement: ParentElement = ornate.create(parent, ParentElement::class.java)

        assertThat(ornateWebElement.childWithName().toString()).isEqualTo(ELEMENT_NAME)
    }

    @Test
    fun shouldUseMethodNameInFindByExtension() {
        val ornateWebElement: ParentElement = ornate.create(parent, ParentElement::class.java)

        assertThat(ornateWebElement.childWithoutName().toString()).isEqualTo(METHOD_NAME)
    }

    @Test
    @Ignore("Fix element name in collection")
    fun shouldUseMethodNameInFindByCollectionExtension() {
        val ornateWebElement: ParentElement = ornate.create(parent, ParentElement::class.java)

        assertThat(ornateWebElement.childList().toString()).isEqualTo(METHOD_LIST_NAME)
    }

    @Test
    @Ignore("Fix element name in collection")
    fun shouldUseNameAnnotationInFindByCollectionExtension() {
        val ornateWebElement: ParentElement = ornate.create(parent, ParentElement::class.java)

        assertThat(ornateWebElement.childListWithName().toString()).isEqualTo(ELEMENT_NAME)
    }

    @Test
    @Ignore("Fix element name in collection")
    fun shouldAppendIndexToElementNameFromCollection() {
        val ornateWebElement: ParentElement = ornate.create(parent, ParentElement::class.java)

        Mockito.`when`(parent.findElements(By.xpath(SELECTOR)))
            .thenReturn(listOf(mockOrnateWebElement()))
        val first = 0
        assertThat(ornateWebElement.childListWithName()[first].toString())
            .isEqualTo(listElementName(ELEMENT_NAME, first))
    }

    @Test
    @Ignore("Fix element name in collection")
    fun shouldAppendIndexToMethodNameFromCollection() {
        val ornateWebElement: ParentElement = ornate.create(parent, ParentElement::class.java)

        Mockito.`when`(parent.findElements(By.xpath(SELECTOR))).thenReturn(listOf(mockOrnateWebElement()))
        val first = 0
        assertThat(ornateWebElement.childList()[first].toString())
            .isEqualTo(listElementName(METHOD_LIST_NAME, first))
    }

    internal interface ParentElement : OrnateWebElement<WebElement> {
        @Name(ELEMENT_NAME)
        @FindBy(SELECTOR)
        fun childWithName(): ChildElement

        @FindBy(SELECTOR)
        fun childWithoutName(): ChildElement

        @Name(ELEMENT_NAME)
        @FindBy(SELECTOR)
        fun childListWithName(): ElementsCollection<ChildElement>

        @FindBy(SELECTOR)
        fun childList(): ElementsCollection<ChildElement>
    }

    internal interface ChildElement : OrnateWebElement<WebElement>

    companion object {
        private fun listElementName(name: String, index: Int) = "$name [$index]"
        private const val SELECTOR = "//div"
        private const val ELEMENT_NAME = "my name"
        private const val METHOD_NAME = "childWithoutName"
        private const val METHOD_LIST_NAME = "childList"
    }
}