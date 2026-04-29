import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.android)
    alias(libs.plugins.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.gms)
    alias(libs.plugins.crashlytics)
    alias(libs.plugins.hilt)
}

android {
    namespace = "xyz.teamgravity.kichkinashahzoda"

    compileSdk {
        version = release(libs.versions.sdk.compile.get().toInt()) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "xyz.teamgravity.kichkinashahzoda"
        minSdk = libs.versions.sdk.min.get().toInt()
        targetSdk = libs.versions.sdk.target.get().toInt()
        versionCode = 5
        versionName = "1.1.3"

        ndk {
            debugSymbolLevel = "FULL"
            abiFilters += setOf("armeabi-v7a", "arm64-v8a", "x86_64", "x86")
        }

        bundle {
            language {
                enableSplit = false
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        debug {
            applicationIdSuffix = ".debug"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    // compose
    implementation(platform(libs.compose))
    implementation(libs.compose.ui)
    implementation(libs.compose.graphics)
    implementation(libs.compose.preview)
    implementation(libs.compose.material3)

    // compose icons
    implementation(libs.compose.icons)

    // compose activity
    implementation(libs.compose.activity)

    // compose lifecycle
    implementation(libs.compose.lifecycle)

    // compose viewmodel
    implementation(libs.compose.viewmodel)

    // compose hilt
    implementation(libs.compose.hilt)

    // core
    implementation(libs.core)

    // splash
    implementation(libs.splash)

    // firebase
    implementation(platform(libs.firebase))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.messaging)

    // hilt
    implementation(libs.hilt)
    ksp(libs.dagger.compiler)

    // coroutines
    implementation(libs.coroutines)
    implementation(libs.coroutines.android)

    // exoplayer
    implementation(libs.exoplayer)
    implementation(libs.exoplayer.ui)
    implementation(libs.exoplayer.mediasession)

    // destinations
    implementation(libs.destinations)
    ksp(libs.destinations.compiler)

    // timber
    implementation(libs.timber)

    // gravity core
    implementation(libs.gravity.core)
    implementation(libs.gravity.core.compose)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xannotation-default-target=param-property",
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api"
        )
    }
}