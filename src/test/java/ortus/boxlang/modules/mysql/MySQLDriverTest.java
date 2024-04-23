package ortus.boxlang.modules.mysql;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.runtime.config.segments.DatasourceConfig;
import ortus.boxlang.runtime.jdbc.drivers.DatabaseDriverType;
import ortus.boxlang.runtime.scopes.Key;

public class MySQLDriverTest {

	@Test
	@DisplayName( "Test getName()" )
	public void testGetName() {
		MySQLDriver	driver			= new MySQLDriver();
		Key			expectedName	= new Key( "Mysql" );
		assertThat( driver.getName() ).isEqualTo( expectedName );
	}

	@Test
	@DisplayName( "Test getType()" )
	public void testGetType() {
		MySQLDriver			driver			= new MySQLDriver();
		DatabaseDriverType	expectedType	= DatabaseDriverType.MYSQL;
		assertThat( driver.getType() ).isEqualTo( expectedType );
	}

	@Test
	@DisplayName( "Test buildConnectionURL()" )
	public void testBuildConnectionURL() {
		MySQLDriver			driver	= new MySQLDriver();
		DatasourceConfig	config	= new DatasourceConfig();
		config.properties.put( "driver", "Mysql" );
		config.properties.put( "database", "mydb" );

		String expectedURL = "jdbc:mysql://localhost:3306/mydb?";
		assertThat( driver.buildConnectionURL( config ) ).isEqualTo( expectedURL );
	}

	@DisplayName( "Throw an exception if the database is not found" )
	@Test
	public void testBuildConnectionURLNoDatabase() {
		MySQLDriver			driver	= new MySQLDriver();
		DatasourceConfig	config	= new DatasourceConfig();

		assertThrows( IllegalArgumentException.class, () -> {
			driver.buildConnectionURL( config );
		} );
	}

	@DisplayName( "Throw an exception if the protocol is not valid" )
	@Test
	public void testBuildConnectionURLInvalidProtocol() {
		MySQLDriver			driver	= new MySQLDriver();
		DatasourceConfig	config	= new DatasourceConfig();
		config.properties.put( "driver", "Mysql" );
		config.properties.put( "database", "mydb" );
		config.properties.put( "protocol", "invalid" );

		assertThrows( IllegalArgumentException.class, () -> {
			driver.buildConnectionURL( config );
		} );
	}

	@DisplayName( "Build a a connection with a protocol" )
	@Test
	public void testBuildConnectionURLHttp() {
		MySQLDriver			driver	= new MySQLDriver();
		DatasourceConfig	config	= new DatasourceConfig();
		config.properties.put( "driver", "Mysql" );
		config.properties.put( "database", "mydb" );
		config.properties.put( "protocol", "loadbalance" );
		config.properties.put( "host", "localhost" );

		String expectedURL = "jdbc:mysql:loadbalance://localhost:3306/mydb?";
	}

}
