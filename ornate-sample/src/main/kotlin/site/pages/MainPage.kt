package site.pages


import site.oppabet.elements.Section
import io.qameta.allure.Description
import site.oppabet.modalWindows.searchWindow.SearchWindow
import org.ornate.WebPage
import org.ornate.extension.FindBy
import org.ornate.extension.Param

interface MainPage: WebPage {
    @FindBy("//section[@class='c-section' and .//h2/span[text() = '{{ title }}']]")
    @Description("Секция {{ title }}")
    fun section(@Param("title") title: String): Section

    @FindBy("//div[@class='search-popup v-modal-search']")
    @Description("Модальное диалоговое окно поиска")
    fun modalSearchWindow(): SearchWindow


}