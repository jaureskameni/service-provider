import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
    java
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springDependencyManagement)
    alias(libs.plugins.openApiGenerator)
    alias(libs.plugins.spotless)
    alias(libs.plugins.errorprone)
    `maven-publish`
}

group = "cm.klg"
version = "0.0.1-SNAPSHOT"

val commonBuildDir = "$rootDir/../submodule/cm.klg.common.build"

ext["nullawayAnnotatedPackages"] = "cm.klg.service_provider"
ext["nullawayExcludedClasses"] =
    listOf(
        "cm.klg.service_provider.ServiceProviderApplication",
        "cm.klg.service_provider.config",
    )

apply(from = "$commonBuildDir/gradle/common.microservice.gradle.kts")
apply(from = "$commonBuildDir/gradle/java.toolchain.gradle.kts")
apply(from = "$commonBuildDir/gradle/errorprone.gradle.kts")
apply(from = "$commonBuildDir/gradle/spotless.gradle.kts")
apply(from = "$commonBuildDir/gradle/openapi.conventions.gradle.kts")

dependencies {
    // Modules KLG
    implementation(libs.cmCommonBase)
    implementation(libs.cmCommonServiceBridge)
    implementation(libs.cmEmbSpringBootStarter)

    // Spring Boot (Versions gérées par le BOM)
    implementation(libs.springBootStarterDataJpa)
    implementation(libs.springBootStarterOauth2ResourceServer)
    implementation(libs.springBootStarterSecurity)
    implementation(libs.springBootStarterWeb)
    implementation(libs.springBootStarterValidation)
    implementation(libs.springBootStarterIntegration)
    implementation(libs.springBootStarterLiquibase)

    // Librairies Tierces
    implementation(libs.springdocOpenapi)
    implementation(libs.jakartaMail)
    implementation(libs.slf4jApi)
    implementation(libs.liquibaseCore)
    implementation(libs.mapstruct)

    compileOnly(libs.lombok)
    compileOnly(libs.jspecify)
    runtimeOnly(libs.postgresql)

    annotationProcessor(libs.lombok)
    annotationProcessor(libs.springBootConfigurationProcessor)
    annotationProcessor(libs.mapstructProcessor)

    // Tests
    testImplementation(libs.springBootStarterTest)
    testImplementation(libs.springSecurityTest)
    testImplementation(libs.restAssured)
    testImplementation(libs.restAssuredSpringMock)
    testRuntimeOnly(libs.junitPlatformLauncher)

    testCompileOnly(libs.lombok)
    testCompileOnly(libs.jspecify)
    testAnnotationProcessor(libs.lombok)
    testAnnotationProcessor(libs.mapstructProcessor)
}

// ── Configuration OpenAPI ────────────────────────────────────────────────────
val applySpringBootOpenApi: ((Any) -> Unit) by extra
val deleteBeforeGenerate: ((Task, String) -> Unit) by extra
val onlyIfStale: ((Any, String) -> Unit) by extra

val mainOpenApiGenerate by tasks.registering(GenerateTask::class) {
    applySpringBootOpenApi(this)
    inputSpec.set("$rootDir/specs/openapi/inbound/main.yml")
    templateDir.set("$rootDir/specs/openapi/templates/spring-boot")
    apiPackage.set("cm.klg.generated.service.provider.adapter.rest.inbound.api")
    modelPackage.set("cm.klg.generated.service.provider.adapter.rest.inbound.dto")
    modelNameSuffix.set("DTO")

    val genDir = "${outputDir.get()}/src/main/java/cm/klg/generated/service/provider/adapter/rest/inbound"
    deleteBeforeGenerate(this, genDir)
    onlyIfStale(this, genDir)
}

val mainDomainEventsOpenApiGenerate by tasks.registering(GenerateTask::class) {
    applySpringBootOpenApi(this)
    inputSpec.set("$rootDir/specs/openapi/outbound/domain-event.yml")
    templateDir.set("$rootDir/specs/openapi/templates/spring-boot")
    modelPackage.set("cm.klg.generated.service.provider.adapter.messaging.outbound.dto")

    val genDir = "${outputDir.get()}/src/main/java/cm/klg/generated/service/provider/adapter/messaging/outbound"
    deleteBeforeGenerate(this, genDir)
    onlyIfStale(this, genDir)
}

val uamDomainEventsOpenApiGenerate by tasks.registering(GenerateTask::class) {
    applySpringBootOpenApi(this)
    inputSpec.set("$rootDir/specs/openapi/inbound/uam-domain-event.yml")
    templateDir.set("$rootDir/specs/openapi/templates/spring-boot")
    modelPackage.set("cm.klg.generated.service.provider.adapter.messaging.inbound.dto")
    modelNamePrefix.set("Uam")

    val genDir = "${outputDir.get()}/src/main/java/cm/klg/generated/service/provider/adapter/messaging/inbound"
    deleteBeforeGenerate(this, genDir)
    onlyIfStale(this, genDir)
}

tasks.compileJava {
    dependsOn(
        mainOpenApiGenerate,
        mainDomainEventsOpenApiGenerate,
        uamDomainEventsOpenApiGenerate,
    )
}
