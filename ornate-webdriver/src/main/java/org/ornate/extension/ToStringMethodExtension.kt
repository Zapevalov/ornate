package org.ornate.extension

import org.ornate.api.MethodExtension
import org.ornate.context.TargetContext
import org.ornate.internal.Configuration
import org.ornate.util.MethodInfo
import java.lang.reflect.Method

/**
 * ToString method extension
 * returns either [Name] annotation value or method name
 */
class ToStringMethodExtension : MethodExtension {
    override fun test(method: Method) = method.name == TO_STRING

    override fun invoke(
        proxy: Any,
        methodInfo: MethodInfo,
        config: Configuration
    ) = config.requireContext(TargetContext::class.java).getValue().name()

    companion object {
        private const val TO_STRING = "toString"
    }
}