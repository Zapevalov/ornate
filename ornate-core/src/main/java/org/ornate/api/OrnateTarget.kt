package org.ornate.api

/**
 * name - just string. In general is className of interface what we should implement in "instance"
 * instance - some ready Object
 */
interface OrnateTarget {
    fun name(): String?
    fun instance(): Any?
}