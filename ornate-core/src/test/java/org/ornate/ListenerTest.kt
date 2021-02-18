package org.ornate

import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.ornate.api.Listener

class ListenerTest {

    @Test
    fun shouldFireListenerMethods() {
        val helloMockitoClass = Mockito.mock(SimpleSayHello::class.java)
        val listenerMockitoClass: Listener = Mockito.mock(Listener::class.java)
        val proxyHello: SimpleSayHello = Ornate()
            .listener(listenerMockitoClass)
            .create(helloMockitoClass, SimpleSayHello::class.java)

        proxyHello.hello()

        Mockito.verify(listenerMockitoClass, Mockito.times(1))
            .beforeMethodCall(ArgumentMatchers.any(), ArgumentMatchers.any())
        Mockito.verify(listenerMockitoClass, Mockito.times(1))
            .afterMethodCall(ArgumentMatchers.any(), ArgumentMatchers.any())
        Mockito.verify(listenerMockitoClass, Mockito.times(1))
            .onMethodReturn(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any())
        Mockito.verify(listenerMockitoClass, Mockito.times(0))
            .onMethodFailure(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any())
    }

    interface SimpleSayHello {
        fun hello() {}
    }
}