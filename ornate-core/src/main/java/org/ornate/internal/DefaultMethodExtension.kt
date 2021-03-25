package org.ornate.internal

import org.ornate.api.MethodExtension
import org.ornate.util.MethodInfo
import java.lang.reflect.Method

/**
 * DefaultMethodExtension
 *
 * Kotlin compiles methods with implementations through
 * [DefaultImpls] static subclass. And here i execute this subclass
 * and invoke [@method]
 */
class DefaultMethodExtension : MethodExtension {
    private lateinit var defaultImplClass: Class<*>

    override fun test(method: Method): Boolean {
        if (method.declaringClass.declaredClasses.any { it.name.contains("DefaultImpls") }) {
            this.defaultImplClass = method.declaringClass.declaredClasses.first { it.name.contains("DefaultImpls") }
            return defaultImplClass.methods.map { it.name }.any { it.contains(method.name) }
        }
        return false
    }

    override fun invoke(proxy: Any, methodInfo: MethodInfo, config: Configuration): Any? {
        val method = defaultImplClass.methods
            .filter { it.name == methodInfo.method.name }
            .filter { it.parameterCount == methodInfo.getArgs().size + 1 }
            .first { it.getWithArgs(methodInfo.getArgs()) }

        return if (methodInfo.getArgs().isNotEmpty()) {
            method?.invoke(defaultImplClass, proxy, *methodInfo.getArgs())
        } else {
            method?.invoke(defaultImplClass, proxy)
        }
    }

    //choose methods with parameters type like methodInfo arguments
    private fun Method.getWithArgs(args: Array<Any?>): Boolean{
        if(args.isEmpty() && parameters.size == 1){
            return true
        }

        return parameterTypes.map { it.name }
            .intersect(args.requireNoNulls().map { it.javaClass.typeName} ).size == args.size
    }
}