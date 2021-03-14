package org.ornate.util

import java.lang.reflect.Method

/**
 * MethodInfo
 *
 * collect method information
 */
class MethodInfo(val method: Method, args: Array<Any?>?) {
    private val args: Array<Any?>

    fun getArgs(): Array<Any?> {
        return checkArgs(args)
    }

    /**
     * @return parameter like type corresponding object [type]
     */
    fun <T> getParameter(type: Class<T>): T? {
        val parameters = method.parameters
        var result: T? = null
        for (i in parameters.indices) {
            val parameter = parameters[i]
            if (parameter.type == type) {
                result = type.cast(args[i])
            }
        }
        return result
    }

    /**
     * @return parameter like type corresponding object [type] that has [annotation]
     */
    fun <T> getParameter(type: Class<T>, annotation: Class<out Annotation>): T? {
        val parameters = method.parameters
        var result: T? = null
        for (i in parameters.indices) {
            val parameter = parameters[i]
            if (parameter.type == type && parameter.isAnnotationPresent(annotation)) {
                result = type.cast(args[i])
            }
        }
        return result
    }

    private fun checkArgs(args: Array<Any?>?): Array<Any?> {
        return if (args.isNullOrEmpty()) arrayOf() else args
    }

    init {
        this.args = checkArgs(args)
    }
}