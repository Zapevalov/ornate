package org.ornate.extension

import org.openqa.selenium.By
import org.openqa.selenium.support.ByIdOrName


enum class Selector {
    CLASS_NAME {
        override fun buildBy(value: String?): By {
            return By.className(value)
        }
    },
    CSS {
        override fun buildBy(value: String?): By {
            return By.cssSelector(value)
        }
    },
    ID {
        override fun buildBy(value: String?): By {
            return By.id(value)
        }
    },
    ID_OR_NAME {
        override fun buildBy(value: String?): By {
            return ByIdOrName(value)
        }
    },
    LINK_TEXT {
        override fun buildBy(value: String?): By {
            return By.linkText(value)
        }
    },
    NAME {
        override fun buildBy(value: String?): By {
            return By.name(value)
        }
    },
    PARTIAL_LINK_TEXT {
        override fun buildBy(value: String?): By {
            return By.partialLinkText(value)
        }
    },
    TAG_NAME {
        override fun buildBy(value: String?): By {
            return By.tagName(value)
        }
    },
    XPATH {
        override fun buildBy(value: String?): By {
            return By.xpath(value)
        }
    };

    abstract fun buildBy(value: String?): By?
}