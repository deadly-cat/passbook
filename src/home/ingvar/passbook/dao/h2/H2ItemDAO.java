package home.ingvar.passbook.dao.h2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import home.ingvar.passbook.dao.ItemDAO;
import home.ingvar.passbook.dao.ResultException;
import home.ingvar.passbook.transfer.Item;
import home.ingvar.passbook.transfer.User;

public class H2ItemDAO implements ItemDAO {
	
	private static final String INSERT = "INSERT INTO passbook.items (owner_id, service, username, password, comment) VALUES (?, P_ENCRYPT(?, ?), P_ENCRYPT(?, ?), P_ENCRYPT(?, ?), P_ENCRYPT(?, ?))";
	private static final String UPDATE = "UPDATE passbook.items SET password = P_ENCRYPT(?, ?), comment = P_ENCRYPT(?, ?) WHERE owner_id = ? AND service = P_ENCRYPT(?, ?) AND username = P_ENCRYPT(?, ?)";
	private static final String DELETE = "DELETE passbook.items WHERE owner_id = ? AND service = P_ENCRYPT(?, ?) AND username = P_ENCRYPT(?, ?)";
	private static final String DELETE_ALL = "DELETE passbook.items WHERE owner_id = ?";
	private static final String GET  = "SELECT P_DECRYPT(?, service), P_DECRYPT(?, username), P_DECRYPT(?, password), P_DECRYPT(?, comment) FROM passbook.items WHERE owner_id = ? AND service = P_ENCRYPT(?, ?) AND username = P_ENCRYPT(?, ?)";
	private static final String LIST = "SELECT P_DECRYPT(?, service), P_DECRYPT(?, username), P_DECRYPT(?, password), P_DECRYPT(?, comment) FROM passbook.items WHERE owner_id = ?";
	
	private H2DaoFactory factory;
	
	public H2ItemDAO(H2DaoFactory factory) {
		this.factory = factory;
	}

	@Override
	public void add(Item item) throws ResultException {
		User user = item.getOwner();
		userValidate(user);
		
		Connection connection = null;
		try {
			connection = factory.getConnection();
			PreparedStatement state = connection.prepareStatement(INSERT);
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
			
			int added = state.executeUpdate();
			if(added == 0) {
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
	public void update(Item item) throws ResultException {
		userValidate(item.getOwner());
		User owner = item.getOwner();
		Item exist = get(item.getOwner(), item.getService(), item.getUsername());
		String password = owner.getPassword();
		
		Connection connection = null;
		try {
			connection = factory.getConnection();
			PreparedStatement state = connection.prepareStatement(UPDATE);
			state.setString(1, password);
			state.setString(2, item.getPassword().isEmpty() ? exist.getPassword() : item.getPassword());
			state.setString(3, password);
			state.setString(4, item.getComment().isEmpty() ? exist.getComment() : item.getComment());
			state.setLong(5, owner.getId());
			state.setString(6, password);
			state.setString(7, item.getService());
			state.setString(8, password);
			state.setString(9, item.getUsername());
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
	public void delete(Item item) throws ResultException {
		userValidate(item.getOwner());
		
		Connection connection = null;
		try {
			connection = factory.getConnection();
			PreparedStatement state = connection.prepareStatement(DELETE);
			state.setLong(1, item.getOwner().getId());
			state.setString(2, item.getOwner().getPassword());
			state.setString(3, item.getService());
			state.setString(4, item.getOwner().getPassword());
			state.setString(5, item.getUsername());
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
	public void delete(User owner) throws ResultException {
		userValidate(owner);
		
		Connection connection = null;
		try {
			connection = factory.getConnection();
			PreparedStatement state = connection.prepareStatement(DELETE_ALL);
			state.setLong(1, owner.getId());
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
	public Item get(User owner, String service, String username) throws ResultException {
		userValidate(owner);
		
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
				item.setService(result.getString(1));
				item.setUsername(result.getString(2));
				item.setPassword(result.getString(3));
				item.setComment(result.getString(4));
				return item;
			} else {
				throw new ResultException("Item not found!");
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
		userValidate(owner);
		
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
				item.setService(result.getString(1));
				item.setUsername(result.getString(2));
				item.setPassword(result.getString(3));
				item.setComment(result.getString(4));
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
	
	
	private void userValidate(User user) throws ResultException {
		if(user.getId() <= 0) {
			throw new ResultException("User not found!");
		}
		factory.getUserDAO().get(user.getUsername(), user.getPassword());
	}

}
