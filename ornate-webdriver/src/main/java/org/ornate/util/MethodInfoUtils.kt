package org.ornate.util

import org.ornate.annotation.*
import org.ornate.exceptions.OrnateException
import org.ornate.util.ReflectionUtils.isAnnotated
import java.lang.reflect.Method


object MethodInfoUtils {
    /**
     * Replace string template with value from [Param].
     *
     * @param template   [String] - the string template.
     * @param parameters [Map] - the method's parameters.
     * @return - transformed value.
     *
     * Example:
     * <pre>`value `}]")
     * ornateWebElement childWithName(@Param("value") String value);
     * }</pre>
     *
     * where "//div[{{ value }}]" - the template, value - the method's parameter.
     */
    fun processParamTemplate(template: String, parameters: Map<String, String?>): String {
        return processTemplate(template, parameters, "{{ ", " }}")
    }

    /**
     * Replace string template with special value.
     *
     * @param template   [String] - the string template of.
     * @param parameters [Map] - parameters.
     * @param prefix     [CharSequence] - the sequence of characters to be used at the beginning
     * @param suffix     [CharSequence] - the sequence of characters to be used at the end
     * @return - transformed value.
     */
    fun processTemplate(
        template: String, parameters: Map<String, String?>,
        prefix: CharSequence, suffix: CharSequence
    ): String {
        return parameters.entries.fold(
            template,
            { acc, (a, b) -> acc.replace(prefix.toString() + a + suffix.toString(), b!!) }
        )
    }

//    fun getQueryMapValues(method: Method, args: Array<Any>): Map<String, String> {
//        val queryPredicate =
//            IntPredicate { index: Int -> isAnnotated(method.parameters[index], QueryMap::class.java) }
//        return IntStream.range(0, method.parameterCount)
//            .filter(queryPredicate)
//            .boxed()
//            .map { index: Int? ->
//                args[index!!] as HashMap<String?, String?>
//            }
//            .flatMap<Map.Entry<String?, String?>> { x: HashMap<String?, String?> -> x.entries.stream() }
//            .collect(Collectors.toMap({x -> x.key},{x -> x.value})).map { (k,v) -> k!! to v!! }.toMap()
//
//    }

    fun getQueryMapValues(method: Method, args: Array<Any>): Map<String, String> {

        val filter = method.parameters
            .foldIndexed(arrayListOf<Any?>(), { index, acc, parameter ->
                if (isAnnotated(parameter, QueryMap::class.java)) {
                    acc.add(args[index])
                    acc
                } else {
                    acc
                }
            }).flatMap { (it as Map<String, String>).entries }

        return filter.map { (k, v) -> k to v }.toMap()

    }

    /**
     * Get parameters name with annotation [Query] and all argument values.
     *
     * @param method [Method] - the method on action.
     * @param args - method arguments.
     * @return [Map] return ParamName as Map Key, argument value as Map Value.
     */
    fun getQueryValues(method: Method, args: Array<Any>) = method.parameters
        .foldIndexed(mutableMapOf<String, String>(), { index, acc, parameter ->
            if (isAnnotated(parameter, Query::class.java)) {
                acc[parameter.getAnnotation(Query::class.java).value] = args[index].toString()
                acc
            }
            else
                acc
        })
        .toMap()

    /**
     * Get parameters name with annotation [Path] and all argument values.
     *
     * @param method [Method] - the method on action.
     * @param args - method arguments.
     * @return [Map] return ParamName as Map Key, argument value as Map Value.
     */
    fun getPathSegmentValues(method: Method, args: Array<Any>) = method.parameters
        .filter { isAnnotated(it, Path::class.java) }
        .mapIndexed { index, parameter ->
            parameter.getAnnotation(Path::class.java).value to args[index].toString()
        }
        .toMap()

    /**
     * Get parameters name with annotation [Param] and all argument values.
     *
     * @param method [Method] - the method on action.
     * @param args - method arguments.
     * @return [Map] return ParamName as Map Key, argument value as Map Value.
     */
    fun getParamValues(method: Method, args: Array<Any?>) =
        method.parameters
            .filter { isAnnotated(it, Param::class.java) }
            .mapIndexed { index, parameter ->
                parameter.getAnnotation(Param::class.java).value to args[index].toString()
            }
            .toMap()

    fun getMethodInformation(methodInfo: MethodInfo): Triple<Method, String, Map<String, String>> {
        val method: Method = methodInfo.method
        val parameters = getParamValues(method, methodInfo.getArgs())
        val name: String = method.getAnnotation(Name::class.java)
            ?.let { processParamTemplate(it.value, parameters) } ?: method.name

        return Triple(method, name, parameters)
    }

    fun getFindBy(method: Method): FindBy {
        return if (method.isAnnotationPresent(FindBy::class.java))
            method.getAnnotation(FindBy::class.java)
        else throw OrnateException(
            "Method ${method.name} does not have an annotation [@FindBy]"
        )
    }
}
