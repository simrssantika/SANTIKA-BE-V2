package com.santika.simrs.global.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@Configuration
class FileStorageConfig {

    @Value("\${app.temp-dir:/tmp/app-uploads}")
    lateinit var tempDir: String

    @Value("\${app.upload-dir:/tmp/app-uploads}")
    lateinit var uploadDir: String

    @Bean
    fun tempDirectory(): Path = Paths.get(tempDir).also { Files.createDirectories(it) }

    @Bean
    fun uploadDirectory(): Path = Paths.get(uploadDir).also { Files.createDirectories(it) }
}
