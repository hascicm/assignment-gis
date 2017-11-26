package PDT.Backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Connector2 {
	private Connection connection;
	private Statement statement;
	private static final Connector2 INSTANCE = new Connector2();

	public static Connector2 getInstance() {
		return INSTANCE;
	}

	public Connector2() {
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/PDT2", "postgres", "admin");
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
