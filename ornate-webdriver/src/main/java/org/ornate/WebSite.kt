package org.ornate

import org.openqa.selenium.SearchContext
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WrapsDriver
import org.ornate.annotation.DriverProvider

/**
 * Web Site.
 */
interface WebSite : WrapsDriver, SearchContext {
    @DriverProvider
    override fun getWrappedDriver(): WebDriver
}