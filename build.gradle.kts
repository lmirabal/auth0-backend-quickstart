plugins {
    kotlin("jvm") version "1.3.72"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(http4k("core"))
    implementation(http4k("server-jetty"))
    implementation(http4k("client-okhttp"))
    implementation(http4k("cloudnative"))
    implementation("com.auth0:java-jwt:3.10.3")
    implementation("com.auth0:jwks-rsa:0.11.0")

    testImplementation(http4k("testing-hamkrest"))
    testImplementation(http4k("format-jackson"))
    testImplementation(junit("api"))
    testRuntimeOnly(junit("engine"))
}

tasks {
    test {
        useJUnitPlatform()
    }
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    wrapper {
        distributionType = Wrapper.DistributionType.ALL
        gradleVersion = "6.4"
    }
}

fun http4k(module: String) = "org.http4k:http4k-$module:3.246.0"
fun junit(module: String) = "org.junit.jupiter:junit-jupiter-$module:5.6.2"