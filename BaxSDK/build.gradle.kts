plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("maven-publish")
    id("signing")
}

// Read all credentials from either environment variables or gradle.properties
val sonatypeUsername: String? = System.getenv("SONATYPE_USERNAME") ?: project.findProperty("sonatypeUsername") as String?
val sonatypePassword: String? = System.getenv("SONATYPE_PASSWORD") ?: project.findProperty("sonatypePassword") as String?
val signingKey: String? = System.getenv("SIGNING_KEY") ?: project.findProperty("signingKey") as String?
val signingPassword: String? = System.getenv("SIGNING_PASSWORD") ?: project.findProperty("signingPassword") as String?

android {
    namespace = "mx.bax.sdk"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

ext {
    set("PUBLISH_GROUP_ID", "io.github.baxsdk")
    set("PUBLISH_ARTIFACT_ID", "sdk")
    set("PUBLISH_VERSION", "2025.01.28")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                groupId = project.ext.get("PUBLISH_GROUP_ID").toString()
                artifactId = project.ext.get("PUBLISH_ARTIFACT_ID").toString()
                version = project.ext.get("PUBLISH_VERSION").toString()

                from(components["release"])

                pom {
                    name.set("Bax SDK")
                    description.set("Bax SDK for Android")
                    url.set("https://github.com/baxsdk/sdk")
                    
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    
                    developers {
                        developer {
                            id.set("baxsdk")
                            name.set("Bax SDK Team")
                            email.set("rvelazquez@bax.mx")
                        }
                    }
                    
                    scm {
                        connection.set("scm:git:git://github.com/baxsdk/sdk.git")
                        developerConnection.set("scm:git:ssh://github.com:baxsdk/sdk.git")
                        url.set("https://github.com/baxsdk/sdk")
                    }
                }
            }
        }
        
        repositories {
            maven {
                name = "sonatype"
                val releasesUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
                val snapshotsUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
                url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsUrl else releasesUrl)
                credentials {
                    username = sonatypeUsername
                    password = sonatypePassword
                }
            }
        }
    }

    signing {
        useInMemoryPgpKeys(signingKey, signingPassword)
        sign(publishing.publications["release"])
    }
}