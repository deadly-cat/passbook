package home.ingvar.passbook.gui.views;

import home.ingvar.passbook.dao.ResultException;
import home.ingvar.passbook.transfer.Item;
import home.ingvar.passbook.utils.I18n;
import home.ingvar.passbook.gui.ItemsTableModel;
import home.ingvar.passbook.gui.MainFrame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;

import org.apache.log4j.Logger;

/**
 * @author ingvar
 * @version 0.1
 *
 */
public class ItemsPanel extends I18nJPanel {

	private static final Logger logger = Logger.getLogger(ItemsPanel.class);
	private static final long serialVersionUID = 1L;
	private final MainFrame frame;
	private final I18n i18n;
	private ItemDialog itemDialog;
	private JTable table;
	private ItemsTableModel model;
	private TableRowSorter<ItemsTableModel> sorter;
	private JLabel status;
	private JLabel lblService;
	
	private Action addAction;
	private Action editAction;
	private Action deleteAction;
	
	private JButton btnAdd;
	private JButton btnEdit;
	private JButton btnDelete;
	
	public ItemsPanel(MainFrame frame) {
		this.frame = frame;
		this.i18n  = frame.getI18n();
		this.itemDialog = new ItemDialog(frame);
		this.model  = new ItemsTableModel(frame.getItemDAO(), i18n);
		this.table  = new JTable(model);
		this.sorter = new TableRowSorter<ItemsTableModel>(model);
		this.status = new StatusLabel();
		this.lblService = new JLabel();
		this.addAction = new ViewItemAction("", icon("home/ingvar/passbook/gui/resources/add.png"), true);
		this.editAction = new ViewItemAction("", icon("home/ingvar/passbook/gui/resources/edit.png"), false);
		this.deleteAction = new DeleteItemsAction("", icon("home/ingvar/passbook/gui/resources/delete.png"));
		this.btnAdd = new JButton(addAction);
		this.btnEdit = new JButton(editAction);
		this.btnDelete = new JButton(deleteAction);
		
		init();
		buttons();
		rei18n();
	}
	
	public void loadItems(List<Item> items) {
		model.loadItems(items);
	}
	
	@Override
	public void rei18n() {
		lblService.setText(i18n.get("labels.service"));
		addAction.putValue(Action.NAME, i18n.get("buttons.item.add"));
		editAction.putValue(Action.NAME, i18n.get("buttons.item.edit"));
		deleteAction.putValue(Action.NAME, i18n.get("buttons.item.delete"));
		btnAdd.setText("");
		btnEdit.setText("");
		btnDelete.setText("");
		for(int i = 0; i < table.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setHeaderValue(model.getColumnName(i));
		}
		itemDialog.rei18n();
		repaint();
	}
	
	private Icon icon(String path) {
		return new ImageIcon(ClassLoader.getSystemResource(path));
	}
	
