plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
    id("signing")
}

android {
    namespace = "io.github.baxsdk.sdk"
    compileSdk = 33
    
    defaultConfig {
        minSdk = 21
        targetSdk = 33
        
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "io.github.baxsdk"
            artifactId = "BaxSDK"
            version = "2025.01.27"

            afterEvaluate {
                from(components["release"])
            }
            
            pom {
                name.set("BAX SDK Android")
                description.set("Code for BAX Android SDK")
                url.set("https://github.com/bax-exchange/sdk-android")
                
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("http://www.opensource.org/licenses/mit-license.php")
                    }
                }
                
                developers {
                    developer {
                        name.set("Rodrigo Velazquez")
                        email.set("rvelazquez@bax.mx")
                        organization.set("Bax Exchange")
                        organizationUrl.set("http://www.bax.mx")
                    }
                }
                
                scm {
                    connection.set("scm:git:git://github.com/bax-exchange/sdk-android.git")
                    developerConnection.set("scm:git:ssh://github.com/bax-exchange/sdk-android.git")
                    url.set("https://github.com/bax-exchange/sdk-android/tree/develop")
                }
            }
        }
    }
    
    repositories {
        maven {
            name = "OSSRH"
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_PASSWORD")
            }
        }
    }
}

signing {
    val signingKey = System.getenv("GPG_PRIVATE_KEY")
    val signingPassword = System.getenv("MAVEN_GPG_PASSPHRASE")
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications["release"])
}