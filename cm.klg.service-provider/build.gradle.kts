import com.diffplug.spotless.extra.wtp.EclipseWtpFormatterStep
import net.ltgt.gradle.errorprone.CheckSeverity
import net.ltgt.gradle.errorprone.errorprone
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask


plugins {
    java
    id("org.springframework.boot") version "4.0.5"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.openapi.generator") version "7.11.0"
    id("com.diffplug.spotless") version "7.0.4"
    id("net.ltgt.errorprone") version "4.1.0"
    `maven-publish`
}

apply(from = "$rootDir/../submodule/cm.klg.common.build/gradle/commonMicroservice.gradle")

group = "cm.klg"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

val mapstructVersion = "1.5.5.Final"
val errorProneVersion = "2.31.0"
val nullAwayVersion = "0.12.4"

dependencies {
    implementation("cm.klg:common.base:0.1.3-SNAPSHOT")
    implementation("cm.klg:common.service-bridge:0.0.1-SNAPSHOT")
    implementation("cm.klg:emb-spring-boot-starter:0.3.1-SNAPSHOT")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
    implementation("org.springframework.boot:spring-boot-starter-integration")
    implementation("jakarta.mail:jakarta.mail-api")
    implementation("org.slf4j:slf4j-api:2.0.9")
    compileOnly("org.projectlombok:lombok")
    compileOnly("org.jspecify:jspecify:1.0.0")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testCompileOnly("org.projectlombok:lombok")
    testCompileOnly("org.jspecify:jspecify:1.0.0")
    testAnnotationProcessor("org.projectlombok:lombok")

    // Liquibase
    implementation("org.springframework.boot:spring-boot-liquibase")
    implementation("org.liquibase:liquibase-core")

    // Mapstruct
    implementation("org.mapstruct:mapstruct:$mapstructVersion")
    annotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")
    testAnnotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")

    add("errorprone", "com.google.errorprone:error_prone_core:2.36.0")
    add("errorprone", "com.uber.nullaway:nullaway:0.12.6")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named<JavaCompile>("compileJava") {
    options.compilerArgs.add("--should-stop=ifError=FLOW")
    options.errorprone {
        disableWarningsInGeneratedCode = true
        excludedPaths.set(".*/build/generated/sources/.*")
        disableAllChecks.set(true)
        check("NullAway", CheckSeverity.ERROR)
        option("NullAway:AnnotatedPackages", "cm.klg.service-provider")
        option("NullAway:JSpecifyMode", true)
        option("NullAway:TreatGeneratedAsUnannotated", true)
        option("NullAway:CheckOptionalEmptiness", true)
        option("NullAway:SuggestSuppressions", true)

        option(
            "NullAway:ExcludedClasses",
            listOf(
                "cm.klg.uam.UamApplication",
                "cm.klg.uam.config",
            ).joinToString(","),
        )

        option(
            "NullAway:ExcludedClassAnnotations",
            listOf(
                "org.mapstruct.Mapper",
                "org.springframework.boot.test.context.SpringBootTest",
                "org.junit.jupiter.api.Test",
                "org.junit.jupiter.api.extension.ExtendWith",
                "org.junit.jupiter.api.BeforeEach",
                "org.mockito.Mock",
                "org.springframework.context.annotation.Configuration",
                "org.springframework.boot.autoconfigure.SpringBootApplication",
                "jakarta.annotation.Generated",
                "com.fasterxml.jackson.annotation.JsonProperty",
                "jakarta.persistence.Converter",
            ).joinToString(","),
        )

        option(
            "NullAway:CustomNullableAnnotations",
            listOf(
                "org.springframework.lang.Nullable",
                "jakarta.annotation.Nullable",
            ).joinToString(","),
        )

        option(
            "NullAway:CustomNonnullAnnotations",
            listOf(
                "org.springframework.lang.NonNull",
                "jakarta.annotation.Nonnull",
            ).joinToString(","),
        )

        option("NullAway:HandleTestAssertionLibraries", true)

        option(
            "NullAway:ExcludedFieldAnnotations",
            listOf(
                "org.springframework.beans.factory.annotation.Autowired",
                "jakarta.inject.Inject",
                "lombok.Generated",
                "lombok.NonNull",
                "org.jspecify.annotations.NonNull",
            ).joinToString(","),
        )
    }
}

tasks.named<JavaCompile>("compileTestJava") {
    options.errorprone.isEnabled.set(false)
}

val mainOpenApiGenerate by tasks.registering(GenerateTask::class) {
    generatorName.set("spring")
    templateDir.set("$rootDir/specs/openapi/templates/spring-boot")
    inputSpec.set("$rootDir/specs/openapi/inbound/main.yml")
    outputDir.set(
        layout.buildDirectory
            .dir("generated/sources/openapi")
            .get()
            .asFile.path,
    )
    apiPackage.set("cm.klg.generated.service.provider.adapter.rest.inbound.api")
    modelPackage.set("cm.klg.generated.service.provider.adapter.rest.inbound.dto")
    modelNameSuffix.set("DTO")
    configOptions.set(
        mapOf(
            "dateLibrary" to "java8-localdatetime",
            "library" to "spring-boot",
            "interfaceOnly" to "true",
            "useTags" to "true",
            "skipDefaultInterface" to "true",
            "useSpringBoot3" to "true",
        ),
    )
    typeMappings.set(
        mapOf(
            "time" to "java.time.LocalTime",
        ),
    )
    val generatedSourceCodeDir =
        file(outputDir.get()!! + "/src/main/java/cm/klg/generated/service/provider/adapter/rest/inbound")
    doFirst {
        generatedSourceCodeDir.deleteRecursively()
    }
    onlyIf {
        !generatedSourceCodeDir.exists() ||
            file(templateDir.get()!!).lastModified() > generatedSourceCodeDir.lastModified()
    }
}

val uamDomainEventsOpenApiGenerate by tasks.registering(GenerateTask::class) {
    generatorName.set("spring")
    templateDir.set("$rootDir/specs/openapi/templates/spring-boot")
    inputSpec.set("$rootDir/specs/openapi/inbound/uam-domain-event.yml")
    modelPackage.set("cm.klg.generated.service.provider.adapter.messaging.inbound.dto")
    modelNamePrefix.set("Uam")
    outputDir.set(
        layout.buildDirectory
            .dir("generated/sources/openapi")
            .get()
            .asFile.path,
    )
    configOptions.set(
        mapOf(
            "dateLibrary" to "java8-localdatetime",
            "library" to "spring-boot",
            "interfaceOnly" to "true",
            "useTags" to "true",
            "skipDefaultInterface" to "true",
            "useSpringBoot3" to "true",
        ),
    )
    typeMappings.set(
        mapOf(
            "time" to "java.time.LocalTime",
        ),
    )
    val generatedSourceCodeDir =
        file(outputDir.get() + "/src/main/java/cm/klg/generated/service/provider/adapter/messaging/inbound")
    doFirst {
        generatedSourceCodeDir.deleteRecursively()
    }
    onlyIf {
        !generatedSourceCodeDir.exists() ||
            file(inputSpec.get()).lastModified() > generatedSourceCodeDir.lastModified()
    }
}

tasks.compileJava {
    dependsOn(
        mainOpenApiGenerate,
        uamDomainEventsOpenApiGenerate,
    )
}

sourceSets.main.get().java.srcDir(
    layout.buildDirectory
        .dir("generated/sources/openapi/src/main/java")
        .get()
        .asFile.path,
)

spotless {
    java {
        toggleOffOn()
        targetExclude("build/**")
        importOrder()
        googleJavaFormat("1.33.0")
            .reflowLongStrings()
            .formatJavadoc(true)
            .reorderImports(true)
            .groupArtifact("com.google.googlejavaformat:google-java-format")
    }
    kotlin {
        targetExclude("build/**")
        target("**/*.kts")
        ktlint("1.5.0")
    }
    format("xml") {
        target("src/**/*.xml")
        targetExclude("build/**")
        eclipseWtp(EclipseWtpFormatterStep.XML)
    }
    yaml {
        targetExclude("build/**")
        target(
            "src/*/resources/**/*.yaml",
            "src/*/resources/**/*.yml",
            "specs/openapi/**/*.yaml",
            "specs/openapi/**/*.yml",
        )
        targetExclude("src/test/resources/docker-compose.yml")
        jackson()
            .feature("ORDER_MAP_ENTRIES_BY_KEYS", true)
    }
}
