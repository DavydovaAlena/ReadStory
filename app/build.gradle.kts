
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
//    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")

}



android {

    namespace = "ru.adavydova.booksmart"
    compileSdk = 34

    defaultConfig {
        applicationId = "ru.adavydova.booksmart"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_1_9
        targetCompatibility = JavaVersion.VERSION_1_9
    }
    kotlinOptions {
        jvmTarget = "9"
    }
    buildFeatures {

        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    val readium_version = "3.0.0-alpha.1"

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:v2024.02.00-alpha01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")


    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")

    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.12.0")
    // gson convertor
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    //Hilt
    implementation("com.google.dagger:hilt-android:2.50")
    kapt("com.google.dagger:hilt-android-compiler:2.50")

    //Paging3
    implementation("androidx.paging:paging-compose:3.3.0-alpha02")

    //Room

    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    implementation ("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.0")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    implementation("com.squareup.leakcanary:leakcanary-android:3.0-alpha-1")

    //Coil
    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation ("com.google.android.gms:play-services-location:21.1.0")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("androidx.compose.foundation:foundation:1.6.1")

//    implementation ("com.google.android.recaptcha:recaptcha:18.4.0")
//    implementation ("com.google.android.gms:play-services-safetynet:18.0.1")

    //Media3
    implementation("androidx.media3:media3-datasource-okhttp:1.2.1")
    implementation("androidx.media3:media3-exoplayer:1.2.1")
    implementation("androidx.media3:media3-ui:1.2.1")
    implementation("androidx.media3:media3-session:1.2.1")
    implementation("androidx.media3:media3-exoplayer-ima:1.2.1")
    implementation("androidx.media3:media3-ui:1.2.1")
    implementation("androidx.media3:media3-ui-leanback:1.2.1")
    implementation("androidx.media3:media3-exoplayer-workmanager:1.2.1")

    //Fragment
    implementation("androidx.compose.ui:ui-viewbinding:1.6.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")

    //Readium

//
//    implementation("org.readium.kotlin-toolkit:readium-adapter-pdfium-navigator:3.0.0-alpha.1")
//    implementation("org.readium.kotlin-toolkit:readium-adapter-pdfium-document:3.0.0-alpha.1")
    implementation ("org.readium.kotlin-toolkit:readium-shared:$readium_version")
    implementation ("org.readium.kotlin-toolkit:readium-streamer:$readium_version")
    implementation ("org.readium.kotlin-toolkit:readium-navigator:$readium_version")
    implementation ("org.readium.kotlin-toolkit:readium-lcp:$readium_version")
    implementation("org.readium.kotlin-toolkit:readium-navigator-media-tts:3.0.0-alpha.1")

    implementation("org.readium.kotlin-toolkit:readium-navigator-media:3.0.0-alpha.1")
    implementation("org.readium.kotlin-toolkit:readium-adapter-exoplayer-audio:3.0.0-alpha.1")
    // https://mavenlibs.com/maven/dependency/org.readium.kotlin-toolkit/readium-navigator-media-tts
    implementation("org.readium.kotlin-toolkit:readium-navigator-media-tts:3.0.0-alpha.1")

    implementation("joda-time:joda-time:2.12.7")
    implementation ("com.jakewharton.timber:timber:5.0.1")

    //DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    implementation ("com.github.skydoves:colorpicker-compose:1.0.0")
    implementation("dev.chrisbanes.haze:haze-jetpack-compose:0.5.4")
}
