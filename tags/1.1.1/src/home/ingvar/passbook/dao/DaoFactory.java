package home.ingvar.passbook.dao;

import home.ingvar.passbook.dao.h2.H2DaoFactory;
import home.ingvar.passbook.lang.Exceptions;
import home.ingvar.passbook.utils.I18n;

/**
 * Класс разделяющий уровень приложения и уровень данных
 * 
 * База данных должна включать в себя следующие обязательные данные:
 * Schema:
 *     - passbook
 * Tables:
 *     - users
 *         > id long auto increment primary key
 *         > username varchar(20) not null unique
 *         > password char(64) not null - contains hash sha256(password + username)
 *         > fullname varchar(40)
 *     - items
 *         > id long auto increment primary key
 *         > owner_id long references (users.id) not null
 *         > service varchar(100) not null
 *         > username varchar(100) not null
 *         > password varchar(100)
 *         > comment varchar(200)
 *         
 * Все данные в таблице items, кроме id, owner_id необходимо шифровать используя пароль пользователя(именно пароль, а не его хэш)
 * 
 * @author ingvar
 * @version 0.3
 *
 */
public abstract class DaoFactory {
	
	/**
	 * H2 database (http://www.h2database.com)
	 */
	public static final int H2 = 1;
	/**
	 * SQLite database
	 */
	public static final int SQLITE = 2;
	
	public static final Object[][] STORAGES = {
		{"H2 database", DaoFactory.H2},
		//{"SQLite database", DaoFactory.SQLITE}
	};
	
	/**
	 * Create DaoFactory instance
	 * 
	 * @param type DaoFactory type. (e.q. DaoFactory.<b>H2</b>)
	 * @return new instance
	 * @throws InstantiationException if type not defined
	 */
	public static DaoFactory newInstance(int type) throws InstantiationException {
		switch(type) {
			case H2: return new H2DaoFactory();
		}
		throw new InstantiationException(I18n.getInstance().getException(Exceptions.NO_DAO_INSTANCE));
	}
	
	/**
	 * Return UserDAO object for manipulating this <i>User</i> data
	 * 
	 * @return an UserDAO object
	 */
	public abstract UserDAO getUserDAO();
	/**
	 * Return ItemDAO object for manipulating this <i>Item</i> data
	 * 
	 * @return an ItemDAO object
	 */
	public abstract ItemDAO getItemDAO();
	/**
	 * Test connect to database
	 */
	public abstract boolean test();
	/**
	 * Create new data storage and initialize its structure
	 * 
	 * @throws ResultException if something wrong
	 */
	public abstract void install() throws ResultException;
	/**
	 * Open connection to datastore
	 */
	public abstract void open();
	/**
	 * Open connection to storage or not
	 */
	public abstract boolean isOpen();
	/**
	 * Close connection to datastore
	 */
	public abstract void close();
	
}
