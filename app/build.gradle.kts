plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    kotlin("kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.divine.journey"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.divine.journey"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    //hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.junit.ktx)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    testImplementation(libs.androidx.core.testing)
    annotationProcessor(libs.androidx.room.compiler)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.hilt.android.testing)
    kapt (libs.hilt.android.compiler)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation (libs.androidx.hilt.navigation.compose)

    // Mockito for mocking
    testImplementation(libs.mockito.core)

    // Mockito Kotlin extension for easier usage with Kotlin
    testImplementation(libs.mockito.kotlin)

    // Truth for easier assertions
    testImplementation(libs.truth)

    // Hilt for Dependency Injection in tests
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.android.compiler.v244)

    // Coroutines test library
    testImplementation(libs.kotlinx.coroutines.test)

    // Hilt testing requires AndroidX Test libraries
    androidTestImplementation(libs.androidx.core)
    androidTestImplementation(libs.androidx.junit)

    // For running tests on AndroidX components
    androidTestImplementation(libs.androidx.core.testing)

    // AndroidX Test Runner for running tests
    androidTestImplementation(libs.androidx.runner)

    testImplementation(libs.robolectric)

    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)

    //retrofit libraries for network calls
    implementation(libs.retrofit)
    implementation(libs.retrofit2.kotlin.coroutines.adapter)
    implementation(libs.logging.interceptor)
    implementation(libs.adapter.rxjava)
    implementation(libs.converter.gson)
    implementation(libs.gson)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.mockwebserver)

}