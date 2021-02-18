package org.ornate.extension

import org.openqa.selenium.By
import org.openqa.selenium.SearchContext
import org.openqa.selenium.WebElement
import org.ornate.Ornate
import org.ornate.api.MethodExtension
import org.ornate.api.OrnateTarget
import org.ornate.api.Retry
import org.ornate.context.RetryerContext
import org.ornate.exceptions.OrnateException
import org.ornate.internal.Configuration
import org.ornate.internal.DefaultRetryer
import org.ornate.internal.Retryer
import org.ornate.target.LazyTarget
import org.ornate.util.MethodInfo
import org.ornate.util.MethodInfoUtils.getFindBy
import org.ornate.util.MethodInfoUtils.getMethodInformation
import org.ornate.util.MethodInfoUtils.getParamValues
import org.ornate.util.MethodInfoUtils.processParamTemplate
import java.lang.reflect.Method
import java.util.*
import java.util.function.Consumer

/**
 * Extension for methods with [FindBy] annotation.
 */
class FindByExtension : MethodExtension {
    override fun test(method: Method): Boolean {
        return (method.isAnnotationPresent(FindBy::class.java)
                && WebElement::class.java.isAssignableFrom(method.returnType))
    }

    override fun invoke(
        proxy: Any?,
        methodInfo: MethodInfo,
        config: Configuration
    ): Any {
        val (method, name, parameters) = getMethodInformation(methodInfo)
        val findBy = getFindBy(method)

        val by: By? = findBy.selector.buildBy(processParamTemplate(findBy.value, parameters))
        val context = if (proxy is SearchContext) proxy else throw OrnateException("proxy is not SearchContext")

        val childConfiguration: Configuration = config.child()
        methodInfo.method.getAnnotation(Retry::class.java)?.let {
            childConfiguration.registerContext(RetryerContext(DefaultRetryer(it)))
        }
        val elementTarget: OrnateTarget = LazyTarget(name) { context.findElement(by) }
        return Ornate(childConfiguration)
            .create(elementTarget, method.returnType)
    }
}