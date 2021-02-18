package org.ornate.util

import org.apache.commons.lang3.ClassUtils
import org.apache.commons.lang3.ClassUtils.isAssignable
import org.apache.commons.lang3.reflect.ConstructorUtils
import org.ornate.exceptions.OrnateException
import java.lang.Exception
import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Method
import java.util.*

/**
 * Reflection utils.
 */
object ReflectionUtils {
    private fun getAllInterfaces(vararg classes: Class<*>): List<Class<*>> {
        return classes.flatMap { ClassUtils.getAllInterfaces(it).plus(it) }
    }

    fun getMethods(vararg clazz: Class<*>): List<Method> {
        return  getAllInterfaces(*clazz).flatMap { it.declaredMethods.asIterable() }
    }

    fun <T> newInstance(clazz: Class<T>): T {
        return try {
            ConstructorUtils.invokeConstructor(clazz)
        } catch (e: Exception) {
            throw OrnateException("Can't instantiate class $clazz", e)
        }
    }

    /**
     * Get matching methods for the given class, superclass and all of interfaces implemented by the given
     * class and its superclasses. Note: first, find method in current class and etc.
     *
     * @param cls            - current Class [Class]
     * @param methodName     - method name [String]
     * @param parameterTypes - parameter types [Class]
     * @return required method [Method]
     */
    fun getMatchingMethod(
        cls: Class<*>, methodName: String,
        vararg parameterTypes: Class<*>?
    ): Method {
        if (cls.equals(null) && Objects.requireNonNull(methodName).isNotEmpty()) {
            throw OrnateException("Null class not allowed.")
        }

        val methods: List<Method> = listOf(cls)
            .plus(ClassUtils.getAllSuperclasses(cls))
            .plus(ClassUtils.getAllInterfaces(cls))
            .flatMap { x: Class<*> -> x.declaredMethods.toList() }


        val filter1: List<Method?> = methods.filter {
            it.name == methodName && parameterTypes.contentDeepEquals(it.parameterTypes)
        }

        val filter2 = methods.filter {
            it.name == methodName && isAssignable(
                parameterTypes,
                it.parameterTypes, true
            )
        }

        return methods
            .filter { it.name == methodName }
            .filter {
                parameterTypes.contentDeepEquals(it.parameterTypes) ||
                        isAssignable(parameterTypes, it.parameterTypes, true)
            }
            .takeIf { it.isNotEmpty() }
            ?.get(0)
            ?: throw OrnateException("Can't find valid method: " + cls.name + "." + methodName)
    }

    /**
     * Check is element have needful annotation.
     *
     * @param element - all class implemented [AnnotatedElement] interface.
     * @param type    [Class] - type of needful annotation
     * @return - true or false.
     */
    fun isAnnotated(element: AnnotatedElement, type: Class<out Annotation>): Boolean {
        return element.isAnnotationPresent(type)
    }
}
