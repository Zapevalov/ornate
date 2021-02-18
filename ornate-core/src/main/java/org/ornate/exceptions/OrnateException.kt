package org.ornate.exceptions

class OrnateException: RuntimeException {
    constructor(message: String): super(message)
    constructor(): super()
    constructor(message: String, cause: Exception): super(message, cause)
}