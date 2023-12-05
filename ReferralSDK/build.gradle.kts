plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.salesforce.referral_sdk"
    compileSdk = 33

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")
    // Gson
    implementation("com.google.code.gson:gson:2.10")
    //Hilt
    implementation("com.google.dagger:hilt-android:2.48.1")
    implementation("com.google.dagger:hilt-compiler:2.48.1")

    // Mock web server
    testImplementation("com.squareup.okhttp3:mockwebserver:4.10.0")
    // JUnit
    testImplementation("junit:junit:4.13.2")
    // Coroutine test
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
    // Google truth for assertion
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("org.mockito:mockito-core:5.3.1")

    //Mockk
    testImplementation("io.mockk:mockk:1.12.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}