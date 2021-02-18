package org.ornate.internal

import net.bytebuddy.ByteBuddy
import net.bytebuddy.ClassFileVersion
import net.bytebuddy.dynamic.DynamicType
import org.ornate.api.MethodExtension
import org.ornate.util.MethodInfo
import java.lang.invoke.MethodHandles
import java.lang.reflect.Method

import net.bytebuddy.matcher.ElementMatchers.*
import net.bytebuddy.implementation.DefaultMethodCall
import java.lang.Exception
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodType
import java.lang.reflect.Type
import java.lang.reflect.UndeclaredThrowableException
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType
import kotlin.reflect.jvm.kotlinFunction


class DefaultMethodExtension : MethodExtension {
    override fun test(method: Method): Boolean {
        return method.isAnnotationPresent(JvmDefault::class.java)
    }

    override operator fun invoke(
        proxy: Any?,
        methodInfo: MethodInfo,
        config: Configuration
    ): Any? {
        val declaringClass: Class<*> = methodInfo.method.declaringClass
        val map: Array<Class<*>?> = methodInfo.method.parameters.map { it.type }.toTypedArray()
        val parameterTypes: Array<Class<*>?> = if(map.isEmpty()){ arrayOfNulls(0) } else { map }

        val bindTo: MethodHandle = MethodHandles.lookup()
            .findSpecial(
                declaringClass,
                methodInfo.method.name,
                MethodType.methodType(methodInfo.method.returnType, parameterTypes),
                declaringClass
            ).bindTo(proxy)
        return if(methodInfo.getArgs().isEmpty()){
            bindTo.invoke()
        } else {
            bindTo.invokeWithArguments(*methodInfo.getArgs())
        }
    }
}