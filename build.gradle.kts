plugins {
    id("java")
    id("checkstyle")
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.4"
}

allprojects {
    apply(plugin = "java")

    group = "me.dgpr"
    version = "1.0.0-SNAPSHOT"
    java.sourceCompatibility = JavaVersion.VERSION_21

    repositories {
        mavenCentral()
    }

    tasks.compileTestJava {
        options.encoding = "UTF-8"
    }

    tasks.compileJava {
        options.encoding = "UTF-8"
    }

    tasks.withType<Checkstyle>().configureEach {
        configFile = file(".style/google_checks.xml")
    }

    tasks.check {
        dependsOn("checkstyleMain", "checkstyleTest")
    }
}

subprojects {
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    dependencies {
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}