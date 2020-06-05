package com.example.davsqlite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.*;

@SpringBootApplication
public class DavSqliteApplication {

	private static final Logger log = LoggerFactory.getLogger(DavSqliteApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DavSqliteApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo() {
		return (args) -> {
			Connection connection = null;
			try {
				// create a database connection
				connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
				Statement statement = connection.createStatement();
				statement.setQueryTimeout(30);  // set timeout to 30 sec.

				statement.executeUpdate("drop table if exists person");
				statement.executeUpdate("create table person (id integer, name string)");
				statement.executeUpdate("insert into person values(1, 'leo')");
				statement.executeUpdate("insert into person values(2, 'yui')");
				ResultSet rs = statement.executeQuery("select * from person");
				while (rs.next()) {
					// read the result set
					log.info("name = " + rs.getString("name"));
					log.info("id = " + rs.getInt("id"));
				}
			} catch (SQLException e) {
				// if the error message is "out of memory",
				// it probably means no database file is found
				log.error(e.getMessage());
			} finally {
				try {
					if (connection != null)
						connection.close();
				} catch (SQLException e) {
					// connection close failed.
					log.error(e.getMessage());
				}
			}
		};
	}

}
