package org.ornate.extension

import org.ornate.api.MethodExtension
import org.ornate.context.TargetContext
import org.ornate.internal.Configuration
import org.ornate.util.MethodInfo
import java.lang.reflect.Method

/**
 * GetWrappedElement method extension.
 */
class WrappedElementMethodExtension : MethodExtension {
    override fun test(method: Method) = method.name == GET_WRAPPED_ELEMENT

    override fun invoke(
        proxy: Any?,
        methodInfo: MethodInfo,
        config: Configuration
    ) = config.requireContext(TargetContext::class.java).getValue().instance()


    companion object {
        private const val GET_WRAPPED_ELEMENT = "getWrappedElement"
    }
}