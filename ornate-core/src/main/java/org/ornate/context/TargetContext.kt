package org.ornate.context

import org.ornate.api.Context
import org.ornate.api.OrnateTarget

/**
 * Target context
 * helps to create Retry instance as context
 */
class TargetContext(private val ornateTarget: OrnateTarget) : Context<OrnateTarget?> {
    override fun getValue() = ornateTarget
}