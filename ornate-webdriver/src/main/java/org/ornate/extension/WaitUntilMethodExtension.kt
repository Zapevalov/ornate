package org.ornate.extension

import org.hamcrest.Matcher
import org.hamcrest.StringDescription
import org.ornate.api.MethodExtension
import org.ornate.exception.WaitUntilException
import org.ornate.internal.Configuration
import org.ornate.util.MethodInfo
import java.lang.reflect.Method

/**
 * WaitUntil method extension for [OrnateWebElement].
 */
class WaitUntilMethodExtension : MethodExtension {
    override fun test(method: Method) = method.name == WAIT_UNTIL

    override fun invoke(
        proxy: Any?,
        methodInfo: MethodInfo,
        config: Configuration
    ): Any? {
        val message: String = methodInfo.getParameter(String::class.java) ?: ""
        val matcher: Matcher<*> =
            methodInfo.getParameter(Matcher::class.java) ?: throw IllegalStateException("Unexpected method signature")

        if (!matcher.matches(proxy)) {
            val description = StringDescription()
            description.appendText(message)
                .appendText("\nExpected: ")
                .appendDescriptionOf(matcher)
                .appendText("\n     but: ")

            matcher.describeMismatch(proxy, description)
            throw WaitUntilException(description.toString())
        }
        return proxy
    }

    companion object {
        private const val WAIT_UNTIL = "waitUntil"
    }
}