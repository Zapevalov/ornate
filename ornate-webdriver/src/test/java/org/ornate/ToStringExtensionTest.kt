package org.ornate

import org.apache.commons.lang3.RandomStringUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.openqa.selenium.WebElement
import org.ornate.extension.ToStringMethodExtension
import org.ornate.testdata.ObjectFactory.mockWebElement

class ToStringExtensionTest {
    private lateinit var message: String
    private lateinit var parent: WebElement


    @Before
    fun createParent() {
        message = RandomStringUtils.randomAlphanumeric(10)
        parent = mockWebElement()
    }

    @Test
    fun shouldUseDefaultToStringMethodWithoutExtension() {
        val ornateWebElement = Ornate().create(parent, OrnateWebElement::class.java)
        Mockito.`when`(parent.toString()).thenReturn(message)
        assertThat(ornateWebElement.toString()).isEqualTo(message)
    }

    @Test
    fun shouldUseToStringExtensionMethodName() {
        val ornateWebElement = Ornate()
            .extension(ToStringMethodExtension())
            .create(message, parent, OrnateWebElement::class.java)
        assertThat(ornateWebElement.toString()).isEqualTo(message)
    }
}