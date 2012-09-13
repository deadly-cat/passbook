package home.ingvar.passbook.dao.h2;

import home.ingvar.passbook.dao.ResultException;
import home.ingvar.passbook.dao.UserDAO;
import home.ingvar.passbook.dao.ValidationException;
import home.ingvar.passbook.lang.Exceptions;
import home.ingvar.passbook.lang.Labels;
import home.ingvar.passbook.transfer.User;
import home.ingvar.passbook.utils.I18n;

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
	private I18n i18n;
	
	public H2UserDAO(H2DaoFactory factory) {
		this.factory = factory;
		this.i18n = I18n.getInstance();
	}

	@Override
	public void add(User user) throws ResultException {
		validate(user, false);
		Connection connection = null;
		try {
			connection = factory.getConnection();
			PreparedStatement state = connection.prepareStatement(INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
			state.setString(1, user.getUsername());
			state.setString(2, user.getPassword());
			state.setString(3, user.getUsername());//salt
			state.setString(4, user.getFullname());
			state.executeUpdate();
			ResultSet id = state.getGeneratedKeys();
			if(id.next()) {
				user.setId(id.getLong(1));
			} else {
				throw new ResultException(i18n.getException(Exceptions.PERSIST_ADD));
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
		validate(user, true);
		Connection connection = null;
		try {
			connection = factory.getConnection();
			PreparedStatement state = connection.prepareStatement(UPDATE);
			state.setString(1, user.getFullname());
			state.setLong(2, user.getId());
			if(state.executeUpdate() == 0) {
				throw new ResultException(i18n.getException(Exceptions.PERSIST_UPD));
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
		validate(user, true);
		Connection connection = null;
		try {
			connection = factory.getConnection();
			//before delete user need delete all user items
			String delItems = "DELETE passbook.items WHERE owner_id = " + user.getId();
			connection.createStatement().executeUpdate(delItems);
			PreparedStatement state = connection.prepareStatement(DELETE);
			state.setLong(1, user.getId());
			if(state.executeUpdate() == 0) {
				throw new ResultException(i18n.getException(Exceptions.PERSIST_DEL));
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
				throw new ResultException(i18n.getException(Exceptions.LOGIN_FAIL));
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
	public void changePassword(User user, String newPassword) throws ResultException {
		if(newPassword == null || newPassword.isEmpty()) {
			throw new ValidationException(i18n.get(Labels.MESSAGES_PASSWORD_EMPTY));
		}
		validate(user, true);
		String updItems = String.format("UPDATE passbook.items SET service = P_ENCRYPT('%1$s', P_DECRYPT('%2$s', service)), username = P_ENCRYPT('%1$s', P_DECRYPT('%2$s', username)), password = P_ENCRYPT('%1$s', P_DECRYPT('%2$s', password)), comment = P_ENCRYPT('%1$s', P_DECRYPT('%2$s', comment)) WHERE owner_id = '%3$d'", newPassword, user.getPassword(), user.getId());
		Connection connection = null;
		try {
			connection = factory.getConnection();
			connection.createStatement().executeUpdate(updItems);
			String cngPassword = "UPDATE passbook.users SET password = P_HASH(?, ?) WHERE id = ?";
			PreparedStatement state = connection.prepareStatement(cngPassword);
			state.setString(1, newPassword);
			state.setString(2, user.getUsername());//salt
			state.setLong(3, user.getId());
			if(state.executeUpdate() > 0) {
				user.setPassword(newPassword);
			} else {
				throw new ResultException(i18n.getException(Exceptions.PERSIST_PWD_CNG));
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
	public void validate(User user, boolean exist) throws ResultException {
		if(exist) {
			if(user.getId() <= 0) {
				throw new ValidationException(i18n.getException(Exceptions.UNKNOWN_USER));
			}
			User valid = get(user.getUsername(), user.getPassword());
			if(valid.getId() != user.getId()) {
				throw new ValidationException(i18n.getException(Exceptions.LOGIN_FAIL));
			}
		}
		else {
			String username = user.getUsername();
			String password = user.getPassword();
			if(username == null || username.isEmpty()) {
				throw new ValidationException(i18n.get(Labels.MESSAGES_USERNAME_EMPTY));
			}
			if(!username.matches("^[A-z][\\w-_.]+[\\w]$")) {
				throw new ValidationException(i18n.getException(Exceptions.LOGIN_SYMBOLS));
			}
			if(password == null || password.isEmpty()) {
				throw new ValidationException(i18n.get(Labels.MESSAGES_PASSWORD_EMPTY));
			}
			
			String sqlExist = "SELECT id FROM passbook.users WHERE lower(username) LIKE lower('" + username + "')";
			Connection connection = null;
			try {
				connection = factory.getConnection();
				ResultSet result = connection.createStatement().executeQuery(sqlExist);
				if(result.next()) {
					throw new ValidationException(i18n.getException(Exceptions.USER_EXIST));
				}
			} catch(SQLException e) {
				throw new ResultException(e);
			} finally {
				if(connection != null) {
					try{connection.close();}catch(SQLException e){}
				}
			}
		}
		
		if(user.getFullname() == null || user.getFullname().isEmpty()) {
			user.setFullname(user.getUsername());
		}
	}

}
