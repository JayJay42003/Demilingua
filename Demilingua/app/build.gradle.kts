plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.demilingua"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.demilingua"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation ("com.google.android.material:material:1.12.0")
    implementation ("com.fasterxml.jackson.core:jackson-databind:2.13.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0") // Para JSON
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.1") // Para logs
    implementation ("com.squareup.okhttp3:okhttp-urlconnection:4.9.1")

    // Carga de imágenes dinámicas
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation(libs.swiperefreshlayout)
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")

    // Jetpack & Architecture
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)

    // Room (Solo Runtime para Java)
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)

    // Hilt (Inyección de dependencias para Java)
    implementation(libs.hilt.android)
    annotationProcessor(libs.hilt.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}