package org.ornate

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.doThrow

class TargetMethodTest {
    private lateinit var origin: Origin
    private lateinit var sayHello: SayHello

    @Before
    fun setUp() {
        sayHello = Mockito.mock(SayHello::class.java)
        origin = Ornate().create(sayHello, Origin::class.java)
    }

    @Test
    fun shouldExecuteTargetMethod() {
        origin.hello()
        Mockito.verify(sayHello, Mockito.times(1))!!.hello()
    }

    @Test(expected = RuntimeException::class)
    fun shouldPropagateExceptionInTargetMethod() {
        doThrow(RuntimeException::class.java).`when`(sayHello)!!.hello()
        origin.hello()
    }


    internal interface Origin : SayHello

    internal interface SayHello {
        fun hello()
    }
}