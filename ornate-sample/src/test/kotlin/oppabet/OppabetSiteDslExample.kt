package oppabet

import io.github.bonigarcia.wdm.WebDriverManager
import org.hamcrest.CoreMatchers.not
import org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.ornate.*
import org.testng.annotations.AfterTest
import org.testng.annotations.BeforeTest
import org.testng.annotations.Test
import ru.yandex.qatools.matchers.webdriver.DisplayedMatcher.displayed
import ru.yandex.qatools.matchers.webdriver.TextMatcher.text
import site.OppabetSite
import site.oppabet.listener.AllureLogger


class OppabetSiteDslExample {
    private lateinit var ornateConfig: Ornate
    private lateinit var driver: WebDriver

    @BeforeTest
    fun startDriver() {
        WebDriverManager.chromedriver().setup()
        driver = ChromeDriver()
        ornateConfig = Ornate(WebDriverConfiguration(driver, "https://oppabet.com"))
            .listener(AllureLogger())
    }

    @Test
    fun test() = webTest {
        onSite<OppabetSite>(ornateConfig, driver) {
            onMainPage().apply {
                open()

                section("LIVE Bets").apply {
                    header().apply {
                        title().should(text(equalToIgnoringCase("LIVE Bets")))
                        searchField().sendKeys("Фирма веников не вяжет", Keys.ENTER)
                    }
                }

                (modalSearchWindow().should(displayed())).apply {
                    closeBtn().click()
                    closeBtn().should(not(displayed()))
                }
            }
        }
    }


    @AfterTest
    fun stopDriver() = driver.close()
}