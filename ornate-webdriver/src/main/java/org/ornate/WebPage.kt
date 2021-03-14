package org.ornate

import org.openqa.selenium.SearchContext
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WrapsDriver
import org.ornate.annotation.DriverProvider

/**
 * Web Page.
 */
interface WebPage : WrapsDriver, SearchContext {
    @DriverProvider
    override fun getWrappedDriver(): WebDriver

    @JvmDefault
    fun open(url: String) = wrappedDriver[url]

    //TODO take out %ORNATE_WEBSITE_URL% to config
    @JvmDefault
    fun open() = wrappedDriver[System.getProperties().getProperty("ORNATE_WEBSITE_URL")]

}