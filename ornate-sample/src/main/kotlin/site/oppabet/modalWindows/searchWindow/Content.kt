package site.oppabet.modalWindows.searchWindow

import io.qameta.allure.Description
import org.ornate.OrnateWebElement
import org.ornate.extension.FindBy

interface Content {
    @FindBy(".//div[@class='search-popup-events__item']")
    @Description("Таблица с результатами поиска")
    fun searchResults(): List<OrnateWebElement<*>>
}