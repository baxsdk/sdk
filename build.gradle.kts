plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}

android {
    namespace = "com.example.baxsdk"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
        targetSdk = 34
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("release") {
            from(components["release"])

            groupId = "com.github.alxsags"
            artifactId = "BaxSDK"
            version = "0.0.1"
        }
    }
}
