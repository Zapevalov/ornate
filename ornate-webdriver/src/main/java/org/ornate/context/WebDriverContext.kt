package org.ornate.context

import org.openqa.selenium.WebDriver
import org.ornate.api.Context

class WebDriverContext(val webDriver: WebDriver) : Context<WebDriver?> {
    override fun getValue() = webDriver
}