package org.ornate.internal

import org.ornate.api.MethodInvoker
import kotlin.Throws
import org.ornate.context.TargetContext
import org.ornate.util.MethodInfo
import org.ornate.util.ReflectionUtils
import java.lang.reflect.InvocationTargetException
import java.util.*

/**
 * Target method invoker.
 */
class TargetMethodInvoker : MethodInvoker {
    @Throws(Throwable::class)
    override fun invoke(proxy: Any?, methodInfo: MethodInfo, config: Configuration): Any? {
        val target: Any = config.requireContext(TargetContext::class.java).getValue().instance()!!
        val targetMethod = ReflectionUtils.getMatchingMethod(
            target.javaClass,
            methodInfo.method.name,
            *getParametersTypes(*methodInfo.getArgs())
        )
        return try {
            targetMethod.isAccessible = true
            val sdfdf = targetMethod.invoke(target, *methodInfo.getArgs())
            sdfdf
        } catch (e: InvocationTargetException) {
            throw e.targetException
        }
    }

    private fun getParametersTypes(vararg args: Any?): Array<Class<*>?> {
        return args.map { obj: Any? -> obj?.javaClass }.toTypedArray()
    }
}