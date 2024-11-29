plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.ambrosianaapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.ambrosianaapp"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.transition)
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.transition)
    implementation(libs.androidx.lifecycle.runtime.compose.android)
    implementation(libs.suggestions)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")

    implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
    implementation ("androidx.compose.runtime:runtime-livedata:1.5.4")
    implementation ("androidx.compose.runtime:runtime:1.5.4")

    implementation("androidx.compose.ui:ui-text-google-fonts:1.5.4")



    // Amplify support
    implementation("com.amplifyframework.ui:authenticator:1.4.0")
    implementation("com.amplifyframework:aws-storage-s3:2.24.0")
    implementation("com.amplifyframework:core-kotlin:2.24.0")
    implementation("com.amplifyframework:aws-analytics-pinpoint:2.24.0")

    implementation("com.amplifyframework:aws-auth-cognito:2.24.0")
    implementation("com.amplifyframework:aws-geo-location:2.24.0")



    implementation("com.amplifyframework:aws-api:2.24.0")
}