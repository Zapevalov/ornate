package site.oppabet.elements

import io.qameta.allure.Description
import org.openqa.selenium.WebElement
import org.ornate.OrnateWebElement
import org.ornate.extension.FindBy
import org.ornate.extension.Selector

interface SectionHeader: OrnateWebElement<SectionHeader> {
    @FindBy("page_title", selector = Selector.ID)
    @Description("Заголовок")
    fun title(): OrnateWebElement<WebElement>

    @FindBy(".//input")
    @Description("Поле для ввода поискового запроса")
    fun searchField(): OrnateWebElement<WebElement>
}