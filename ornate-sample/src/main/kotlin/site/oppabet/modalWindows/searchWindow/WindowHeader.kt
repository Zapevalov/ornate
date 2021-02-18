package site.oppabet.modalWindows.searchWindow

import io.qameta.allure.Description
import org.openqa.selenium.WebElement
import org.ornate.OrnateWebElement
import org.ornate.extension.FindBy
import org.ornate.extension.Param
import org.ornate.extension.Selector

interface WindowHeader{
    @FindBy(".//div[@class='search-popup__title']")
    @Description("Заголовок окна")
    fun title(): OrnateWebElement<*>

    @FindBy(".//span")
    @Description("Кол-во найденных записей")
    fun searchResult(): OrnateWebElement<*>

    @FindBy(".//div[@class='c-checkbox' and ./label[text()='{{ name }}']]")
    @Description("Чебокс {{ name }}")
    fun checkBox(@Param("name") name: String): OrnateWebElement<*>

    @FindBy("search-in-popup", selector = Selector.ID)
    @Description("Поисковое поле")
    fun searchField(): OrnateWebElement<*>

    @FindBy(".//div[@class='search-popup__clear']")
    @Description("кнопка очистки поискового поля")
    fun clearSearchFieldBtn(): OrnateWebElement<*>

    @FindBy(".//button")
    @Description("Кнопка \"Поиск\"")
    fun searchBtn(): OrnateWebElement<*>
}