# bx-mysql

```
|:------------------------------------------------------:|
| ⚡︎ B o x L a n g ⚡︎
| Dynamic : Modular : Productive
|:------------------------------------------------------:|
```

<blockquote>
	Copyright Since 2023 by Ortus Solutions, Corp
	<br>
	<a href="https://www.boxlang.io">www.boxlang.io</a> |
	<a href="https://www.ortussolutions.com">www.ortussolutions.com</a>
</blockquote>

<p>&nbsp;</p>

This module provides a BoxLang JDBC driver for MySQL, enabling integration between BoxLang applications and MySQL databases through BoxLang's `queryExecute()` and datasource management.

## Features

- 🚀 **High Performance**: Built on `com.mysql:mysql-connector-j` with performance-tuned connection defaults
- ⚙️ **Overridable Defaults**: Every default is a JDBC URL parameter you can override via `custom`
- 🔄 **BoxLang Integration**: Native support for `queryExecute()` and datasource definitions
- ⚖️ **HA Protocols**: Supports the `loadbalance` and `replication` connection protocols

## Installation

### Via CommandBox (Recommended)

```bash
box install bx-mysql
```

### Via BoxLang Module Installer

```bash
# Into the BoxLang HOME
install-bx-module bx-mysql

# Or a local folder
install-bx-module bx-mysql --local
```

## Quick Start

```javascript
// Application.bx
this.datasources[ "myDB" ] = {
    "driver"  : "mysql",
    "host"    : "localhost",
    "port"    : 3306,
    "database": "mydb",
    "username": "root",
    "password": "secret"
};

// Use it in your code
result = queryExecute( "SELECT 1 AS test", [], { "datasource": "myDB" } );
```

## Configuration Examples

See [BoxLang's Defining Datasources](https://boxlang.ortusbooks.com/boxlang-language/syntax/queries#defining-datasources) for full details on where and how to define datasources.

### Application-Level Datasource

```javascript
this.datasources[ "myDB" ] = {
    "driver"  : "mysql",
    "host"    : "db.example.com",
    "port"    : 3306,
    "database": "mydb",
    "username": "app",
    "password": "secret"
};
```

### Inline Datasource

Define the datasource directly in the `queryExecute()` options:

```javascript
result = queryExecute(
    "SELECT * FROM users WHERE id = ?",
    [ 1 ],
    {
        "datasource": {
            "driver"  : "mysql",
            "host"    : "localhost",
            "database": "mydb",
            "username": "root",
            "password": "secret"
        }
    }
);
```

### Protocols

Use `protocol` for `loadbalance` or `replication` connections. Any other value throws an error.

```javascript
this.datasources[ "haDB" ] = {
    "driver"  : "mysql",
    "protocol": "loadbalance",
    "host"    : "node1:3306,node2:3306",
    "database": "mydb",
    "username": "app",
    "password": "secret"
};
```

## Default Connection Parameters

Every default below is appended to the JDBC URL as a query string parameter. Because they are URL parameters (not fixed pool properties), you can override any of them with the `custom` struct.
See the [Connector/J performance notes](https://cdn.oreillystatic.com/en/assets/1/event/21/Connector_J%20Performance%20Gems%20Presentation.pdf) for background.

| Parameter | Default | Purpose |
|-----------|---------|---------|
| `prepStmtCacheSize` | `250` | Number of prepared statements the driver caches per connection |
| `prepStmtCacheSqlLimit` | `2048` | Maximum length of a prepared SQL statement the driver will cache |
| `cachePrepStmts` | `true` | Enables the prepared statement cache. The two settings above have no effect without it |
| `useServerPrepStmts` | `true` | Uses server-side prepared statements for a large performance boost |
| `useLocalSessionState` | `true` | Uses local session state instead of extra round trips to the server |
| `rewriteBatchedStatements` | `true` | Rewrites batched statements into multi-statement or multi-value queries |
| `cacheResultSetMetadata` | `true` | Caches result set metadata |
| `cacheServerConfiguration` | `true` | Caches server configuration variables |
| `elideSetAutoCommits` | `true` | Skips redundant `SET autocommit` calls |
| `maintainTimeStats` | `false` | Disables internal time statistics tracking |

### Overriding Defaults

Add any Connector/J parameter to `custom`. Your values win over the defaults, and the remaining defaults still apply.

```javascript
this.datasources[ "myDB" ] = {
    "driver"  : "mysql",
    "database": "mydb",
    "username": "root",
    "password": "secret",
    "custom"  : {
        // Override a default
        "useServerPrepStmts": false,
        // Add your own
        "connectTimeout": 5000,
        "useSSL": true
    }
};
```

`custom` can also be a query string:

```javascript
"custom": "useServerPrepStmts=false&connectTimeout=5000"
```

## Development

### Prerequisites

- Java 21+
- Gradle (wrapper included)

### Building from Source

```bash
git clone https://github.com/ortus-boxlang/bx-mysql.git
cd bx-mysql

./gradlew build
./gradlew test
```

### Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Make your changes and add tests
4. Ensure all tests pass (`./gradlew test`)
5. Format your code (`./gradlew spotlessApply`)
6. Open a Pull Request

## Resources

- **Documentation**: [BoxLang Database Guide](https://boxlang.ortusbooks.com/boxlang-language/syntax/queries)
- **MySQL Connector/J**: [Configuration Properties](https://dev.mysql.com/doc/connector-j/en/connector-j-reference-configuration-properties.html)
- **Issues & Support**: [GitHub Issues](https://github.com/ortus-boxlang/bx-mysql/issues)
- **ForgeBox**: [bx-mysql Package](https://forgebox.io/view/bx-mysql)

## Changelog

See [changelog.md](changelog.md) for a complete list of changes and version history.

## License

Licensed under the Apache License, Version 2.0. See [LICENSE](https://www.apache.org/licenses/LICENSE-2.0) for details.

## Ortus Sponsors

BoxLang is a professional open-source project and it is completely funded by the [community](https://patreon.com/ortussolutions) and [Ortus Solutions, Corp](https://www.ortussolutions.com). Ortus Patreons get many benefits like a cfcasts account, a FORGEBOX Pro account and so much more. If you are interested in becoming a sponsor, please visit our patronage page: [https://patreon.com/ortussolutions](https://patreon.com/ortussolutions)

### THE DAILY BREAD

> "I am the way, and the truth, and the life; no one comes to the Father, but by me (JESUS)" Jn 14:1-12
