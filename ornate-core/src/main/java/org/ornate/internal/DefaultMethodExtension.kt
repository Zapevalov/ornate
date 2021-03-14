package org.ornate.internal

import org.ornate.api.MethodExtension
import org.ornate.util.MethodInfo
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType
import java.lang.reflect.Method

/**
 * DefaultMethodExtension
 *
 * Kotlin does not work with [default] methods, but
 * framework compiles interfaces to bytecode with helping key [-Xjvm-default=all] and gets necessary for us java 11 code
 * Then Ornate checks methods of Interface and if method has annotation [JvmDefault]
 * Ornate invokes default method in interfaces through [MethodHandles.lookup]
 */
class DefaultMethodExtension : MethodExtension {
    override fun test(method: Method): Boolean {
        return method.isAnnotationPresent(JvmDefault::class.java)
    }

    override operator fun invoke(
        proxy: Any,
        methodInfo: MethodInfo,
        config: Configuration
    ): Any? {
        val declaringClass: Class<*> = methodInfo.method.declaringClass
        val parameterTypes: Array<Class<*>?> =
            methodInfo.method.parameters.map { it.type }.toTypedArray().takeIf { it.isNotEmpty() }
                ?: arrayOfNulls(0)


        val bindTo: MethodHandle = MethodHandles.lookup()
            .findSpecial(
                declaringClass,
                methodInfo.method.name,
                MethodType.methodType(methodInfo.method.returnType, parameterTypes),
                declaringClass
            ).bindTo(proxy)
        return if (methodInfo.getArgs().isEmpty()) {
            bindTo.invoke()
        } else {
            bindTo.invokeWithArguments(*methodInfo.getArgs())
        }
    }
}