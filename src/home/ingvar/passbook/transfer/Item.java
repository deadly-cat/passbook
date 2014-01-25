package home.ingvar.passbook.transfer;

import java.io.Serializable;
import java.util.Date;

/**
 * Объект агрегирующий информацию о единице данных пользователя.
 * 
 * @author ingvar
 * @version 0.1
 *
 */
public class Item implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;
	private long id;
	private User owner;
	private String service;
	private String username;
	private String password;
	private String comment;
	private Date modifyDate;
	
	public Item() {}
	
	public Item(User owner) {
		this();
		this.owner = owner;
	}
	
	private Item(Item item) {
		id = item.id;
		owner    = item.owner;
		service  = item.service;
		username = item.username;
		password = item.password;
		comment  = item.comment;
		modifyDate = item.modifyDate;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
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
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	@Override
	public int hashCode() {
		int hash = 4;
		return owner.hashCode() * hash + ((int) id) * hash + hash;
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
		return owner.equals(o.owner) && id == o.id;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(id).append("] ");
		sb.append(service).append(" > ");
		sb.append(username);
		/*sb.append(":").append(password);
		sb.append(" (").append(comment).append(")");*/
		
		return sb.toString();
	}
	
	@Override
	public Item clone() {
		return new Item(this);
	}
	
}