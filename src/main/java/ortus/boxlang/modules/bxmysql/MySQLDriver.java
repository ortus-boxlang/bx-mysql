/**
 * [BoxLang]
 *
 * Copyright [2023] [Ortus Solutions, Corp]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ortus.boxlang.modules.bxmysql;

import java.util.Map;

import ortus.boxlang.runtime.config.segments.DatasourceConfig;
import ortus.boxlang.runtime.dynamic.casters.StringCaster;
import ortus.boxlang.runtime.jdbc.drivers.DatabaseDriverType;
import ortus.boxlang.runtime.jdbc.drivers.IJDBCDriver;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.IStruct;
import ortus.boxlang.runtime.types.Struct;
import ortus.boxlang.runtime.types.util.StructUtil;

/**
 * The HyperSQL JDBC Driver
 * https://hsqldb.org/doc/2.0/guide/dbproperties-chapt.html#dpc_connection_url
 */
public class MySQLDriver implements IJDBCDriver {

	/**
	 * The name of the driver
	 */
	protected static final Key					NAME						= new Key( "Mysql" );

	/**
	 * The class name of the driver
	 */
	protected static final String				DRIVER_CLASS_NAME			= "org.hsqldb.jdbc.JDBCDriver";

	/**
	 * The default delimiter for the custom parameters
	 */
	protected static final String				DEFAULT_DELIMITER			= "&";

	/**
	 * Default Protocols Map
	 */
	protected static final Map<String, String>	DEFAULT_PROTOCOLS			= Map.of(
	    "loadbalance", "loadBalance",
	    "replication", "replication"
	);

	/**
	 * The default parameters for the connection URL
	 * These are added to the connection URL as query parameters
	 */
	protected static final IStruct				DEFAULT_PARAMS				= Struct.of(
	);

	/**
	 * Default Hikari Properties For MYSQL Performance
	 * https://cdn.oreillystatic.com/en/assets/1/event/21/Connector_J%20Performance%20Gems%20Presentation.pdf
	 */
	protected static final IStruct				DEFAULT_HIKARI_PROPERTIES	= Struct.of(
	    // This sets the number of prepared statements that the driver will cache per connection
	    "prepStmtCacheSize", 250,
	    // This is the maximum length of a prepared SQL statement that the driver will cache
	    "prepStmtCacheSqlLimit", 2048,
	    // Neither of the above parameters have any effect if the cache is in fact disabled, as it is by default
	    "cachePrepStmts", true,
	    // If available, use it to get a big boost in performance
	    "useServerPrepStmts", true,
	    "useLocalSessionState", true,
	    "rewriteBatchedStatements", true,
	    "cacheResultSetMetadata", true,
	    "cacheServerConfiguration", true,
	    "elideSetAutoCommits", true,
	    "maintainTimeStats", false
	);

	@Override
	public Key getName() {
		return NAME;
	}

	@Override
	public DatabaseDriverType getType() {
		return DatabaseDriverType.MYSQL;
	}

	/**
	 * The class name of the driver
	 */
	@Override
	public String getClassName() {
		return DRIVER_CLASS_NAME;
	}

	@Override
	public String buildConnectionURL( DatasourceConfig config ) {
		// Validate the database
		String database = ( String ) config.properties.getOrDefault( "database", "" );
		if ( database.isEmpty() ) {
			throw new IllegalArgumentException( "The database property is required for the HyperSQL JDBC Driver" );
		}

		// Validate the host
		String host = ( String ) config.properties.getOrDefault( "host", "localhost" );
		if ( host.isEmpty() ) {
			host = "localhost";
		}

		// Default the protocol to mem
		String protocol = ( String ) config.properties.getOrDefault( "protocol", "" );
		if ( protocol.length() > 0 && !DEFAULT_PROTOCOLS.containsKey( protocol ) ) {
			throw new IllegalArgumentException(
			    "The protocol property is invalid for the MySQL JDBC Driver: [" + protocol + "]. Available protocols are: " +
			        String.join( ", ", DEFAULT_PROTOCOLS.keySet() )
			);
		}
		// Append the : to the protocol if it exists
		if ( protocol.length() > 0 ) {
			protocol += ":";
		}

		// Port
		String port = StringCaster.cast( config.properties.getOrDefault( "port", "3306" ) );
		if ( port.isEmpty() || port.equals( "0" ) ) {
			port = "3306";
		}

		// Custom Params
		IStruct params = new Struct( DEFAULT_PARAMS );
		// If the custom parameters are a string, convert them to a struct
		if ( config.properties.get( Key.custom ) instanceof String castedParams ) {
			config.properties.put( Key.custom, StructUtil.fromQueryString( castedParams, DEFAULT_DELIMITER ) );
		}
		// Add the custom parameters
		config.properties.getAsStruct( Key.custom ).forEach( params::put );

		// Add Hikari Defaults if they don't exist into the properties
		DEFAULT_HIKARI_PROPERTIES.forEach( ( key, value ) -> {
			if ( !config.properties.containsKey( key ) ) {
				config.properties.put( key, value );
			}
		} );

		// Add username if it exists
		if ( config.properties.containsKey( Key.username ) && config.properties.getAsString( Key.username ).length() > 0 ) {
			params.put( "user", config.properties.get( Key.username ) );
		}

		// Add password if it exists
		if ( config.properties.containsKey( Key.password ) ) {
			params.put( Key.password, config.properties.get( Key.password ) );
		}

		// Build the connection URL with no host info
		return String.format(
		    "jdbc:mysql:%s//%s:%s/%s?%s",
		    protocol,
		    host,
		    port,
		    database,
		    StructUtil.toQueryString( params, DEFAULT_DELIMITER )
		);
	}

}
