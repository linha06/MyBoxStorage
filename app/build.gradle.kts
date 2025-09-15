plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.linha.myboxstorage"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.linha.myboxstorage"
        minSdk = 24
        targetSdk = 36
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
    kotlinOptions {
        jvmTarget = "11"
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    // room implementation
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler) // import ksp dulu di ke-2 build.gradle
    annotationProcessor(libs.androidx.room.compiler)
    // view model
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // paging
    implementation(libs.androidx.paging.runtime)
    // optional - Jetpack Compose integration
    implementation(libs.androidx.paging.compose)
    // paging room
    implementation(libs.androidx.room.paging)
    // live data
    implementation(libs.androidx.runtime.livedata)
    // navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.runtime.ktx)
    // pulltoRefresh
    implementation(libs.material3)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.material.icons.core)
    // google font
    implementation(libs.androidx.ui.text.google.fonts)
    // animasi
    implementation(libs.androidx.animation)
    // datastore
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    // coil foto
    implementation(libs.coil.compose)
    // permission
    implementation(libs.accompanist.permissions)
}