package home.ingvar.passbook.dao.h2;

import home.ingvar.passbook.dao.DaoFactory;
import home.ingvar.passbook.dao.ItemDAO;
import home.ingvar.passbook.dao.ResultException;
import home.ingvar.passbook.dao.UserDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.h2.jdbcx.JdbcConnectionPool;

public class H2DaoFactory extends DaoFactory {
	
	private final JdbcConnectionPool pool;
	private final H2UserDAO userDAO;
	private final H2ItemDAO itemDAO;
	
	public H2DaoFactory() {
		pool = JdbcConnectionPool.create("jdbc:h2:storage;IFEXISTS=TRUE", "passbook", "passbook");
		pool.setMaxConnections(10);
		userDAO = new H2UserDAO(this);
		itemDAO = new H2ItemDAO(this);
	}
	
	public Connection getConnection() throws SQLException {
		return pool.getConnection();
	}
	
	@Override
	public UserDAO getUserDAO() {
		return userDAO;
	}

	@Override
	public ItemDAO getItemDAO() {
		return itemDAO;
	}
	
	@Override
	public boolean test() {
		try {
			pool.getConnection().close();
			return true;
		} catch(SQLException e) {
			return false;
		}
	}
	
	@Override
	public void install() throws ResultException {
		try {
			Class.forName("org.h2.Driver");
		} catch(ClassNotFoundException e) {
			throw new ResultException(e);
		}
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:h2:storage", "passbook", "passbook");
			Statement state = connection.createStatement();
			state.executeUpdate("CREATE SCHEMA IF NOT EXISTS passbook");
			state.executeUpdate("CREATE TABLE IF NOT EXISTS passbook.users (id IDENTITY, username VARCHAR(20) NOT NULL UNIQUE, password CHAR(64) NOT NULL, fullname VARCHAR(40))");
			state.executeUpdate("CREATE TABLE IF NOT EXISTS passbook.items (owner_id BIGINT REFERENCES passbook.users(id), service VARCHAR(100), username VARCHAR(100), password VARCHAR(100), comment VARCHAR(200), PRIMARY KEY(owner_id, service, username))");
			state.executeUpdate("CREATE ALIAS IF NOT EXISTS P_HASH FOR \"home.ingvar.passbook.dao.h2.StoredProcedure.hash\"");
			state.executeUpdate("CREATE ALIAS IF NOT EXISTS P_ENCRYPT FOR \"home.ingvar.passbook.dao.h2.StoredProcedure.encrypt\""); //P_ENCRYPT(String key, String data) return byte[]
			state.executeUpdate("CREATE ALIAS IF NOT EXISTS P_DECRYPT FOR \"home.ingvar.passbook.dao.h2.StoredProcedure.decrypt\""); //P_DECRYPT(String key, byte[] data) return String
			
		} catch(SQLException e) {
			throw new ResultException(e);
		} finally {
			if(connection != null) {
				try{connection.close();} catch(SQLException e) {}
			}
		}
	}

}
