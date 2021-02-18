package org.ornate.exception

import org.openqa.selenium.WebDriverException

class WaitUntilException(message: String?) : WebDriverException(message)