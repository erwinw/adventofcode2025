plugins {
    // We apply the 'java' plugin here just to define the group/version for the root if needed
    java
}

subprojects {
    // Apply plugins common to all CLI apps
    apply(plugin = "java")
    apply(plugin = "application")

    repositories {
        mavenCentral()
    }

    dependencies {
        // Note: In subprojects blocks, we use string names for configurations
        "testImplementation"("org.junit.jupiter:junit-jupiter:5.9.1")
        "implementation"("com.google.guava:guava:32.1.2-jre")
    }

    // Configure Java version
    configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(25))
        }
    }

    // Configure common Test task settings
    tasks.withType<Test> {
        useJUnitPlatform()
    }
}