package org.ornate.api

import org.ornate.internal.Configuration
import org.ornate.util.MethodInfo

/**
 * Listener.
 */
interface Listener : Extension {
    fun beforeMethodCall(methodInfo: MethodInfo?, configuration: Configuration?)
    fun afterMethodCall(methodInfo: MethodInfo?, configuration: Configuration?)
    fun onMethodReturn(methodInfo: MethodInfo?, configuration: Configuration?, returned: Any?)
    fun onMethodFailure(methodInfo: MethodInfo?, configuration: Configuration?, throwable: Throwable?)
}