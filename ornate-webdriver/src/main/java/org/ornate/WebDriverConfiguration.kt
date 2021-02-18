package org.ornate

import org.openqa.selenium.WebDriver
import org.ornate.context.RetryerContext
import org.ornate.context.WebDriverContext
import org.ornate.extension.*
import org.ornate.internal.Configuration
import org.ornate.internal.DefaultMethodExtension
import org.ornate.internal.EmptyRetryer

/**
 * WebDriver configuration.
 */
class WebDriverConfiguration(webDriver: WebDriver) : Configuration() {
    constructor(webDriver: WebDriver, baseUrl: String?) : this(webDriver) {
        System.getProperties().setProperty("ORNATE_WEBSITE_URL", baseUrl)
    }

    init {
        registerContext(WebDriverContext(webDriver))
        registerContext(RetryerContext(EmptyRetryer()))
        registerExtension(DriverProviderExtension())
        registerExtension(DefaultMethodExtension())
        registerExtension(FindByExtension())
        registerExtension(FindByCollectionExtension())
        registerExtension(ShouldMethodExtension())
        registerExtension(WaitUntilMethodExtension())
        registerExtension(WrappedElementMethodExtension())
        registerExtension(ExecuteJScriptMethodExtension())
        registerExtension(PageExtension())
        registerExtension(FilterCollectionExtension())
        registerExtension(ToStringMethodExtension())
        registerExtension(ExtractMethodExtension())
    }
}