	private void init() {
		setLayout(new BorderLayout());
		add(new JScrollPane(table), BorderLayout.CENTER);
		
		sorter.setSortsOnUpdates(true);
		table.setRowSorter(sorter);
		table.setAutoscrolls(true);
		table.getTableHeader().setReorderingAllowed(false); //disable moving column
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent event) {
				int row = table.rowAtPoint(event.getPoint());
				int col = table.columnAtPoint(event.getPoint());
				if(event.getButton() == MouseEvent.BUTTON3) {
					table.setRowSelectionInterval(row, row);
				}
				model.showPassword(-1);
				if(event.getButton() == MouseEvent.BUTTON1 && col == 2) { //password column
					int modelRow = table.convertRowIndexToModel(row);
					model.showPassword(modelRow);
				}
			}
			@Override
			public void mouseClicked(MouseEvent event) {
				if(event.getButton() == MouseEvent.BUTTON1 && event.getClickCount() >= 2) {
					int row = table.convertRowIndexToModel(table.rowAtPoint(event.getPoint()));
					Item item = model.getItem(row);
					StringSelection data = new StringSelection(item.getPassword());
					Clipboard clipboard  = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(data, null);
					status.setText(i18n.get("messages.copy-password"));
				}
			}
		});
		
		FlowLayout layout = new FlowLayout(FlowLayout.RIGHT);
		layout.setVgap(2);
		JPanel statusPanel = new JPanel(layout);
		add(statusPanel, BorderLayout.SOUTH);
		statusPanel.add(status);
		statusPanel.setPreferredSize(new Dimension(frame.getWidth(), status.getFont().getSize() + 10));
	}
	
	private void buttons() {
		JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
		add(controls, BorderLayout.NORTH);
		
		controls.add(btnAdd);
		controls.add(btnEdit);
		controls.add(btnDelete);
		
		controls.add(lblService);
		final JTextField filterField = new JTextField(15);
		filterField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent event) {
				filter(filterField.getText());
			}
			@Override
			public void insertUpdate(DocumentEvent event) {
				filter(filterField.getText());
			}
			@Override
			public void changedUpdate(DocumentEvent event) {
				filter(filterField.getText());
			}
		});
		controls.add(filterField);
		
		JPopupMenu popup = new JPopupMenu();
		popup.add(new JMenuItem(addAction));
		popup.add(new JMenuItem(editAction));
		popup.add(new JMenuItem(deleteAction));
		table.setComponentPopupMenu(popup);
	}
	
	private void filter(String expression) {
		RowFilter<ItemsTableModel, Object> rf = RowFilter.regexFilter(expression, 0);
		sorter.setRowFilter(rf);
	}
	
	private class StatusLabel extends JLabel {
		private static final long serialVersionUID = 1L;
		private static final int TIMEOUT = 5000;
		private Runnable cleaner;
		
		public StatusLabel() {
			cleaner = new Runnable() {
				@Override
				public void run() {
					try {Thread.sleep(TIMEOUT);} catch(InterruptedException e) {}
					setText("");
				}
			};
		}
		
		@Override
		public void setText(String text) {
			super.setText(text);
			if(!text.isEmpty()) {
				new Thread(cleaner).start();
			}
		}
	}
	
	
	
	private class ViewItemAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		private boolean isNew;
		
		public ViewItemAction(String name, Icon icon, boolean isNew) {
			super(name, icon);
			this.isNew = isNew;
		}
		
		@Override
		public void actionPerformed(ActionEvent event) {
			if(isNew) {
				add();
			} else {
				edit();
			}
		}
		
		private void add() {
			Item item = itemDialog.showDialog(frame.getUser());
			if(item != null) {
				try {
					model.addItem(item);
				} catch(ResultException e) {
					logger.error(e);
					JOptionPane.showMessageDialog(frame, e, i18n.get("title.error"), JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		
		private void edit() {
			int viewRow = table.getSelectedRow();
			if(viewRow == -1) {
				JOptionPane.showMessageDialog(frame, i18n.get("messages.item-not-selected"), i18n.get("title.warning"), JOptionPane.WARNING_MESSAGE);
				return;
			}
			int row = table.convertRowIndexToModel(viewRow);
			Item item = itemDialog.showDialog(model.getItem(row).clone());
			if(item != null) {
				try {
					model.updateItem(item);
				} catch(ResultException e) {
					logger.error(e);
					JOptionPane.showMessageDialog(frame, e, i18n.get("title.error"), JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
	
	
	
	private class DeleteItemsAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		
		public DeleteItemsAction(String name, Icon icon) {
			super(name, icon);
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			int rows[] = table.getSelectedRows();
			if(rows.length == 0) {
				JOptionPane.showMessageDialog(frame, i18n.get("messages.item-not-selected"), i18n.get("title.warning"), JOptionPane.WARNING_MESSAGE);
				return;
			}
			try {
				for(int i = 0; i < rows.length; i++) {
					int row = table.convertRowIndexToModel(rows[i]);
					model.removeItem(row);
				}
				model.fireTableRowsDeleted(0, table.getRowCount() - 1);
			} catch(ResultException e) {
				logger.error(e);
				JOptionPane.showMessageDialog(frame, e, i18n.get("title.error"), JOptionPane.ERROR_MESSAGE);
			}
		}
		
	}
	
}