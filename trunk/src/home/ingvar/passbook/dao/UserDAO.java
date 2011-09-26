package home.ingvar.passbook.dao;

import home.ingvar.passbook.transfer.User;

/**
 * Интерфейс определяющий методы для манипуляции профилем пользователя 
 * 
 * @author ingvar
 * @version 0.1
 *
 */
public interface UserDAO {

	/**
	 * Добавляет нового пользователя в хранилище.
	 * В передаваемый параметр <i>User</i>, после добавления пользователя в хранилище
	 * устанвливается его id, присвоенный хранилищем
	 *  
	 * @param user Объект агрегирующий данные для добавления
	 * @throws ResultException Если при валидации или добавлении произошла ошибка
	 */
	public void add(User user) throws ResultException;
	/**
	 * Обновляет профиль пользователя.
	 * Для обновления доступно поле fullname.
	 * Обязательны для заполения поля username, password, т.к. по этим полям производится идентификация и авторизация.
	 * 
	 * @param user Объект агрегирующий данные
	 * @throws ResultException Если при авторизации или обновлении произошла ошибка
	 */
	public void update(User user) throws ResultException;
	/**
	 * Удаляет пользователя из базы данных.
	 * Обязательны для заполения поля username, password, т.к. по этим полям производится идентификация и авторизация.
	 * 
	 * @param user Объект агрегирующий данные
	 * @throws ResultException Если при авторизации или удалении произошла ошибка
	 */
	public void delete(User user) throws ResultException;
	/**
	 * Аутентифицирует пользователя и возвращает объект агрегирующий информацию о пользователе.
	 * При удалении польователя необходимо так же удалить все его данные.
	 * 
	 * @param username Имя пользователя
	 * @param password Пароль пользователя
	 * @return Объект агрегирующий информацию о пользователе
	 * @throws ResultException Если аутентификация прошла неудачно
	 */
	public User get(String username, String password) throws ResultException;
	
}
