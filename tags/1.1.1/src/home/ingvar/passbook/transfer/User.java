package home.ingvar.passbook.transfer;

import java.io.Serializable;

/**
 * Объект агрегирующий информацию о пользователе
 * 
 * @author ingvar
 * @version 0.1
 *
 */
public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id;
	private String fullname;
	private String username;
	private String password;
	
	public User(String username, String password, String fullname) {
		this.id = 0;
		this.username = username;
		this.password = password;
		this.fullname = fullname == null ? username : fullname;
	}
	
	public User() {
		this("", "", "");
	}
	
	public void fill(User user) {
		id = user.id;
		fullname = user.fullname;
		username = user.username;
		password = user.password;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
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
	
	@Override
	public int hashCode() {
		int hash = 4;
		return (int) id * hash + hash;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if(!(obj instanceof User)) {
			return false;
		}
		User o = (User) obj;
		return id == o.id;
	}
	
	@Override
	public String toString() {
		return (fullname == null || fullname.isEmpty()) ? username : fullname;
	}
	
}