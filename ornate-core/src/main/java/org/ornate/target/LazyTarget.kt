package org.ornate.target

import org.ornate.api.OrnateTarget
import java.util.function.Supplier


class LazyTarget(private val name: String, private val supplier: Supplier<Any>) : OrnateTarget {
    override fun name(): String {
        return name
    }

    override fun instance(): Any {
        return supplier.get()
    }
}