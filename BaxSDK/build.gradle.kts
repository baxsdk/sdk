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

println("Sonatype Username: $sonatypeUsername")
println("Sonatype Password: $sonatypePassword")
//println("Signing Key: $signingKey")
//println("Signing Password: $signingPassword")

android {
    publishing {
        // Enable publishing for the release variant
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
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

}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

afterEvaluate {
    println("Sonatype Username exists: ${!sonatypeUsername.isNullOrEmpty()}")
    println("Sonatype Password exists: ${!sonatypePassword.isNullOrEmpty()}")
    
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "mx.bax.sdk"
                artifactId = "BaxSDK-core"
                version = "2025.01.29.1"

                pom {
                    name.set("BaxSDK")
                    description.set("Bax SDK for Android")
                    url.set("https://github.com/bax-exchange/sdk-android")
                    packaging = "aar"
                    
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    
                    developers {
                        developer {
                            id.set("baxsdk")
                            name.set("Rodrigo Velazquez")
                            email.set("rvelazquez@bax.mx")
                        }
                    }
                    
                    scm {
                        connection.set("scm:git:git://github.com/bax-exchange/sdk-android.git")
                        developerConnection.set("scm:git:ssh://github.com:bax-exchange/sdk-android.git")
                        url.set("https://github.com/bax-exchange/sdk-android")
                    }
                    organization {
                        name.set("Bax Exchange")
                        url.set("https://www.bax.mx")
                    }
                }
            }
        }
        repositories {
            maven {
                name = "Maven"
                url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                credentials {
                    username = sonatypeUsername
                    password = sonatypePassword
                }
            }
        }
    }
}

tasks.register<Zip>("createBundle") {
    dependsOn("publishToMavenLocal")
    
    from("${System.getProperty("user.home")}/.m2/repository") {
        include("mx/bax/sdk/**")  // Adjust path based on your groupId
    }
    
    archiveFileName.set("sdk-bundle-${version}.zip")
    destinationDirectory.set(layout.buildDirectory.dir("bundle"))
}

signing {
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications)
}