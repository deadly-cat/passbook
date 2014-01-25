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
	
	private final H2UserDAO userDAO;
	private final H2ItemDAO itemDAO;
	private JdbcConnectionPool pool;
	
	public H2DaoFactory() {
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
			if(!isOpen()) {
				open();
			}
			pool.getConnection().close();
			close();
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
			state.executeUpdate("CREATE TABLE IF NOT EXISTS passbook.users (id IDENTITY PRIMARY KEY, username VARCHAR(20) NOT NULL UNIQUE, password CHAR(64) NOT NULL, fullname VARCHAR(40))");
			state.executeUpdate("CREATE TABLE IF NOT EXISTS passbook.items (id IDENTITY PRIMARY KEY, owner_id BIGINT REFERENCES passbook.users(id), service VARCHAR(100), username VARCHAR(100), password VARCHAR(100), comment VARCHAR(200), modify_date TIMESTAMP)");
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
	
	@Override
	public boolean isOpen() {
		return pool != null;
	}

	@Override
	public void open() {
		pool = JdbcConnectionPool.create("jdbc:h2:storage;IFEXISTS=TRUE", "passbook", "passbook");
		pool.setMaxConnections(10);
	}

	@Override
	public void close() {
		pool.dispose();
		pool = null;
	}
	
	@Override
	public void update() throws ResultException {
		if(!isOpen()) {
			open();
		}
		updateItemModifyDate();
		close();
	}
	
	private void updateItemModifyDate() throws ResultException {
		Connection connection = null;
		Statement state = null;
		try {
			connection = pool.getConnection();
			state = connection.createStatement();
			//check existing passbook.items.modify_date column
			state.executeQuery("select modify_date from passbook.items");
		} catch(SQLException e) {
			if(state != null && e.getMessage().toLowerCase().indexOf("modify_date") >= 0) {
				try {
					state.executeUpdate("ALTER TABLE passbook.items ADD modify_date TIMESTAMP");
					state.executeUpdate("update passbook.items set modify_date = CURRENT_TIMESTAMP()");
				} catch(SQLException e1) {
					throw new ResultException(e1);
				}
			} else {
				throw new ResultException(e);
			}
		} finally {
			if(connection != null) {
				try{connection.close();} catch(SQLException e) {}
			}
		}
	}

}
