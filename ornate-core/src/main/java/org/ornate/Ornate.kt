package org.ornate

import org.ornate.api.*
import org.ornate.context.TargetContext
import org.ornate.internal.OrnateMethodHandler
import org.ornate.internal.Configuration
import org.ornate.internal.ListenerNotifier
import org.ornate.internal.TargetMethodInvoker
import org.ornate.target.HardcodedTarget
import org.ornate.util.ReflectionUtils.getMethods
import java.lang.reflect.Method
import java.lang.reflect.Proxy

class Ornate @JvmOverloads constructor(private val configuration: Configuration = Configuration()) {

    fun listener(listener: Listener): Ornate {
        configuration.registerExtension(listener)
        return this
    }

    fun extension(methodExtension: MethodExtension): Ornate {
        configuration.registerExtension(methodExtension)
        return this
    }

    fun context(context: Context<*>): Ornate {
        configuration.registerContext(context)
        return this
    }

    fun <T> create(ornateTarget: OrnateTarget, type: Class<T>): T {
        val invokers: MutableMap<Method, MethodInvoker> = HashMap()
        val methods: List<Method> = getMethods(type, Any::class.java)

        context(TargetContext(ornateTarget))


        methods.forEach { method: Method ->
            val invoker: MethodInvoker = configuration.getExtensions(MethodExtension::class.java)
                .filter { extension -> extension.test(method) }
                .map { obj: Any -> MethodInvoker::class.java.cast(obj) }
                .getOrElse(0) { TargetMethodInvoker() }

            invokers[method] = invoker
        }

        val notifier = ListenerNotifier()
        configuration.getExtensions(Listener::class.java).forEach { notifier.addListeners(listOf(it)) }

        return Proxy.newProxyInstance(
            type.classLoader,
            arrayOf<Class<*>>(type),
            OrnateMethodHandler(configuration, notifier, invokers)
        ) as T
    }

    fun <T> create(name: String, target: Any, type: Class<T>): T {
        return create(HardcodedTarget(name, target), type)
    }

    fun <T> create(target: Any, type: Class<T>): T {
        return create(type.simpleName, target, type)
    }

}