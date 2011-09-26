package home.ingvar.passbook.transfer;

import java.io.Serializable;

/**
 * Объект агрегирующий информацию о единице данных пользователя.
 * 
 * @author ingvar
 * @version 0.1
 *
 */
@SuppressWarnings("serial")
public class Item implements Serializable, Cloneable {

	private User owner;
	private String service;
	private String username;
	private String password;
	private String comment;
	
	public Item(User owner) {
		this();
		this.owner = owner;
	}
	
	public Item() {
		owner = null;
		service  = "";
		username = "";
		password = "";
		comment  = "";
	}
	
	private Item(Item item) {
		owner    = item.owner;
		service  = item.service;
		username = item.username;
		password = item.password;
		comment  = item.comment;
	}
	
	public User getOwner() {
		return owner;
	}
	
	public void setOwner(User owner) {
		this.owner = owner;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password == null ? "" : password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getComment() {
		return comment == null ? "" : comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@Override
	public int hashCode() {
		int hash = 4;
		return owner.hashCode() * hash + service.hashCode() * hash + username.hashCode() * hash + hash;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		else if(!(obj instanceof Item)) {
			return false;
		}
		Item o = (Item) obj;
		return owner.equals(o.owner) && service.equals(o.service) && username.equals(o.username);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("|").append(owner.toString()).append("\t");
		sb.append("|").append(service).append("\t");
		sb.append("|").append(username).append("\t");
		sb.append("|").append(password).append("\t");
		sb.append("|").append(comment).append("|");
		
		return sb.toString();
	}
	
	@Override
	public Item clone() {
		return new Item(this);
	}
	
}