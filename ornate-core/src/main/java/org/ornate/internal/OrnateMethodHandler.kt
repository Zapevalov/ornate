package org.ornate.internal

import org.ornate.api.MethodInvoker
import org.ornate.api.Retry
import org.ornate.api.Timeout
import org.ornate.context.RetryerContext
import org.ornate.internal.retry.CustomRetryer
import org.ornate.internal.retry.DefaultRetryer
import org.ornate.internal.retry.Retryer
import org.ornate.util.MethodInfo
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

/**
 * OrnateMethodHandler
 *
 * Entry point for called method
 * The corresponding invoke method is called depending on the [MethodInvoker] type class
 * by default such class is [TargetMethodInvoker]
 */
class OrnateMethodHandler(
    private val configuration: Configuration,
    private val notifier: ListenerNotifier,
    private val handlers: Map<Method, MethodInvoker>
) : InvocationHandler {
    @Throws(Throwable::class)
    override fun invoke(proxy: Any, method: Method, args: Array<Any?>?): Any? {
        val methodInfo = MethodInfo(method, args)
        notifier.beforeMethodCall(methodInfo, configuration)
        return try {
            val handler = handlers[method]
            val result: Any? = invokeWithRetry(handler, proxy, methodInfo)
            notifier.onMethodReturn(methodInfo, configuration, result)
            result
        } catch (e: Throwable) {
            notifier.onMethodFailure(methodInfo, configuration, e)
            throw e
        } finally {
            notifier.afterMethodCall(methodInfo, configuration)
        }
    }

    private fun invokeWithRetry(
        methodInvoker: MethodInvoker?,
        proxy: Any,
        methodInfo: MethodInfo
    ): Any? {
        val retryAnnotation: Retry? = methodInfo.method.getAnnotation(Retry::class.java)
        val userRetryer: Retryer? = retryAnnotation?.let { Retryer::class.java.cast(CustomRetryer(it)) }

        val retryer: Retryer =
            userRetryer ?: configuration.getContext(RetryerContext::class.java)?.getValue() ?: RetryerContext(
                DefaultRetryer()
            ).getValue()

        methodInfo.getParameter(Int::class.java, Timeout::class.java)?.let {
            retryer.timeoutInSeconds(it)
        }

        return retry(proxy, methodInvoker, methodInfo, retryer)
    }


    private fun retry(proxy: Any, methodInvoker: MethodInvoker?, methodInfo: MethodInfo, retryer: Retryer): Any? {
        var lastException: Throwable
        val start = System.currentTimeMillis()
        do {
            lastException = try {
                return methodInvoker!!.invoke(proxy, methodInfo, configuration)
            } catch (e: Throwable) {
                e
            }
        } while (retryer.shouldRetry(start, lastException))
        throw lastException
    }
}