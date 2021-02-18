package org.ornate.exception

import org.openqa.selenium.WebDriverException

class OrnateDriverException(message: String?): WebDriverException(message)