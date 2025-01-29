val sdkTag = "0.0.1"

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}

android {
    namespace = "mx.bax.sdk.BaxSDK"
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

repositories {
    mavenCentral()
    google()
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.baxsdk:sdk:$sdkTag")
}

publishing {
    publications {
        create<MavenPublication>("release") {
            from(components["release"])

            groupId = "com.github.baxsdk"
            artifactId = "BaxSDK"
            version = "$sdkTag"

            pom {
                name.set("BaxSDK")
                description.set("SDK for integrating with the Bax service")
                url.set("https://github.com/baxsdk/sdk")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("baxsdk")
                        name.set("Bax SDK Team")
                        email.set("sdk@bax.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/baxsdk/sdk.git")
                    developerConnection.set("scm:git:ssh://github.com/baxsdk/sdk.git")
                    url.set("https://github.com/baxsdk/sdk")
                }
            }
        }
    }
    repositories {
        maven {
            url = uri("https://jitpack.io")
        }
    }
}
