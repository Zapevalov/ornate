package org.ornate.api

import java.lang.reflect.Method
import java.util.function.Predicate

/**
 * just interface for merger some interfaces
 */
interface MethodExtension: Predicate<Method>, MethodInvoker, Extension