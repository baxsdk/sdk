plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("maven-publish")
}

android {
    namespace = "mx.bax.sdk.BaxSDK"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx.v190)
    testImplementation(libs.junit.junit)
}

publishing {
    publications {
        create<MavenPublication>("release") {

            groupId = "com.github.baxsdk"
            artifactId = "BaxSDK"
            version = "0.0.1"

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
