package org.ornate

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.ornate.annotation.FindBy
import org.ornate.annotation.Name
import org.ornate.annotation.Param
import org.ornate.extension.*
import org.ornate.testdata.ObjectFactory.mockOrnateWebElement
import org.ornate.testdata.ObjectFactory.mockWebElement

class FindByWithNameTest {
    private lateinit var parent: WebElement

    private lateinit var ornate: Ornate

    @Before
    fun createParent() {
        parent = mockWebElement()
        ornate = Ornate()
            .extension(ToStringMethodExtension())
            .extension(FindByExtension())
            .extension(FindByCollectionExtension())
    }

    @Test
    fun shouldUseTemplateInNameAnnotationForElement() {
        val ornateWebElement: ParentElement = ornate.create(parent, ParentElement::class.java)
        assertThat(ornateWebElement.childWithName(PARAM_VALUE).toString()).isEqualTo(FINAL_ELEMENT_NAME)
    }

    @Test
    fun shouldUseTemplateInNameAnnotationForElementsCollection() {
        val ornateWebElement: ParentElement = ornate.create(parent, ParentElement::class.java)
        assertThat(ornateWebElement.childListWithName(PARAM_VALUE).toString()).isEqualTo(FINAL_ELEMENT_NAME)
    }

    @Test
    fun shouldUseTemplateInNameAnnotationForCollectionElement() {
        val ornateWebElement: ParentElement = ornate.create(parent, ParentElement::class.java)
        Mockito.`when`(parent.findElements(By.xpath(FINAL_SELECTOR))).thenReturn(listOf(mockOrnateWebElement()))
        assertThat(ornateWebElement.childListWithName(PARAM_VALUE)[0].toString())
            .isEqualTo(listElementName())
    }

    internal interface ParentElement : OrnateWebElement<WebElement> {
        @Name(ELEMENT_NAME)
        @FindBy(SELECTOR)
        fun childWithName(@Param(PARAM) id: String?): OrnateWebElement<WebElement>

        @Name(ELEMENT_NAME)
        @FindBy(SELECTOR)
        fun childListWithName(@Param(PARAM) id: String?): ElementsCollection<OrnateWebElement<WebElement>>
    }

    companion object{
        private fun listElementName() ="$FINAL_ELEMENT_NAME [0]"
        private const val PARAM = "id"
        private const val PARAM_VALUE = "val"
        private const val SELECTOR = "//*[@id=\"{{ $PARAM }}\"]"
        private const val FINAL_SELECTOR = "//*[@id=\"$PARAM_VALUE\"]"
        private const val ELEMENT_NAME = "element {{ $PARAM }}"
        private const val FINAL_ELEMENT_NAME = "element $PARAM_VALUE"
    }
}