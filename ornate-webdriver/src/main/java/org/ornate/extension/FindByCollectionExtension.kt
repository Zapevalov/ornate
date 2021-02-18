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
import org.ornate.target.HardcodedTarget
import org.ornate.target.LazyTarget
import org.ornate.util.MethodInfo
import org.ornate.util.MethodInfoUtils.getFindBy
import org.ornate.util.MethodInfoUtils.getMethodInformation
import org.ornate.util.MethodInfoUtils.processParamTemplate
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.*

/**
 * Extension for methods with [FindBy] annotation
 * and [ElementsCollection] return type.
 */
class FindByCollectionExtension : MethodExtension {
    override fun test(method: Method): Boolean {
        return (method.isAnnotationPresent(FindBy::class.java)
                && MutableList::class.java.isAssignableFrom(method.returnType))
    }

    override fun invoke(
        proxy: Any?,
        methodInfo: MethodInfo,
        config: Configuration
    ): Any {
        val (method, name, parameters) = getMethodInformation(methodInfo)
        val context = if (proxy is SearchContext) proxy else throw OrnateException("proxy is not SearchContext")
        val childConfiguration: Configuration = config.child()
        val findBy = getFindBy(method)

        val elementsTarget = LazyTarget(name) {
            val methodReturnType = (method.genericReturnType as ParameterizedType)
                .actualTypeArguments[0]

            val clazz:Class<*> = when(methodReturnType) {
                is ParameterizedType -> methodReturnType.rawType as Class<*>
                else -> methodReturnType as Class<*>
            }

            val by: By? = findBy.selector.buildBy(processParamTemplate(findBy.value, parameters))
            val dfdf: List<WebElement> = context.findElements(by) as List<WebElement>

            var dfdf1: ArrayList<Any>? = null
            for (i in dfdf.indices){
                val ornateTarget: OrnateTarget = HardcodedTarget("$name [$i]", dfdf[i])
                dfdf1 = arrayListOf(Ornate(childConfiguration).create(ornateTarget, clazz))
            }

            dfdf1!!
        }

        methodInfo.method.getAnnotation(Retry::class.java)?.let {
            childConfiguration.registerContext(RetryerContext(DefaultRetryer(it)))
        }

        return Ornate(childConfiguration).create(elementsTarget, method.returnType)
    }
}