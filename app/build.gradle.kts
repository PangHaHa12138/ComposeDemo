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
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.material)
    implementation(libs.androidx.material.icons.core)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.cardview)
    implementation(libs.coil.kt.coil.compose)

    implementation(libs.exoplayer)

    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.common)
    implementation(libs.androidx.media3.common.ktx)
    implementation(libs.androidx.media3.container)
    implementation(libs.androidx.media3.exoplayer.dash)
    implementation(libs.androidx.media3.exoplayer.hls)
    implementation(libs.androidx.media3.exoplayer.smoothstreaming)
    implementation(libs.androidx.media3.exoplayer.rtsp)
    implementation(libs.androidx.media3.datasource.rtmp)
    implementation(libs.androidx.media3.datasource.okhttp)
    implementation(libs.androidx.media3.datasource)
    implementation(libs.androidx.media3.database)
    implementation(libs.androidx.media3.extractor)
    implementation(libs.androidx.media3.decoder)
    implementation(libs.androidx.media3.ui)
    implementation(libs.androidx.media3.ui.compose)
    implementation(libs.androidx.media3.session)


//    // For MIDI playback support with ExoPlayer (see additional dependency requirements in
//    // https://github.com/androidx/media/blob/release/libraries/decoder_midi/README.md)
//    //implementation(libs.androidx.media3.exoplayer.midi)
//    // For ad insertion using the Interactive Media Ads SDK with ExoPlayer
//    implementation(libs.androidx.media3.exoplayer.ima)
//    // For loading data using the Cronet network stack
//    implementation(libs.androidx.media3.datasource.cronet)
//    // For building media playback UIs for Android TV using the Jetpack Leanback library
//    implementation(libs.androidx.media3.ui.leanback)
//    // For integrating with Cast
//    implementation(libs.androidx.media3.cast)
//    // For scheduling background operations using Jetpack Work's WorkManager with ExoPlayer
//    implementation(libs.androidx.media3.exoplayer.workmanager)
//    // For transforming media files
//    implementation(libs.androidx.media3.transformer)
//    // For applying effects on video frames
//    implementation(libs.androidx.media3.effect)
//    // For muxing media files
//    implementation(libs.androidx.media3.muxer)



    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)




}