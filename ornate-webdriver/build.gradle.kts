description = "Ornate Webdriver"

dependencies {
    api(project(":core"))
    implementation("org.apache.httpcomponents:httpclient:4.5.8")

    testImplementation("ru.yandex.qatools.matchers:webdriver-matchers")
    testImplementation("org.apache.commons:commons-lang3")
    testImplementation("org.mockito:mockito-core")
    testImplementation("org.assertj:assertj-core")
    testImplementation("junit:junit")
}