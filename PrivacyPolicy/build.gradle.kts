plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}
//apply(from = "${project.rootDir}/aar_copy.gradle.kts")

android {
    namespace = "com.art.privacy.policy"
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        setProperty("archivesBaseName", "PrivacyPolicy-v1.0.1")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    compileOnly("androidx.fragment:fragment:1.3.5")
}