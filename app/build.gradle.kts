plugins {
    alias(libs.plugins.android.application)
}

android {

    signingConfigs {
        getByName("debug") {
            storeFile = file("/home/enric/.android/debug.jks")
            storePassword = "android"
            keyPassword = "android"
            keyAlias = "androiddebugkey"
        }
    }

    namespace = "com.example.snooker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.snooker"
        minSdk = 23
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
    packaging {
        resources {
            excludes += "/META-INF/INDEX.LIST"
            excludes += "/META-INF/DEPENDENCIES"
        }
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.credentials)
    implementation(libs.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.google.api.client.android)
    implementation(libs.google.api.client)
    implementation(libs.google.api.services.drive)
    implementation(libs.google.http.client.gson)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
