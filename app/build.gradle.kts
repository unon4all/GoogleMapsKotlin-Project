plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)

    id("kotlin-parcelize")
    alias(libs.plugins.navigation.safe.args)
    id("kotlin-kapt")
    alias(libs.plugins.daggerHilt)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
}

android {
    namespace = "com.example.googlemapsproject"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.googlemapsproject"
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
        dataBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

secrets {
    // Optionally specify a different file name containing your secrets.
    // The plugin defaults to "local.properties"
    propertiesFileName = "secrets.properties"

    // A properties file containing default secret values. This file can be
    // checked in version control.
    defaultPropertiesFileName = "local.defaults.properties"

    // Configure which keys should be ignored by the plugin by providing regular expressions.
    // "sdk.dir" is ignored by default.
    ignoreList.add("keyToIgnore") // Ignore the key "keyToIgnore"
    ignoreList.add("sdk.*")       // Ignore all keys matching the regexp "sdk.*"
}


// Allow references to generated code
kapt {
    correctErrorTypes = true
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

    implementation(libs.hilt.android)
    implementation(libs.play.services.maps)
    implementation(libs.androidx.appcompat)
    kapt(libs.hilt.compiler)


    //Navigation Component
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    //LifeCycle
    implementation(libs.androidx.lifecycle.extensions)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    //Fused Location
    implementation(libs.play.services.location)

    //Util
    implementation(libs.android.maps.utils)

    kapt(libs.androidx.hilt.compiler)

    implementation(libs.androidx.work.runtime.ktx)

    //Easy Permissions
    implementation(libs.easypermissions.ktx)

    implementation(libs.androidx.appcompat.v120)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}