package org.ornate.internal

import org.ornate.api.Listener
import org.ornate.util.MethodInfo
import org.slf4j.LoggerFactory
import java.lang.Exception
import java.util.*

/**
 * Listener notifier.
 */
class ListenerNotifier : Listener {
    private val listeners: MutableList<Listener> = ArrayList()

    //раньше тут было  vararg
    fun addListeners( listeners: List<Listener>) {
        this.listeners.addAll(listeners)
    }

    override fun beforeMethodCall(methodInfo: MethodInfo?, configuration: Configuration?) {
        for (listener in listeners) {
            try {
                listener.beforeMethodCall(methodInfo, configuration)
            } catch (e: Exception) {
                LOGGER.error("Error during listener {} beforeMethodCall", listener, e)
            }
        }
    }

    override fun afterMethodCall(methodInfo: MethodInfo?, configuration: Configuration?) {
        for (listener in listeners) {
            try {
                listener.afterMethodCall(methodInfo, configuration)
            } catch (e: Exception) {
                LOGGER.error("Error during listener {} afterMethodCall", listener, e)
            }
        }
    }

    override fun onMethodReturn(methodInfo: MethodInfo?, configuration: Configuration?, returned: Any?) {
        for (listener in listeners) {
            try {
                listener.onMethodReturn(methodInfo, configuration, returned)
            } catch (e: Exception) {
                LOGGER.error("Error during listener {} onMethodReturn", listener, e)
            }
        }
    }

    override fun onMethodFailure(
        methodInfo: MethodInfo?, configuration: Configuration?,
        throwable: Throwable?
    ) {
        for (listener in listeners) {
            try {
                listener.onMethodFailure(methodInfo, configuration, throwable)
            } catch (e: Exception) {
                LOGGER.error("Error during listener {} onMethodFailure", listener, e)
            }
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(ListenerNotifier::class.java)
    }
}