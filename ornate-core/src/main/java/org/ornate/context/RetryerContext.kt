package org.ornate.context

import org.ornate.api.Context
import org.ornate.internal.Retryer

/**
 * Retry context.
 */
class RetryerContext(private val value: Retryer) : Context<Retryer?> {
    override fun getValue() = value
}