plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.linguaplayindia"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.linguaplayindia"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
    // AndroidX + Material
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // RecyclerView + CardView + Lottie
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.airbnb.android:lottie:6.4.0")

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.activity)
    implementation(libs.androidx.ui)
    ksp(libs.androidx.room.compiler)

    // Gson
    implementation(libs.gson)

    // Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // ML Kit translation dependency
    implementation("com.google.mlkit:translate:17.0.2")

    // (Optional) For downloading language models automatically
    implementation("com.google.mlkit:common:18.10.0")

    // Retrofit for HTTP networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // Gson converter for JSON parsing
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Optional: Logging (helps debug API calls)
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    implementation("com.google.mlkit:translate:17.0.2")

    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.biometric:biometric:1.1.0")

    implementation("androidx.biometric:biometric:1.2.0-alpha05")
    implementation("com.airbnb.android:lottie:6.3.0")
    implementation("androidx.drawerlayout:drawerlayout:1.2.0")
    implementation("androidx.cardview:cardview:1.0.0")

}

