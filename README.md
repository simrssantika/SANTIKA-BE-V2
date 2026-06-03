# SANTIKA SIMRS

Sistem Informasi Manajemen Rumah Sakit berbasis **modular monolith** dengan Spring Boot 4 + Kotlin.

Dibangun untuk mengelola data kepegawaian, pasien, medis, dan operasional rumah sakit secara terpusat dengan arsitektur yang maintainable dan performant.

---

## Teknologi Utama

| | |
|---|---|
| **Backend** | Kotlin 2.2.21 · Spring Boot 4.0.6 · Java 24 |
| **Database** | PostgreSQL 17 · Liquibase · JPA + jOOQ |
| **Cache** | DragonFly (Redis-compatible) |
| **Security** | Spring Security · JWT · Rate Limiting |
| **Resilience** | Resilience4j (Circuit Breaker, Retry) |

---

## Dokumentasi

Buka file HTML berikut di browser untuk panduan lengkap:

- **[docs/index.html](docs/index.html)** — Setup, instalasi, konfigurasi, environment variable
- **[docs/file/index.html](docs/file/index.html)** — File system: upload, storage, rollback, cleanup
- **[docs/signed-url/index.html](docs/signed-url/index.html)** — Signed URL: akses file via HMAC tanpa JWT

---

## Struktur Modul

```
src/main/kotlin/com/santika/simrs/
├── global/          # Security, JWT, cache, rate limit, exception
├── module/
│   ├── kepegawaian/ # SDM — pegawai, dokter, departemen
│   └── master/      # Data master — wilayah, medis, penjamin
└── shared/          # Auth, user, role, file
```

---

## Quick Start

```bash
# 1. Jalankan PostgreSQL dan DragonFly
# 2. Generate kode jOOQ
./gradlew generateJooq

# 3. Jalankan aplikasi
./gradlew bootRun
```

Server berjalan di `http://localhost:9002`
