package com.santika.simrs.shared.file.common

sealed class FileException(override val message: String) : RuntimeException(message) {
    class FileNotFoundFromStorage(message: String) : FileException(message)
    class FileNotFoundFromDatabase(message: String) : FileException(message)
    class FileInvalidPath(message: String) : FileException(message)
}
