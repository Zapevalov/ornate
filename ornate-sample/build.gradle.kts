plugins {
    id("io.qameta.allure") version "2.8.1"
}


allure {
    resultsDir = file("build/allure-results")
    version = "2.10.0"
    autoconfigure = true
    aspectjweaver = true
}



dependencies {
    implementation(project(":core"))
    implementation(project(":webdriver"))
    implementation("org.testng:testng:7.3.0")
    implementation("io.qameta.allure:allure-gradle:2.8.1")
    implementation("io.qameta.allure:allure-testng:2.13.8")
    implementation("org.slf4j:slf4j-simple:1.7.30")
    implementation("org.slf4j:slf4j-api:1.7.30")
    implementation("io.github.bonigarcia:webdrivermanager:4.3.0")
    implementation("com.google.guava:guava:28.2-jre")
    implementation("org.seleniumhq.selenium:selenium-java:3.141.59") { exclude("com.google.guava", "guava") }
    implementation("org.aeonbits.owner:owner-java8:1.0.6")
    implementation("ru.yandex.qatools.matchers:webdriver-matchers:1.4.1")
    implementation("ru.yandex.qatools.matchers:matcher-decorators:1.4.1")
}


tasks.named<Test>("test") {
    useTestNG()
}


tasks.withType(JavaCompile::class) {
    options.encoding = "UTF-8"
}