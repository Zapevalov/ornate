package oppabet

import io.github.bonigarcia.wdm.WebDriverManager
import org.hamcrest.Matchers
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.ornate.Ornate
import org.ornate.WebDriverConfiguration
import org.testng.annotations.AfterTest
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test
import ru.yandex.qatools.matchers.webdriver.DisplayedMatcher
import ru.yandex.qatools.matchers.webdriver.TextMatcher
import site.OppabetSite
import site.oppabet.listener.AllureLogger

class OppabetSiteTest {

    private lateinit var ornate: Ornate
    private lateinit var driver: WebDriver

    @BeforeTest
    fun startDriver() {
        WebDriverManager.chromedriver().driverVersion("88").setup()
        driver = ChromeDriver()
        ornate = Ornate(WebDriverConfiguration(driver, "https://oppabet.com"))
            .listener(AllureLogger())
    }

    @Test
    fun oppabetSimpleTest() {
        onSite().onMainPage().apply {
            open()

            section("LIVE Bets").apply {
                header().apply {
                    title().should(TextMatcher.text(Matchers.equalToIgnoringCase("LIVE Bets")))
                    searchField().sendKeys("Фирма веников не вяжет", Keys.ENTER)
                }
            }

            modalSearchWindow().should(DisplayedMatcher.displayed()).apply {
                closeBtn().click()
                closeBtn().should(Matchers.not(DisplayedMatcher.displayed()))
            }
        }
    }

    private fun onSite(): OppabetSite = ornate.create(driver, OppabetSite::class.java)

    @AfterTest
    fun stopDriver() = driver.close()
}