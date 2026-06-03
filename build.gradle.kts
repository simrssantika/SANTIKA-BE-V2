plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.spring") version "2.2.21"
    id("org.springframework.boot") version "4.0.6"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("plugin.jpa") version "2.2.21"
    id("nu.studer.jooq") version "9.0"
}

group = "com.santika"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(24)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.springframework.boot:spring-boot-starter-liquibase")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("tools.jackson.module:jackson-module-kotlin")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-actuator-test")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
    testImplementation("org.springframework.boot:spring-boot-starter-jooq-test")
    testImplementation("org.springframework.boot:spring-boot-starter-liquibase-test")
    testImplementation("org.springframework.boot:spring-boot-starter-validation-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // security
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.bouncycastle:bcpkix-jdk18on:1.84")

    // jwt
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

    // cache backend - DragonFly (Redis-compatible via Lettuce)
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.apache.commons:commons-pool2")

    // resilience - circuit breaker, retry, time limiter
    implementation("org.springframework.boot:spring-boot-starter-aspectj")
    implementation("io.github.resilience4j:resilience4j-spring-boot3:2.3.0")
    implementation("io.github.resilience4j:resilience4j-kotlin:2.3.0")


    jooqGenerator("org.jooq:jooq-codegen:3.19.28")
    jooqGenerator("org.jooq:jooq-meta-extensions-liquibase:3.19.28")
    jooqGenerator("org.postgresql:postgresql")
    jooqGenerator("org.liquibase:liquibase-core:4.29.2")
    jooqGenerator("org.yaml:snakeyaml:2.3")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}


jooq {
    configurations {
        create("main") {
            jooqConfiguration.apply {
                generator.apply {
                    database.apply {
                        name = "org.jooq.meta.extensions.liquibase.LiquibaseDatabase"
                        withProperties(
                            org.jooq.meta.jaxb.Property().withKey("rootPath")
                                .withValue("${projectDir}/src/main/resources"),
                            org.jooq.meta.jaxb.Property().withKey("scripts").withValue("db/changelog/001-master.yaml")
                        )
                        withSchemata(
                            org.jooq.meta.jaxb.SchemaMappingType().withInputSchema("MASTER"),
                            org.jooq.meta.jaxb.SchemaMappingType().withInputSchema("SHARED"),
                            org.jooq.meta.jaxb.SchemaMappingType().withInputSchema("KEPEGAWAIAN")
                        )
                    }
                    target.apply {
                        packageName = "com.santika.simrs.jooq"
                        directory = "src/main/jooq"
                    }
                }
            }
        }
    }
}

sourceSets {
    main {
        java {
            srcDir("src/main/jooq")
        }
    }
}

