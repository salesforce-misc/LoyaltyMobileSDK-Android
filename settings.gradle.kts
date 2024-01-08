pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://jitpack.io") }
        maven(url="https://dl.bintray.com/kotlin/dokka")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }
    }
}
include("SampleApps:MyNTORewards")
include(":Sources")
include(":GamificationMobileSDK-Android:GamificationMobileSDK")