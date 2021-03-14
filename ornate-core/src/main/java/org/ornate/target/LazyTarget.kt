package org.ornate.target

import org.ornate.api.OrnateTarget
import java.util.function.Supplier

/**
 * LazyTarget
 *
 * use lazy init concept - instance will be get when it will be created, not early
 */
class LazyTarget(private val name: String, private val supplier: Supplier<Any>) : OrnateTarget {
    override fun name(): String {
        return name
    }

    override fun instance(): Any {
        return supplier.get()
    }
}