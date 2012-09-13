package home.ingvar.passbook.dao.h2;

import home.ingvar.passbook.dao.ItemDAO;
import home.ingvar.passbook.dao.ResultException;
import home.ingvar.passbook.lang.Exceptions;
import home.ingvar.passbook.transfer.Item;
import home.ingvar.passbook.transfer.User;
import home.ingvar.passbook.utils.I18n;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class H2ItemDAO implements ItemDAO {
	
	private static final String INSERT = "INSERT INTO passbook.items (owner_id, service, username, password, comment) VALUES (?, P_ENCRYPT(?, ?), P_ENCRYPT(?, ?), P_ENCRYPT(?, ?), P_ENCRYPT(?, ?))";
	//private static final String UPDATE = "UPDATE passbook.items SET password = P_ENCRYPT(?, ?), comment = P_ENCRYPT(?, ?) WHERE owner_id = ? AND service = P_ENCRYPT(?, ?) AND username = P_ENCRYPT(?, ?)";
	private static final String DELETE = "DELETE passbook.items WHERE id = ? AND owner_id = ?";
	private static final String GET  = "SELECT id, P_DECRYPT(?, service), P_DECRYPT(?, username), P_DECRYPT(?, password), P_DECRYPT(?, comment) FROM passbook.items WHERE owner_id = ? AND service = P_ENCRYPT(?, ?) AND username = P_ENCRYPT(?, ?)";
	private static final String LIST = "SELECT id, P_DECRYPT(?, service), P_DECRYPT(?, username), P_DECRYPT(?, password), P_DECRYPT(?, comment) FROM passbook.items WHERE owner_id = ?";
	
	private H2DaoFactory factory;
	private I18n i18n;
	
	public H2ItemDAO(H2DaoFactory factory) {
		this.factory = factory;
		this.i18n = I18n.getInstance();
	}

	@Override
	public void add(Item item) throws ResultException {
		validate(item);
		User user = item.getOwner();		
		Connection connection = null;
		try {
			connection = factory.getConnection();
			PreparedStatement state = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
			String password = user.getPassword();
			
			state.setLong(1, user.getId());
			state.setString(2, password);
			state.setString(3, item.getService());
			state.setString(4, password);
			state.setString(5, item.getUsername());
			state.setString(6, password);
			state.setString(7, item.getPassword());
			state.setString(8, password);
			state.setString(9, item.getComment());
			if(state.executeUpdate() == 0) {
				throw new ResultException(i18n.getException(Exceptions.PERSIST_ADD));
			}
			ResultSet id = state.getGeneratedKeys();
			if(id.next()) {
				item.setId(id.getLong(1));
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
	public void update(Item item) throws ResultException {
		validate(item);
		Connection connection = null;
		try {
			String password = item.getOwner().getPassword();
			connection = factory.getConnection();
			StringBuilder sqlUpdate = new StringBuilder("UPDATE passbook.items SET ");
			if(item.getService() != null) {
				sqlUpdate.append("service = P_ENCRYPT('"+password+"', '"+item.getService()+"')");
			}
			if(item.getUsername() != null) {
				if(!sqlUpdate.toString().endsWith("SET ")) {
					sqlUpdate.append(", ");
				}
				sqlUpdate.append("username = P_ENCRYPT('"+password+"','"+item.getUsername()+"')");
			}
			if(item.getPassword() != null) {
				if(!sqlUpdate.toString().endsWith("SET ")) {
					sqlUpdate.append(", ");
				}
				sqlUpdate.append("password = P_ENCRYPT('"+password+"','"+item.getPassword()+"')");
			}
			if(item.getComment() != null) {
				if(!sqlUpdate.toString().endsWith("SET ")) {
					sqlUpdate.append(", ");
				}
				sqlUpdate.append("comment = P_ENCRYPT('"+password+"','"+item.getComment()+"')");
			}
			sqlUpdate.append(" WHERE id = "+item.getId()+" AND owner_id = "+item.getOwner().getId());
			if(connection.createStatement().executeUpdate(sqlUpdate.toString()) == 0) {
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
	public void delete(Item item) throws ResultException {
		validate(item);
		Connection connection = null;
		try {
			connection = factory.getConnection();
			PreparedStatement state = connection.prepareStatement(DELETE);
			state.setLong(1, item.getId());
			state.setLong(2, item.getOwner().getId());
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
	public Item get(User owner, String service, String username) throws ResultException {
		factory.getUserDAO().validate(owner, true);
		Connection connection = null;
		try {
			connection = factory.getConnection();
			PreparedStatement state = connection.prepareStatement(GET);
			String password = owner.getPassword();
			for(int i = 1; i <= 4; i++) { //for decrypt data fields
				state.setString(i, password);
			}
			state.setLong(5, owner.getId());
			state.setString(6, password);
			state.setString(7, service);
			state.setString(8, password);
			state.setString(9, username);
			
			ResultSet result = state.executeQuery();
			if(result.next()) {
				Item item = new Item();
				item.setOwner(owner);
				item.setId(result.getLong(1));
				item.setService(result.getString(2).trim());
				item.setUsername(result.getString(3).trim());
				item.setPassword(result.getString(4).trim());
				item.setComment(result.getString(5).trim());
				return item;
			} else {
				throw new ResultException(i18n.getException(Exceptions.PERSIST_NOT_FOUND));
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
	public List<Item> list(User owner) throws ResultException {
		factory.getUserDAO().validate(owner, true);
		List<Item> items = new ArrayList<Item>();
		Connection connection = null;
		try {
			connection = factory.getConnection();
			PreparedStatement state = connection.prepareStatement(LIST);
			String password = owner.getPassword();
			for(int i = 1; i <= 4; i++) { //for decrypt data fields
				state.setString(i, password);
			}
			state.setLong(5, owner.getId());
			
			ResultSet result = state.executeQuery();
			while(result.next()) {
				Item item = new Item();
				item.setOwner(owner);
				item.setId(result.getLong(1));
				item.setService(result.getString(2).trim());
				item.setUsername(result.getString(3).trim());
				item.setPassword(result.getString(4).trim());
				item.setComment(result.getString(5).trim());
				items.add(item);
			}
			
		} catch(SQLException e) {
			throw new ResultException(e);
		} finally {
			if(connection != null) {
				try{connection.close();}catch(SQLException e){}
			}
		}
		return items;
	}
	
	@Override
	public void validate(Item item) throws ResultException {
		factory.getUserDAO().validate(item.getOwner(), true);
	}

}
