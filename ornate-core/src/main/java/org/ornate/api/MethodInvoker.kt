package org.ornate.api

import org.ornate.internal.Configuration
import org.ornate.util.MethodInfo

/**
 * MethodInvoker
 */
interface MethodInvoker {
    @Throws(Throwable::class)
    fun invoke(proxy: Any, methodInfo: MethodInfo, config: Configuration): Any?
}