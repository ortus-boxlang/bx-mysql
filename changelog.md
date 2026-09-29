# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

* * *

## [Unreleased]

### 🛠 Build

- CI now runs Gradle through the project wrapper (`./gradlew`) instead of the Gradle setup action and a separately pinned Gradle version.

### 🔄 Changed

- All performance defaults (`prepStmtCacheSize`, `cachePrepStmts`, `useServerPrepStmts`, etc.) are now default JDBC URL params (`defaultCustomParams`) instead of default Hikari properties, so they can be overridden via the datasource `custom` struct.
- Rewrote the readme with installation, inline datasource examples, and the list of default connection parameters.

### 🛠 Build

- PR workflow now uses a concurrency group so a branch push and its PR event no longer run duplicate builds. The format check job now runs on `ubuntu-latest` (the retired `ubuntu-20.04` runner left the PR workflow queued forever). Removed the CommandBox `format:check` step (no such script exists; `./gradlew spotlessCheck` is the format check).

### 🔐 Security

- Bumps com.mysql:mysql-connector-j from 9.2.0 to 9.3.0.
- Bumped `mysql-connector-j` to version 9.2.0 to address [SNYK-JAVA-COMGOOGLEPROTOBUF-8055227](https://security.snyk.io/vuln/SNYK-JAVA-COMGOOGLEPROTOBUF-8055227)


## [1.0.0] - 2024-06-13

- First iteration of this module

[unreleased]: https://github.com/ortus-boxlang/bx-mysql/compare/v1.0.1...HEAD
[1.0.1]: https://github.com/ortus-boxlang/bx-mysql/compare/v1.0.1...v1.0.1
[1.0.0]: https://github.com/ortus-boxlang/bx-mysql/compare/f2ce71dad5581aa57b4c657144a175f7209dea47...v1.0.0
