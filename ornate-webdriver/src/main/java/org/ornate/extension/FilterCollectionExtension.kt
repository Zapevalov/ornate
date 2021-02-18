package org.ornate.extension

import org.ornate.ElementsCollection
import org.ornate.Ornate
import org.ornate.api.MethodExtension
import org.ornate.internal.Configuration
import org.ornate.util.MethodInfo
import java.lang.reflect.Method

/**
 * Filter method extension for [ElementsCollection].
 */
class FilterCollectionExtension : MethodExtension {
    override fun test(method: Method): Boolean {
        return method.name == FILTER && MutableList::class.java.isAssignableFrom(method.declaringClass)
    }

    override fun invoke(
        proxy: Any?,
        methodInfo: MethodInfo,
        config: Configuration
    ): ElementsCollection<*> {

        val predicate = methodInfo.getArgs()[0] as (Any?) -> Boolean

        val filter = (proxy as List<*>).filter(predicate)
        return Ornate(config).create(filter, ElementsCollection::class.java)
    }

    companion object {
        private const val FILTER = "filter"
    }
}