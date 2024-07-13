plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.10"

    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.kk.android.bayareanews"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.kk.android.bayareanews"
        minSdk = 24
        targetSdk = 34
        versionCode = 9
        versionName = "2.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }

    buildTypes {
        release {
            isShrinkResources = true
            isMinifyEnabled = true
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

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("com.google.firebase:firebase-common-ktx:20.4.2")
    implementation("com.google.firebase:firebase-config-ktx:21.6.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("androidx.compose.foundation:foundation:1.6.0-rc01")

    //
    implementation(platform("androidx.compose:compose-bom:2023.06.01"))
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    //

    //kk
    implementation ("com.github.bumptech.glide:glide:4.14.2")
    annotationProcessor("com.github.bumptech.glide:compiler:4.11.0")
    annotationProcessor("androidx.annotation:annotation:1.7.1")
    //implementation("com.google.android.material:material:1.11.0-beta01")

    //implementation("com.giphy.sdk:ui:2.3.12")
    //implementation("com.fasterxml.jackson.core:jackson-core:2.11.1")
    //implementation("com.fasterxml.jackson.core:jackson-annotations:2.11.1")
    //implementation("com.fasterxml.jackson.core:jackson-databind:2.11.1")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // Retrofit with Scalar Converter
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")

    // Kotlin serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    // Retrofit with Kotlin serialization Converter
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    //image downloader
    //implementation("io.coil-kt:coil-compose:2.4.0")
    //implementation("io.coil-kt:coil-gif:2.4.0")

    //glide faster than coil
    implementation("com.github.bumptech.glide:compose:1.0.0-beta01")

    //dagger
    implementation("com.google.dagger:hilt-android:2.49")
    kapt("com.google.dagger:hilt-android-compiler:2.48.1")

    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")  //has hiltViewModel()


    val nav_version = "2.7.6"

    // Kotlin
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    // Feature module Support
    implementation("androidx.navigation:navigation-dynamic-features-fragment:$nav_version")

    // Testing Navigation
    androidTestImplementation("androidx.navigation:navigation-testing:$nav_version")

    // Jetpack Compose Integration
    implementation("androidx.navigation:navigation-compose:$nav_version")

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    //material3
    implementation("androidx.compose.material3:material3-android:1.2.0-beta02")
    implementation("androidx.compose.material3:material3-window-size-class:1.2.0-beta02")
    implementation("com.google.accompanist:accompanist-flowlayout:0.33.2-alpha")

    implementation("io.coil-kt:coil:2.5.0")
    implementation("io.coil-kt:coil-compose:2.5.0")

    implementation("androidx.compose.material:material-icons-extended")  //card expand/collapse
    implementation("androidx.compose.material:material")

    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
    implementation(platform("com.google.firebase:firebase-config:21.6.0"))

    // This dependency is downloaded from the Google’s Maven repository.
    // Make sure you also include that repository in your project's build.gradle file.
    implementation("com.google.android.play:feature-delivery:2.1.0")

    // For Kotlin users, also import the Kotlin extensions library for Play Feature Delivery:
    implementation("com.google.android.play:feature-delivery-ktx:2.1.0")

    // Room database
    val room_version = "2.6.1"

    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$room_version")
    // To use Kotlin Symbol Processing (KSP)
    //ksp("androidx.room:room-compiler:$room_version")


}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}

//kotlin {
//    jvmToolchain(8)
//}