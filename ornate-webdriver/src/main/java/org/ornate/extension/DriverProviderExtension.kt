package org.ornate.extension

import org.openqa.selenium.WebDriver
import org.ornate.annotation.DriverProvider
import org.ornate.api.MethodExtension
import org.ornate.context.WebDriverContext
import org.ornate.internal.Configuration
import org.ornate.util.MethodInfo
import java.lang.reflect.Method

/**
 * Get webDriver from context
 */
class DriverProviderExtension : MethodExtension {
    override fun test(method: Method): Boolean {
        return method.isAnnotationPresent(DriverProvider::class.java)
    }

    override fun invoke(proxy: Any, methodInfo: MethodInfo, config: Configuration): WebDriver? {
        return config.getContext(WebDriverContext::class.java)?.getValue()
    }
}