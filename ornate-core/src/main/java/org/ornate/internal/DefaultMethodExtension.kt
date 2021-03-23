package org.ornate.internal

import org.ornate.api.MethodExtension
import org.ornate.util.MethodInfo
import java.lang.invoke.MethodHandles
import java.lang.reflect.Method
import java.lang.reflect.Parameter

/**
 * DefaultMethodExtension
 *
 * Kotlin compiles methods with implementations through
 * [DefaultImpls] static subclass. And here i execute this subclass
 * and invoke [@method]
 */
class DefaultMethodExtension : MethodExtension {
    override fun test(method: Method): Boolean {
        return method.declaringClass.declaredClasses.any { it.name.contains("DefaultImpls") }
    }

    override fun invoke(proxy: Any, methodInfo: MethodInfo, config: Configuration): Any? {
        val declaringClass: Class<*> = methodInfo.method.declaringClass


        val defaultImplClass = declaringClass.declaredClasses.first { it.name.contains("DefaultImpls") }
        val method = defaultImplClass.methods
            .filter { it.name == methodInfo.method.name }
            .filter { it.parameterCount == methodInfo.getArgs().size + 1 }
            .first { method -> getSomething(declaringClass, method.parameters, methodInfo.getArgs()) }

        return if (methodInfo.getArgs().isNotEmpty())
            method?.invoke(defaultImplClass, proxy, *methodInfo.getArgs())
        else
            method?.invoke(defaultImplClass, proxy)
    }

    private fun getSomething(clazz: Class<*>, parameters: Array<Parameter>, args: Array<Any?>): Boolean {
        val params = parameters.filter { it.type.name != clazz.name}
        return params.map { it.type.name } == args.map { it?.javaClass?.typeName }
    }
}