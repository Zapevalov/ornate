import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import java.net.URI

buildscript {
    repositories {
        jcenter()
        mavenLocal()
    }
}

val gradleScriptDir by extra("${rootProject.projectDir}/gradle")

tasks.withType(Wrapper::class) {
    gradleVersion = "6.8.1"
}

plugins {
    java
//    id("ru.vyarus.quality") version "4.4.0"
    id("com.jfrog.bintray") version "1.8.0"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    id("net.researchgate.release") version "2.8.1"
    kotlin("jvm") version "1.4.21" apply false
    `java-library`
    `maven-publish`
    signing
}

release {
    tagTemplate = "\${version}"
}

configure(listOf(rootProject)) {
    description = rootProject.name
    group = group
}


configure(subprojects) {
    group = group
    version = version

    apply {
        plugin("org.jetbrains.kotlin.jvm")
        plugin("kotlin")
        plugin("maven")
        plugin("java-library")
//        plugin("ru.vyarus.quality")
        plugin("io.spring.dependency-management")
        plugin("maven-publish")
    }

    repositories {
        mavenCentral()
        jcenter()
        mavenLocal()
    }

    dependencies {
        implementation(kotlin("stdlib-jdk8"))
        implementation("org.apache.commons:commons-lang3:3.7")
        implementation("org.seleniumhq.selenium:selenium-java:3.141.59")
        implementation("io.github.bonigarcia:webdrivermanager:2.1.0")
        implementation("ru.yandex.qatools.matchers:webdriver-matchers:1.4.1")
        implementation("org.awaitility:awaitility:3.1.2")
        implementation("org.slf4j:slf4j-api:1.7.25")
        implementation("org.slf4j:slf4j-simple:1.7.25")
        implementation("org.hamcrest:hamcrest-all:1.3")
        implementation("org.assertj:assertj-core:3.6.2")
        implementation("org.mockito:mockito-core:3.2.4")
        implementation("junit:junit:4.12")
        implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.30")
//        implementation("net.bytebuddy:byte-buddy:LATEST")
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }

    java {
        withJavadocJar()
        withSourcesJar()
    }

    publishing {
        publications {
            create<MavenPublication>("main") {
                from(components["java"])
            }
        }

        repositories {
            maven {
                name = "GitHubPackages"
                url = URI.create("https://maven.pkg.github.com/zapevalov/ornate")
                credentials {
                    username = System.getenv("GITHUB_ACTOR")
                    password = System.getenv("GITHUB_TOKEN")
                }
            }
        }
    }
}