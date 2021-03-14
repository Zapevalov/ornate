package site.oppabet.modalWindows.searchWindow


import io.qameta.allure.Description
import org.openqa.selenium.WebElement
import org.ornate.OrnateWebElement
import org.ornate.annotation.FindBy

interface SearchWindow : OrnateWebElement<SearchWindow>{

    @FindBy(".//div[@class='search-popup__close']")
    @Description("Кнопка закрытия окна")
    fun closeBtn(): OrnateWebElement<WebElement>

    @FindBy(".//div[@class='search-popup__header']")
    @Description("Хэдер всплывшего окна")
    fun header(): WindowHeader

    @FindBy(".//div[@class='search-popup-tabs' and //*[contains(text(),'{{tabName}}')]]")
    @Description("Область с результатами поиска")
    fun content(tabName: String): Content
}