plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.compose"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.compose"
        minSdk = 24
        targetSdk = 35
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
    implementation(libs.exoplayer)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation (libs.coil.kt.coil.compose)
    implementation(libs.androidx.material)
    implementation(libs.androidx.material.icons.core)
    implementation(libs.androidx.material.icons.extended)
    implementation (libs.androidx.cardview)


    implementation(libs.androidx.media3.exoplayer)
    // For DASH playback support with ExoPlayer
    implementation(libs.androidx.media3.exoplayer.dash)
    // For HLS playback support with ExoPlayer
    implementation(libs.androidx.media3.exoplayer.hls)
    // For SmoothStreaming playback support with ExoPlayer
    implementation(libs.androidx.media3.exoplayer.smoothstreaming)
    // For RTSP playback support with ExoPlayer
    implementation(libs.androidx.media3.exoplayer.rtsp)
    // For MIDI playback support with ExoPlayer (see additional dependency requirements in
    // https://github.com/androidx/media/blob/release/libraries/decoder_midi/README.md)
    //implementation(libs.androidx.media3.exoplayer.midi)
    // For ad insertion using the Interactive Media Ads SDK with ExoPlayer
    implementation(libs.androidx.media3.exoplayer.ima)

    // For loading data using the Cronet network stack
    implementation(libs.androidx.media3.datasource.cronet)
    // For loading data using the OkHttp network stack
    implementation(libs.androidx.media3.datasource.okhttp)
    // For loading data using librtmp
    implementation(libs.androidx.media3.datasource.rtmp)

    // For building media playback UIs using Compose
    implementation(libs.androidx.media3.ui.compose)
    // For building media playback UIs using Views
    implementation(libs.androidx.media3.ui)
    // For building media playback UIs for Android TV using the Jetpack Leanback library
    implementation(libs.androidx.media3.ui.leanback)

    // For exposing and controlling media sessions
    implementation(libs.androidx.media3.session)
    // For extracting data from media containers
    implementation(libs.androidx.media3.extractor)
    // For integrating with Cast
    implementation(libs.androidx.media3.cast)
    // For scheduling background operations using Jetpack Work's WorkManager with ExoPlayer
    implementation(libs.androidx.media3.exoplayer.workmanager)
    // For transforming media files
    implementation(libs.androidx.media3.transformer)
    // For applying effects on video frames
    implementation(libs.androidx.media3.effect)
    // For muxing media files
    implementation(libs.androidx.media3.muxer)

    // Common functionality for reading and writing media containers
    implementation(libs.androidx.media3.container)
    // Common functionality for media database components
    implementation(libs.androidx.media3.database)
    // Common functionality for media decoders
    implementation(libs.androidx.media3.decoder)
    // Common functionality for loading data
    implementation(libs.androidx.media3.datasource)
    // Common functionality used across multiple media libraries
    implementation(libs.androidx.media3.common)
    // Common Kotlin-specific functionality
    implementation(libs.androidx.media3.common.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)




}