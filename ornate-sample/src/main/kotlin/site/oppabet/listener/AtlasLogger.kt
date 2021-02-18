package site.oppabet.listener

import site.oppabet.listener.AllureLogger.MethodFormatter
import io.qameta.allure.Allure
import io.qameta.allure.model.Status
import io.qameta.allure.model.StepResult
import io.qameta.allure.util.ResultsUtils
import org.hamcrest.Matcher
import java.lang.reflect.Method
import org.ornate.api.*
import org.ornate.context.TargetContext
import org.ornate.internal.Configuration
import org.ornate.util.MethodInfo
import java.util.*

internal class AllureLogger : Listener {
    private val lifecycle = Allure.getLifecycle()
    private val loggableMethods: MutableMap<String, MethodFormatter>

    override fun beforeMethodCall(
        methodInfo: MethodInfo?,
        configuration: Configuration?
    ) {
        getMethodFormatter(methodInfo!!.method).ifPresent { formatter: MethodFormatter ->
            val name: String = configuration!!
                .getContext(TargetContext::class.java)?.getValue()?.name() ?: methodInfo.method.name

            val args = methodInfo.getArgs()
            lifecycle.startStep(
                Objects.toString(methodInfo.hashCode()),
                StepResult().setName(formatter.format(name, args)).setStatus(Status.PASSED)
            )
        }
    }

    override fun afterMethodCall(
        methodInfo: MethodInfo?,
        configuration: Configuration?
    ) {
        getMethodFormatter(methodInfo!!.method)
            .ifPresent { lifecycle.stopStep(Objects.toString(methodInfo.hashCode())) }
    }

    override fun onMethodReturn(
        methodInfo: MethodInfo?,
        configuration: Configuration?,
        returned: Any?
    ) {
    }

    override fun onMethodFailure(
        methodInfo: MethodInfo?,
        configuration: Configuration?,
        throwable: Throwable?
    ) {
        getMethodFormatter(methodInfo!!.method).ifPresent {
            lifecycle.updateStep { stepResult: StepResult ->
                stepResult.status = ResultsUtils.getStatus(throwable).orElse(Status.BROKEN)
                stepResult.statusDetails = ResultsUtils.getStatusDetails(throwable).orElse(null)
            }
        }
    }

    private fun getMethodFormatter(method: Method): Optional<MethodFormatter> {
        return Optional.ofNullable(loggableMethods[method.name])
    }

    private fun interface MethodFormatter {
        fun format(name: String?, args: Array<Any?>?): String?
    }

    init {
        loggableMethods = HashMap()
        loggableMethods["open"] = MethodFormatter { name, _ -> "Открываем страницу '$name'" }
        loggableMethods["click"] = MethodFormatter { name, _ -> "Кликаем на элемент '$name'" }
        loggableMethods["submit"] = MethodFormatter { name, _ -> "Нажимаем на элемент '$name'" }
        loggableMethods["clear"] = MethodFormatter { name, _ -> "Очищаем элемент '$name'" }
        loggableMethods["sendKeys"] = MethodFormatter { name, args ->
            val arguments = (args?.get(0) as Array<*>).contentToString();
            "Вводим в элемент '$name' значение [${arguments}]"
        }
        loggableMethods["waitUntil"] = MethodFormatter { name, args ->
            val matcher = (if (args?.get(0) is Matcher<*>) args[0] else args?.get(1)) as Matcher<*>?
            "Ждем пока элемент '$name' будет в состоянии [$matcher]"
        }
        loggableMethods["should"] = MethodFormatter { name, args ->
            val matcher = (if (args?.get(0) is Matcher<*>) args[0] else args?.get(1)) as Matcher<*>?
            "Проверяем что элемент '$name' в состоянии [$matcher]"
        }
    }
}