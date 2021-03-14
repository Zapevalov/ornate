package org.ornate

import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.openqa.selenium.WebDriver
import org.ornate.annotation.Page
import org.ornate.annotation.Path
import org.ornate.annotation.Query
import org.ornate.annotation.QueryMap
import org.ornate.exceptions.OrnateException
import org.ornate.testdata.ObjectFactory.mockWebDriver

//TODO доделать вызов дефолтной реализации как в Атласе
class PageExtensionTest {
    private lateinit var driver: WebDriver
    private val ornate = "ornate"

    @Before
    fun setUp() {
        driver = mockWebDriver()
    }

    @After
    fun stopDriver() {
        driver.quit()
    }

    @Test
    fun shouldHandleSingleQueryParam() {
        val exceptedURI = "https://github.com/search?abs=%D0%BA%D0%B8%D1%80%D0%B8%D0%BB%D0%BB%D0%B8%D1%86%D0%B0"
        val onSite: TestSite = Ornate(WebDriverConfiguration(driver, "https://github.com"))
            .create(driver, TestSite::class.java)

        onSite.onMainPage("кириллица")
        //onSite.onMainPage("кириллица").ornateWebElement.click()
        Mockito.verify(driver, Mockito.times(1))[exceptedURI]
    }

    @Test
    fun shouldHandleDefaultPathOfPage() {
        `when`(driver.title).thenReturn(ornate)
        val onSite: TestSiteWithDefaultPage = Ornate(WebDriverConfiguration(driver, "https://github.com"))
            .create(driver, TestSiteWithDefaultPage::class.java)
        val title = onSite.onMainPage().wrappedDriver.title
        Assert.assertEquals(ornate, title)
    }

    @Test
    fun shouldHandleDefaultPathWithPathSegment() {
        val onSite: TestSiteWithDefaultPageAndQuery = Ornate(WebDriverConfiguration(driver, "https://github.com"))
            .create(driver, TestSiteWithDefaultPageAndQuery::class.java)
        onSite.onMainPage(ornate, mapOf("first" to "value-1", "second" to "value-2"))
//            .ornateWebElement.sendKeys("Use driver.get(...) and then sendKeys");
        Mockito.verify(driver, Mockito.times(1))["https://github.com/?q=ornate&first=value-1&second=value-2"]
    }

    @Test
    fun shouldHandleQueryParams() {
        val stringCaptor = ArgumentCaptor.forClass(String::class.java)
        val onSite: TestSite = Ornate(WebDriverConfiguration(driver, "https://github.com"))
            .create(driver, TestSite::class.java)
        val queryMap: MutableMap<String, String> = HashMap()
        queryMap["first"] = "value-1"
        queryMap["second"] = "value-2"
        onSite.onMainPage("100", mapOf("first" to "value-1", "second" to "value-2"))
//            .ornateWebElement.sendKeys("Use driver.get(...) and then sendKeys")
        Mockito.verify(driver, Mockito.times(1))[stringCaptor.capture()]
        val capturedStringArg = stringCaptor.value
        MatcherAssert.assertThat(
            capturedStringArg, CoreMatchers.allOf(
                CoreMatchers.containsString("total?"),
                CoreMatchers.containsString("q=100"),
                CoreMatchers.containsString("first=value-1"),
                CoreMatchers.containsString("second=value-2"),
                CoreMatchers.containsString("?")
            )
        )
        Assert.assertEquals(3, capturedStringArg.split("&").toTypedArray().size.toLong())
    }

    @Test(expected = OrnateException::class)
    fun siteWithOutAnyBaseURIShouldThrowException() {
        System.getProperties().clear()
        val siteWithOutUrl: TestSiteWithOutAnyURI = Ornate(WebDriverConfiguration(mockWebDriver()))
            .create(driver, TestSiteWithOutAnyURI::class.java)
        siteWithOutUrl.onMainPage("null")
    }

    @Test
    fun setBaseURIMethodShouldHaveWebSite() {
        val siteWithOutUrl: TestSiteWithOutAnyURI = Ornate(WebDriverConfiguration(driver, "https://github.com"))
            .create(driver, TestSiteWithOutAnyURI::class.java)
        siteWithOutUrl.onMainPage("zero")
        //siteWithOutUrl.onMainPage("zero").ornateWebElement.click();
        Mockito.verify(driver, Mockito.times(1))["https://github.com/search?a=zero"]
    }

    @Test
    fun shouldHandlePathParams() {
        val siteWithPath: TestSiteWithPathParams = Ornate(WebDriverConfiguration(driver, "https://github.com"))
            .create(driver, TestSiteWithPathParams::class.java)

        siteWithPath.onMainPage(1100109, "Ornate", 100, mapOf("first" to "value-1"), mapOf("second" to "value-2"))
        //siteWithPath.onMainPage(1100109, "Ornate", 100, queryMap, queryMap1).ornateWebElement.click();
//        https://github.com/users/1100109/Ornate?q=100&first=value-1&second=value-2
//        https://github.com/users/1100109/Ornate?q=%7Bid%7D&first=value-1&second=value-2"
        Mockito.verify(
            driver,
            Mockito.times(1)
        )["https://github.com/users/1100109/Ornate?q=100&first=value-1&second=value-2"]
    }

    @Test
    fun shouldHandleUrlWithUserAndPassword() {
        val siteWithUserAndPass: TestSiteWithOutAnyURI =
            Ornate(WebDriverConfiguration(driver, "http://username:password@example.com/"))
                .create(driver, TestSiteWithOutAnyURI::class.java)
        siteWithUserAndPass.onMainPage("zero")
        //siteWithUserAndPass.onMainPage("zero").ornateWebElement.click();
        Mockito.verify(driver, Mockito.times(1))["http://username:password@example.com/search?a=zero"]
    }

    @Test
    fun shouldHandleUrlWithPort() {
        val siteWithUserAndPass: TestSiteWithOutAnyURI =
            Ornate(WebDriverConfiguration(driver, "http://example.com:8443/"))
                .create(driver, TestSiteWithOutAnyURI::class.java)
        siteWithUserAndPass.onMainPage("zero")
        Mockito.verify(driver, Mockito.times(1))["http://example.com:8443/search?a=zero"]
    }

    interface TestSiteWithDefaultPageAndQuery : WebSite {
        @Page
        fun onMainPage(@Query("q") value: String?,
                       @QueryMap queryMap: Map<String, String>): MainPage?
    }

    interface TestSite : WebSite {
        @Page(url = "total")
        fun onMainPage(@Query("q") value: String?, @QueryMap queryMap: Map<String?, String?>?): MainPage?

        @Page(url = "search")
        fun onMainPage(@Query("abs") value: String?): MainPage
    }

    interface TestSiteWithPathParams : WebSite {
        @Page(url = "users/{id}/{project}")
        fun onMainPage(
                @Path("id") customerId: Long,
                @Path("project") name: String?,
                @Query("q") value: Int?,
                @QueryMap queryMap: Map<String?, String?>?,
                @QueryMap queryMap1: Map<String?, String?>?
        ): MainPage?
    }

    interface TestSiteWithOutAnyURI : WebSite {
        @Page(url = "search")
        fun onMainPage(@Query("a") value: String?): MainPage?
    }

    interface TestSiteWithDefaultPage : WebSite {
        @Page
        fun onMainPage(): MainPage
    }


    interface MainPage : WebPage
}