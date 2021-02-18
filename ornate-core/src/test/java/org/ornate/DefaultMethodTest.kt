package org.ornate

import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.ornate.exceptions.OrnateException
import org.ornate.internal.DefaultMethodExtension

class DefaultMethodTest {
    private lateinit var ornate: Ornate
    @Before
    fun initOrnate() {
        ornate = Ornate()
            .extension(DefaultMethodExtension())
    }

    @Test
    fun shouldExecuteDefaultVoidMethod() {
        val instance: InterfaceWithDefaultVoidMethod = ornate
            .create(Any(), InterfaceWithDefaultVoidMethod::class.java)
        instance.doSomething()
    }

    @Test
    fun shouldExecuteDefaultStringMethod() {
        val instance: InterfaceWithDefaultStringMethod = ornate
            .create(Any(), InterfaceWithDefaultStringMethod::class.java)
        assert(instance.doSomething() == "doSomething()")
    }

    @Test(expected = OrnateException::class)
    fun shouldPropagateExceptionInDefaultMethod() {
        val instance: InterfaceWithDefaultMethodThrowable = ornate
            .create(Any(), InterfaceWithDefaultMethodThrowable::class.java)
        instance.doSomething()
    }

    @Test
    fun shouldExecuteDefaultVoidWithStringMethod() {
        val instance: InterfaceWithDefaultVoidWithStringMethod = ornate
            .create(Any(), InterfaceWithDefaultVoidWithStringMethod::class.java)
        instance.doSomething("doSomething(string: String)")
    }

    interface InterfaceWithDefaultVoidMethod {
        @JvmDefault
        fun doSomething() {
            println("doSomething")
        }
    }

    interface InterfaceWithDefaultVoidWithStringMethod {
        @JvmDefault
        fun doSomething(string: String) {
            println(string)
        }
    }

    interface InterfaceWithDefaultMethodThrowable {
        @JvmDefault
        fun doSomething(){
            throw OrnateException("")
        }
    }

    interface InterfaceWithDefaultStringMethod {
        @JvmDefault
        fun doSomething() = "doSomething()"
    }
}