import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
apply(from = "${project.rootDir}/jacoco.gradle")

var connectedappPropertiesFile = rootProject.file("SampleApps/MyNTORewards/connectedapp.properties")
var connectedappProperties = Properties().apply{
    load(project.rootProject.file("SampleApps/MyNTORewards/connectedapp.properties").inputStream())
}
//connectedappProperties.load(new FileInputStream(connectedappPropertiesFile))

android {
    namespace = "com.salesforce.loyalty.mobile.MyNTORewards"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.salesforce.loyalty.mobile.MyNTORewards"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "CONNECTED_APP_NAME", connectedappProperties.getProperty("CONNECTED_APP_NAME"))
        buildConfigField("String", "CONSUMER_KEY", connectedappProperties.getProperty("CONSUMER_KEY"))
        buildConfigField("String", "CONSUMER_SECRET", connectedappProperties.getProperty("CONSUMER_SECRET"))
        buildConfigField("String", "CALLBACK_URL", connectedappProperties.getProperty("CALLBACK_URL"))
        buildConfigField("String", "BASE_URL", connectedappProperties.getProperty("BASE_URL"))
        buildConfigField("String", "INSTANCE_URL", connectedappProperties.getProperty("INSTANCE_URL"))
        buildConfigField("String", "COMMUNITY_URL", connectedappProperties.getProperty("COMMUNITY_URL"))
        buildConfigField("String", "SELF_REGISTER_URL", connectedappProperties.getProperty("SELF_REGISTER_URL"))
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            enableUnitTestCoverage = true
        }
        getByName("debug") {
            isMinifyEnabled = false
            enableUnitTestCoverage = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
//            testCoverageEnabled = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    useLibrary("android.test.mock")
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0"
    }
    lint {
        abortOnError = false
        lintConfig = file("$rootDir/lint-baseline.xml")
    }
}


dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":Sources"))
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.0")
    implementation("androidx.activity:activity-ktx:1.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")
    implementation("androidx.compose.runtime:runtime-livedata:1.4.0-alpha04")
    implementation("androidx.compose.material:material-icons-extended:1.3.1")
    implementation("androidx.compose.material3:material3:1.1.0")
    implementation("androidx.compose.material3:material3-window-size-class:1.1.0")

    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    val composeVersion = rootProject.extra.get("compose_ui_version") as String

    //compose
    implementation("androidx.activity:activity-compose:1.6.1")
    implementation("androidx.compose.ui:ui:1.4.1")
    implementation("androidx.compose.ui:ui-tooling-preview:${composeVersion}")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${composeVersion}")
    debugImplementation("androidx.compose.ui:ui-tooling:${composeVersion}")
    debugImplementation("androidx.compose.ui:ui-test-manifest:${composeVersion}")

    //pager and indicator

    implementation("com.google.accompanist:accompanist-pager:0.29.0-alpha")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.29.0-alpha")

    //navigation
    val nav_version = "2.5.3"
    implementation("androidx.navigation:navigation-compose:$nav_version")

    //qr code
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")

    //glide library to load image
    implementation("com.github.bumptech.glide:compose:1.0.0-alpha.1")
    implementation("io.coil-kt:coil-compose:2.4.0")

    //Serialize and de-serialize objects to JSON and vice-versa
    implementation("com.google.code.gson:gson:2.8.5")

    // Network library
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")

    //Rx
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("io.reactivex.rxjava2:rxjava:2.2.12")
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.6.1")

    //EncryptedSharedPreference
    implementation("androidx.security:security-crypto:1.0.0-rc03")
    implementation("androidx.security:security-identity-credential:1.0.0-alpha01")
    testImplementation("androidx.test:monitor:1.4.0")
    implementation("androidx.test:core:1.4.1-alpha04")

    // Test rules and transitive dependencies:
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$composeVersion")

    androidTestImplementation("androidx.test:runner:1.5.2")


    testImplementation("org.mockito:mockito-core:5.3.1")
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
    androidTestImplementation("org.mockito:mockito-android:2.7.15")

    testImplementation("io.mockk:mockk:1.13.4")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")

    implementation("androidx.camera:camera-camera2:1.3.0-beta01")
    implementation("androidx.camera:camera-lifecycle:1.3.0-beta01")
    implementation("androidx.camera:camera-view:1.3.0-beta01")
    implementation("androidx.camera:camera-extensions:1.3.0-beta01")
    implementation("com.google.accompanist:accompanist-permissions:0.31.3-beta")

    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

}