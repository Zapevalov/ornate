package org.ornate.api

import java.lang.reflect.Method
import java.util.function.Predicate

interface MethodExtension: Predicate<Method>, MethodInvoker, Extension {
}