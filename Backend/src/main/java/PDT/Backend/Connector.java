package PDT.Backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Connector {
	private Connection connection;
	private Statement statement;
	private static final Connector INSTANCE = new Connector();

	public static Connector getInstance() {
		return INSTANCE;
	}

	public Connector() {
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/PDT", "postgres", "admin");
			statement = connection.createStatement();

		} catch (SQLException e) {
			Logger.getLogger("Database Connector").log(Level.SEVERE, "could not create connection", e);
		} catch (ClassNotFoundException e) {
			Logger.getLogger("Database Connector").log(Level.SEVERE, "could not create connection", e);
		}
	}

	public Statement getStatement() {
		return statement;
	}

	public void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			Logger.getLogger("Database Connector").log(Level.SEVERE, "could not close connection", e);
		}
	}

}
