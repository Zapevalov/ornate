package org.ornate

import org.openqa.selenium.WebDriver


object webTest{
    operator fun invoke(init: WebSiteContext.() -> Unit) = WebSiteContext().init()
}

class WebSiteContext {
    inline fun <reified T> onSite(ornate: Ornate, webDriver: WebDriver, init: T.() -> Unit){
        init(ornate.create(webDriver, T::class.java))
    }
}