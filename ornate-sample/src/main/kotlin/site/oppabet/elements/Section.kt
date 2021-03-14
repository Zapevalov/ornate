package site.oppabet.elements

import io.qameta.allure.Description
import org.ornate.ElementsCollection
import org.ornate.OrnateWebElement
import org.ornate.annotation.FindBy

interface Section : OrnateWebElement<Section>{
    @FindBy(".//div[@class='c-section__header ']")
    @Description("Хэдер секции")
    fun header(): SectionHeader

    @FindBy(".//div[@data-name='dashboard-champ-content']")
    @Description("Расписание матчей")
    fun dashBoardChampItems(): ElementsCollection<OrnateWebElement<*>>
}