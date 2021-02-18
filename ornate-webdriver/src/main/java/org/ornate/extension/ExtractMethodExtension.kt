package org.ornate.extension

import org.ornate.ElementsCollection
import org.ornate.Ornate
import org.ornate.api.MethodExtension
import org.ornate.internal.Configuration
import org.ornate.util.MethodInfo
import java.lang.reflect.Method
import java.util.function.Function
import java.util.stream.Collectors

class ExtractMethodExtension : MethodExtension {
    override fun test(method: Method): Boolean {
        return method.name == EXTRACT && MutableList::class.java.isAssignableFrom(method.returnType)
    }

    override fun invoke(
        proxy: Any?,
        methodInfo: MethodInfo,
        config: Configuration
    ): Any {
        //TODO тут могут быть проблемы
        //example of argument is RepositoryCard::title
        val converter = methodInfo.getArgs()[0] as Function<Any, Any>
        return Ornate(config.child())
            .create(
                (proxy as List<Any>).stream().map(converter).collect(Collectors.toList()),
                ElementsCollection::class.java
            )
    }

    companion object {
        private const val EXTRACT = "extract"
    }
}