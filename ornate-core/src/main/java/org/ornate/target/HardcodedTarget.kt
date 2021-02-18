package org.ornate.target

import org.ornate.api.OrnateTarget

class HardcodedTarget(private val name: String, private val instance: Any?) : OrnateTarget {
    override fun name(): String {
        return name
    }

    override fun instance(): Any? {
        return instance
    }
}