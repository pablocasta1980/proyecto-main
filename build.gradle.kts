plugins {
    java
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "co.edu.uniquindio"
version = "0.0.1-SNAPSHOT"
description = "Breve descripción de la aplicación"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }

    // Excluir commons-logging de todas las configuraciones
    all {
        exclude(group = "commons-logging", module = "commons-logging")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.0")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

    implementation("org.mapstruct:mapstruct:1.6.3")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Asegurar que se use correctamente el orden de procesadores de anotaciones con Lombok y MapStruct
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation ("org.springframework.boot:spring-boot-starter-thymeleaf")

    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")


    implementation ("org.springframework.boot:spring-boot-starter-mail")
    implementation ("jakarta.mail:jakarta.mail-api:2.1.2")

    implementation("org.simplejavamail:simple-java-mail:8.12.5")
    implementation("org.simplejavamail:batch-module:8.12.5")

    implementation("com.cloudinary:cloudinary-http45:1.39.0"){
        // Excluir commons-logging específicamente de Cloudinary si es necesario
        exclude(group = "commons-logging", module = "commons-logging")
    }
    implementation ("com.itextpdf:itext7-core:7.2.2")




}



// Configuración para asegurar que Lombok se procese antes de MapStruct
tasks.withType<JavaCompile> {
    options.compilerArgs = options.compilerArgs + listOf(
        "-Amapstruct.defaultComponentModel=spring",
        "-Amapstruct.unmappedTargetPolicy=IGNORE"
    )
}
