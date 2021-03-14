package org.ornate.context

import org.ornate.api.Context
import org.ornate.internal.retry.Retryer

/**
 * Retry context
 * helps to create Retry instance as context
 */
class RetryerContext(private val value: Retryer) : Context<Retryer?> {
    override fun getValue() = value
}