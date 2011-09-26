package home.ingvar.passbook.gui.views;

import home.ingvar.passbook.dao.ItemDAO;
import home.ingvar.passbook.dao.ResultException;
import home.ingvar.passbook.transfer.Item;
import home.ingvar.passbook.gui.I18n;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * @author ingvar
 * @version 0.3
 *
 */
public class ItemsTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private final I18n i18n;
	private List<Item> items;
	private ItemDAO itemDAO;
	private int showRow;
	
	public ItemsTableModel(ItemDAO itemDAO, List<Item> items, I18n i18n) {
		this.i18n = i18n;
		this.itemDAO = itemDAO;
		this.items = items;
		this.showRow = -1;
	}
	
	public ItemsTableModel(ItemDAO itemDAO, I18n i18n) {
		this(itemDAO, new ArrayList<Item>(), i18n);
	}
	
	public void loadItems(List<Item> items) {
		this.items.clear();
		this.items.addAll(items);
	}
	
	public Item getItem(int row) {
		return items.get(row);
	}
	
	public void addItem(Item item) throws ResultException {
		itemDAO.add(item);
		items.add(item);
		fireTableRowsInserted(0, items.size() - 1);
	}
	
	public void updateItem(Item item) throws ResultException {
		itemDAO.update(item);
		items.set(items.indexOf(item), item);
	}
	
	public void removeItem(Item item) throws ResultException {
		itemDAO.delete(item);
		items.remove(item);
	}
	
	public void removeItem(int index) throws ResultException {
		removeItem(items.get(index));
	}
	
	public void showPassword(int row) {
		showRow = row;
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		Item item = items.get(row);
		switch(col) {
			case 0:
				return item.getService();
			case 1:
				return item.getUsername();
			case 2:
				return row == showRow ? item.getPassword() : "********";
			case 3:
				return item.getComment();
			default:
				return null;
		}
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public int getRowCount() {
		return items.size();
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}
	
	@Override
	public String getColumnName(int col) {
		switch(col) {
			case 0:
				return i18n.get("labels.service");
			case 1:
				return i18n.get("labels.username");
			case 2:
				return i18n.get("labels.password");
			case 3:
				return i18n.get("labels.comment");
			default:
				return null;
		}
	}
	
	@Override
	public Class<?> getColumnClass(int col) {
		return getValueAt(0, col).getClass();
	}

}
