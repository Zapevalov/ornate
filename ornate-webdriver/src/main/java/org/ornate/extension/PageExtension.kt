package org.ornate.extension

import org.apache.http.client.utils.URIBuilder
import org.openqa.selenium.WrapsDriver
import org.ornate.Ornate
import org.ornate.api.MethodExtension
import org.ornate.exceptions.OrnateException
import org.ornate.internal.Configuration
import org.ornate.util.MethodInfo
import org.ornate.util.MethodInfoUtils.getPathSegmentValues
import org.ornate.util.MethodInfoUtils.getQueryMapValues
import org.ornate.util.MethodInfoUtils.getQueryValues
import org.ornate.util.MethodInfoUtils.processTemplate
import org.ornate.util.ReflectionUtils.isAnnotated
import java.lang.reflect.Method
import java.net.URISyntaxException

/**
 * Extension for methods with [Page] annotation.
 */
class PageExtension : MethodExtension {
    override fun test(method: Method): Boolean {
        return isAnnotated(method, Page::class.java)
    }

    override fun invoke(
        proxy: Any?,
        methodInfo: MethodInfo,
        config: Configuration
    ): Any {
        val method = methodInfo.method
        val wrapsDriver = if (proxy is WrapsDriver) proxy else throw OrnateException("Proxy is not WrapsDriver")
        val args: Array<Any> = methodInfo.getArgs().filterNotNull()
            .toTypedArray()
//            .takeIf { it.isNotEmpty() }
//            ?: throw OrnateException("Method ${method.name} doesn't have arguments ")

        defineBaseUrl(method, args).also { if (it.isNotEmpty()) wrapsDriver.wrappedDriver[it]  }

        return Ornate(config).create(wrapsDriver.wrappedDriver, method.returnType as Class<*>)
    }

    /**
     * Build full completed URL.
     *
     * @param baseURI         [String] - the base URI of WebSite.
     * @param pathSegment     [String] - the path segment.
     * @param queryParameters [Map] - the query parameters.
     * @return [String]
     */
    private fun buildUrl(baseURI: String, pathSegment: String, queryParameters: Map<String, String>): String {
        val urlBuilder: URIBuilder
        try {
            urlBuilder = URIBuilder(baseURI)
            urlBuilder.path = pathSegment
            queryParameters.forEach { (param: String?, value: String?) ->
                urlBuilder.addParameter(param, value)
            }
        } catch (e: URISyntaxException) {
            throw OrnateException("Can't parse base URL of your WebSite address", e)
        }
        return urlBuilder.toString()
    }

    private fun defineBaseUrl(method: Method, args: Array<Any>): String {
        val baseURL = method.getAnnotation(Page::class.java).url
        val baseURI = System.getProperties().getProperty(ORNATE_WEBSITE_URL)
            ?: throw OrnateException("URI webSite doesn't declare.")

        val pathParameters: Map<String, String> = getPathSegmentValues(method, args)
        val pathSegment: String = processTemplate(baseURL, pathParameters, "{", "}")
        val queryParameters = getQueryValues(method, args).plus(getQueryMapValues(method, args))

        val resultUrl = buildUrl(baseURI, pathSegment, queryParameters)

        return if (resultUrl != baseURI + DEFAULT_PATH) resultUrl else ""
    }

    companion object {
        private const val DEFAULT_PATH = "/"
        private const val ORNATE_WEBSITE_URL = "ORNATE_WEBSITE_URL"
    }
}