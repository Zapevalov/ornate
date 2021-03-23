package org.ornate.extension

import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.ornate.api.MethodExtension
import org.ornate.context.WebDriverContext
import org.ornate.exceptions.OrnateException
import org.ornate.internal.Configuration
import org.ornate.util.MethodInfo
import java.lang.reflect.Method

/**
 * use JS for specific element
 * @proxy - specific element
 * @methodInfo.getArgs()[0] - js command
 */
class ExecuteJScriptMethodExtension : MethodExtension {
    override fun test(method: Method): Boolean {
        return method.name == EXECUTE_SCRIPT
    }

    override fun invoke(
        proxy: Any,
        methodInfo: MethodInfo,
        config: Configuration
    ): Any {
        val driver: WebDriver = config.getContext(WebDriverContext::class.java)?.getValue() ?:
            throw OrnateException("WebDriver is missing")
        (driver as JavascriptExecutor)
            .executeScript(methodInfo.getArgs()[0] as String, proxy)
        return proxy
    }

    companion object {
        private const val EXECUTE_SCRIPT = "executeScript"
    }
}