buildscript {
    extra.apply {
        set("compose_ui_version", "1.3.3")
    }
    repositories {
        google()
        maven(url="https://dl.bintray.com/kotlin/dokka")
    }
    dependencies {
        classpath("org.jacoco:org.jacoco.core:0.8.7")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.8.20}")
    }
}


// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "7.4.0" apply false
    id("com.android.library") version "7.4.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
    id("org.jetbrains.dokka") version "1.8.20" apply false
}
apply(plugin = "jacoco")