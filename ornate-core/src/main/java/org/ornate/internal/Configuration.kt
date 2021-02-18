package org.ornate.internal

import org.ornate.api.Context
import org.ornate.api.Extension
import org.ornate.exceptions.OrnateException
import java.util.*

/**
 * Ornate configuration.
 */
open class Configuration {
    private val extensions: MutableMap<Class<out Extension?>, Extension>

    fun registerExtension(extension: Extension) {
        extensions[extension.javaClass] = extension
    }

    fun registerContext(context: Context<*>) {
        extensions[context.javaClass] = context
    }

    fun <T : Extension?> getExtensions(extensionType: Class<T>): List<T> {
        return extensions.values
            .filter { extensionType.isInstance(it) }
            .map { extensionType.cast(it) }
    }

    fun <T> getContext(contextType: Class<T>): T? {
        return extensions.values
            .filter { contextType.isInstance(it) }
            .map { contextType.cast(it) }.firstOrNull()
    }

    fun <T> requireContext(contextType: Class<T>): T {
        return getContext(contextType) ?: throw OrnateException("Context not found by type $contextType")
    }

    fun child(): Configuration {
        val configuration = Configuration()
        getExtensions(Extension::class.java).forEach { configuration.registerExtension(it) }
        return configuration
    }

    init {
        extensions = HashMap<Class<out Extension?>, Extension>()
    }
}