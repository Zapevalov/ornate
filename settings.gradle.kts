rootProject.name = "Ornate"

include (":core", ":webdriver", ":sample")
project(":core").projectDir = file("ornate-core")
project(":webdriver").projectDir = file("ornate-webdriver")
project(":sample").projectDir = file("ornate-sample")
