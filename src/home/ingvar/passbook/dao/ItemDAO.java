package home.ingvar.passbook.dao;

import java.util.List;

import home.ingvar.passbook.transfer.Item;
import home.ingvar.passbook.transfer.User;

/**
 * Интерфейс определяющий методы для манипуляции данными пользователя
 * 
 * @author ingvar
 * @version 0.1
 *
 */
public interface ItemDAO {
	
	/**
	 * Добавляет новые данные в хранилище.
	 * 
	 * @param item Подготовленные данные для добавления
	 * @throws ResultException Если при валидации или добавлении произошла ошибка
	 */
	public void add(Item item) throws ResultException;
	/**
	 * Обновляет данные уже существующие в хранилище.
	 * Обязательные для заполнения данные owner, service, username, т.к. они составляют primary key
	 * и по ним осуществляется поиск данных в хранилище.
	 * Для обновления доступны поля password, comment.
	 * Чтобы поле не обновлялось оно должно быть null
	 * 
	 * @param item Подготовленные для обновления данные
	 * @throws ResultException Если при валидации или обновлении произошла ошибка
	 */
	public void update(Item item) throws ResultException;
	/**
	 * Удаляет данные из хранилища.
	 * Обязательные для заполнения данные owner, service, username, т.к. они составляют primary key
	 * и по ним осуществляется поиск данных в хранилище.
	 * 
	 * @param item Объект для удаления
	 * @throws ResultException Если при валидации или удалении произошла ошибка
	 */
	public void delete(Item item) throws ResultException;
	/**
	 * Возвращает единичный экземпляр данных по указанным параметрам
	 * 
	 * @param owner Владелец данных
	 * @param service Имя сервиса
	 * @param username Имя пользователя сервиса
	 * @return Объект данных
	 * @throws ResultException Если при валидации или получении произошла ошибка
	 */
	public Item get(User owner, String service, String username) throws ResultException;
	/**
	 * Возвращает список данных пользователя
	 * 
	 * @param owner Владелец данных
	 * @return Список данных пользователя
	 * @throws ResultException Если при валидации или получении произошла ошибка
	 */
	public List<Item> list(User owner) throws ResultException;
	/**
	 * Валидация данных
	 */
	public void validate(Item item) throws ResultException;
	
}
