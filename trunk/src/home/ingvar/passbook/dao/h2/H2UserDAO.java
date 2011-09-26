package home.ingvar.passbook.dao.h2;

import home.ingvar.passbook.dao.ResultException;
import home.ingvar.passbook.dao.UserDAO;
import home.ingvar.passbook.transfer.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class H2UserDAO implements UserDAO {
	
	private static final String INSERT = "INSERT INTO passbook.users (username, password, fullname) VALUES (?, P_HASH(?, ?), ?)";
	private static final String UPDATE = "UPDATE passbook.users SET fullname = ? WHERE id = ?";
	private static final String DELETE = "DELETE passbook.users WHERE id = ?";
	private static final String GET = "SELECT id, username, password, fullname FROM passbook.users WHERE username = ? AND password = P_HASH(?, ?)";
	
	private H2DaoFactory factory;
	
	public H2UserDAO(H2DaoFactory factory) {
		this.factory = factory;
	}

	@Override
	public void add(User user) throws ResultException {
		String username = user.getUsername();
		String password = user.getPassword();
		String fullname = user.getFullname() == null || user.getFullname().isEmpty() ? username : user.getFullname();
		if(username == null || username.isEmpty() || password == null) {
			throw new ResultException("Username and password can't be empty");
		}
		if(password.length() < 6) {
			throw new ResultException("Password length must be greater than 6 symbols");
		}
		if(!username.matches("[\\w-_]+")) {
			throw new ResultException("Username can be contains alphabet, numeric and symbols '-','_'");
		}
		
		Connection connection = null;
		try {
			connection = factory.getConnection();
			PreparedStatement state = connection.prepareStatement(INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
			state.setString(1, username);
			state.setString(2, password);
			state.setString(3, username);//salt
			state.setString(4, fullname);
			state.executeUpdate();
			ResultSet id = state.getGeneratedKeys();
			if(id.next()) {
				user.setId(id.getLong(1));
			} else {
				throw new ResultException(state.getWarnings());
			}
		} catch(SQLException e) {
			throw new ResultException(e);
		} finally {
			if(connection != null) {
				try{connection.close();}catch(SQLException e){}
			}
		}
	}

	@Override
	public void update(User user) throws ResultException {
		User valid = get(user.getUsername(), user.getPassword());
		
		Connection connection = null;
		try {
			connection = factory.getConnection();
			PreparedStatement state = connection.prepareStatement(UPDATE);
			state.setString(1, user.getFullname());
			state.setLong(2, valid.getId());
			int updated = state.executeUpdate();
			if(updated == 0) {
				throw new ResultException(state.getWarnings());
			}
		} catch(SQLException e) {
			throw new ResultException(e);
		} finally {
			if(connection != null) {
				try{connection.close();}catch(SQLException e){}
			}
		}
	}

	@Override
	public void delete(User user) throws ResultException {
		User valid = get(user.getUsername(), user.getPassword());
		
		factory.getItemDAO().delete(valid);
		Connection connection = null;
		try {
			connection = factory.getConnection();
			PreparedStatement state = connection.prepareStatement(DELETE);
			state.setLong(1, valid.getId());
			int deleted = state.executeUpdate();
			if(deleted == 0) {
				throw new ResultException(state.getWarnings());
			}
		} catch(SQLException e) {
			throw new ResultException(e);
		} finally {
			if(connection != null) {
				try{connection.close();}catch(SQLException e){}
			}
		}
	}

	@Override
	public User get(String username, String password) throws ResultException {
		Connection connection = null;
		try {
			connection = factory.getConnection();
			PreparedStatement state = connection.prepareStatement(GET);
			state.setString(1, username);
			state.setString(2, password);
			state.setString(3, username); //salt
			ResultSet result = state.executeQuery();
			if(result.next()) {
				User user = new User();
				user.setId(result.getLong(1));
				user.setUsername(username);
				user.setPassword(password);
				user.setFullname(result.getString(4));
				return user;
			} else {
				throw new ResultException("Username or password incorrect");
			}
		} catch(SQLException e) {
			throw new ResultException(e);
		} finally {
			if(connection != null) {
				try{connection.close();}catch(SQLException e){}
			}
		}
	}

}
