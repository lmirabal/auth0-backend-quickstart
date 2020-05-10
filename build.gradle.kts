plugins {
    kotlin("jvm") version "1.3.72"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.http4k:http4k-core:3.246.0")
    implementation("org.http4k:http4k-server-jetty:3.246.0")
    implementation("org.http4k:http4k-client-okhttp:3.246.0")

    testImplementation("org.http4k:http4k-testing-hamkrest:3.246.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")
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