import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}
apply(from = "${project.rootDir}/jacoco.gradle")
apply(plugin = "org.jetbrains.dokka")

android {
    namespace = "com.salesforce.loyalty.mobile.sources"
    compileSdk = 33

    defaultConfig {
        minSdk = 24
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        var consumerProguardFiles = "consumer-rules.pro"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("boolean", "LOG_ENABLED", "false")
            enableUnitTestCoverage = true
        }
        getByName("debug") {
            buildConfigField("boolean", "LOG_ENABLED", "true")
            isMinifyEnabled = false
            enableUnitTestCoverage = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }

}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.0")
    implementation("com.google.android.material:material:1.8.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")

    //Rx
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("io.reactivex.rxjava2:rxjava:2.2.12")
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.6.1")

    // Mock web server
    testImplementation("com.squareup.okhttp3:mockwebserver:4.10.0")

    // Gson
    implementation("com.google.code.gson:gson:2.9.0")

    // JUnit
    testImplementation("junit:junit:4.13.2")

    // Coroutine test
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.2")

    // Google truth for assertion
    testImplementation("com.google.truth:truth:1.1.3")

    testImplementation("org.mockito:mockito-core:3.3.3")

    //Mockk
    testImplementation("io.mockk:mockk:1.12.4")
}

tasks {
    val dokkaJavadoc by getting(org.jetbrains.dokka.gradle.DokkaTask::class) {
        outputDirectory.set(rootProject.file("docs/"))
    }
}

tasks.withType<org.jetbrains.dokka.gradle.DokkaTaskPartial>().configureEach {
    dokkaSourceSets.configureEach {
        includeNonPublic.set(true)
        skipDeprecated.set(true)
        skipEmptyPackages.set(true)
    }
}
