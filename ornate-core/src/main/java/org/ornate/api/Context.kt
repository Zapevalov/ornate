package org.ornate.api

/**
 * Main context.
 * @param <T> returned value.
 */
interface Context<T> : Extension{
    /**
     * @return the context value.
     */
    fun getValue(): T
}