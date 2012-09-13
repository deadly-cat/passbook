package home.ingvar.passbook.ui.views;

import home.ingvar.passbook.dao.ResultException;
import home.ingvar.passbook.lang.Labels;
import home.ingvar.passbook.transfer.Item;
import home.ingvar.passbook.ui.AbstractPanel;
import home.ingvar.passbook.ui.Form;
import home.ingvar.passbook.ui.GBH;
import home.ingvar.passbook.ui.ItemsTableModel;
import home.ingvar.passbook.ui.dialogs.Dialog;
import home.ingvar.passbook.ui.res.IMG;
import home.ingvar.passbook.utils.LOG;
import home.ingvar.passbook.utils.PROPS;

import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;

public class MainPanel extends AbstractPanel {

	private static final long serialVersionUID = 1L;

	private ItemsTableModel model;
	private JTable table;
	private TableRowSorter<ItemsTableModel> sorter;
	private JLabel lblStatus;
	private JLabel lblService;
	private JTextField fldService;
	private JButton btnAdd;
	private JButton btnEdit;
	private JButton btnDelete;
	private JButton btnProfile;
	private JButton btnLogout;
	
	private Action actAdd;
	private Action actEdit;
	private Action actDelete;
	
	private PasswordCopier copier;
	
	public MainPanel() {
		lblStatus  = new JLabel();
		lblService = new JLabel();
		fldService = new JTextField(15);
		actAdd = new ViewItemAction("", new ImageIcon(IMG.ADD_ITEM.getImage()), true);
		actEdit = new ViewItemAction("", new ImageIcon(IMG.EDIT_ITEM.getImage()), false);
		actDelete = new DeleteItemsAction("", new ImageIcon(IMG.DELETE_ITEM.getImage()));
		btnAdd = new JButton(actAdd);
		btnEdit = new JButton(actEdit);
		btnDelete = new JButton(actDelete);
		btnProfile = new JButton(new ImageIcon(IMG.USER.getImage()));
		btnLogout = new JButton(new ImageIcon(IMG.EXIT.getImage()));
		
		btnProfile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				show(Form.PROFILE);
			}
		});
		btnLogout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getRoot().logout();
			}
		});
		fldService.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent event) {
				filter(fldService.getText());
			}
			@Override
			public void insertUpdate(DocumentEvent event) {
				filter(fldService.getText());
			}
			@Override
			public void changedUpdate(DocumentEvent event) {
				filter(fldService.getText());
			}
		});
	}
	
	@Override
	protected AbstractPanel postConstruct() {
		copier = new PasswordCopier();
		model  = new ItemsTableModel(getItemDAO());
		table  = new JTable(model);
		sorter = new TableRowSorter<ItemsTableModel>(model);
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
					copier.copy(item.getPassword());
				}
			}
		});
		JPopupMenu popup = new JPopupMenu();
		popup.add(new JMenuItem(actAdd));
		popup.add(new JMenuItem(actEdit));
		popup.add(new JMenuItem(actDelete));
		table.setComponentPopupMenu(popup);
		
		setLayout(new GridBagLayout());
		//1
		add(btnAdd, GBH.get(5, 5, 5, 0));
		add(btnEdit, GBH.get(5, 5, 5, 0));
		add(btnDelete, GBH.get(5, 5, 5, 0));
		add(lblService, GBH.get(5, 5, 5, 0));
		add(fldService, GBH.get().fill(GBH.HORIZONTAL).weightx(0.6));
		add(new JLabel(), GBH.get(0, 0, 0, 0).weightx(0.4));
		add(btnProfile, GBH.get(5, 5, 5, 0));
		add(btnLogout, GBH.get());
		add(new JLabel(), GBH.get(0, 0, 0, 0).width(GBH.REMAINDER));
		//2
		add(new JScrollPane(table), GBH.get().fill(GBH.BOTH).weightx(1.0).weighty(1.0).width(8));
		add(new JLabel(), GBH.get(0, 0, 0, 0).width(GBH.REMAINDER));
		//3
		add(lblStatus, GBH.get(0, 0, 5, 5).width(GBH.RELATIVE).anchor(GBH.LINE_END));
		add(new JLabel(), GBH.get(0, 0, 0, 0).width(GBH.REMAINDER));
		
		return this;
	}
	
	@Override
	protected void preShow() {
		fldService.setText("");
		try {
			List<Item> items = getItemDAO().list(getUser());
			model.loadItems(items);
		} catch(ResultException e) {
			LOG.error(getText(Labels.TITLE_ERROR), e.getMessage(), e);
		}
	}

	@Override
	protected void updateI18n() {
		lblService.setText(getText(Labels.LABELS_SERVICE) + ":");
		actAdd.putValue(Action.NAME, getText(Labels.BUTTONS_ITEM_ADD));
		actEdit.putValue(Action.NAME, getText(Labels.BUTTONS_ITEM_EDIT));
		actDelete.putValue(Action.NAME, getText(Labels.BUTTONS_ITEM_DELETE));
		btnAdd.setText("");
		btnEdit.setText("");
		btnDelete.setText("");
		for(int i = 0; i < table.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setHeaderValue(model.getColumnName(i));
		}
		copier.updateI18n();
		repaint();
	}
	
	@Override
	protected JButton getDefaultButton() {
		return null;
	}
	
	protected void setPasswordTimeout(int timeout) {
		copier.setTimeout(timeout);
	}

	private void filter(String expression) {
		if(model.getRowCount() > 0) {
			RowFilter<ItemsTableModel, Object> rf = RowFilter.regexFilter(expression, 0);
			sorter.setRowFilter(rf);
		}
	}
	
	// ------------- INNER CLASSES ------------- //
	
	private class PasswordCopier {
		
		private final StringSelection EMPTY;
		private final Clipboard CLIPBOARD;
		private String message;
		private int timeout; //in seconds
		private volatile boolean isRunning;
		private volatile boolean mayStart;
		
		public PasswordCopier() {
			EMPTY = new StringSelection("");
			CLIPBOARD = Toolkit.getDefaultToolkit().getSystemClipboard();
			message   = getText(Labels.MESSAGES_COPY_PASSWORD);
			timeout   = PROPS.getInstance().getPasswordTimeout();
			isRunning = false;
			mayStart  = true;
		}
		
		public void copy(String password) {
			stop();
			isRunning = true;
			mayStart = false;
			StringSelection data = new StringSelection(password);
			CLIPBOARD.setContents(data, null);
			start();
		}
		
		public void setTimeout(int timeout) {
			this.timeout = timeout;
		}
		
		public void updateI18n() {
			message = getText(Labels.MESSAGES_COPY_PASSWORD);
		}
		
		private void stop() {
			isRunning = false;
			while(!mayStart);
		}
		
		private void start() {
			new Thread() {
				@Override
				public void run() {
					int timer = timeout + 1;
					while(timer--> 1 && isRunning) {
						lblStatus.setText(message + timer);
						try {Thread.sleep(1000);}catch(InterruptedException e){}
					}
					clear();
					isRunning = false;
					mayStart = true;
				}
			}.start();
		}
		
		private void clear() {
			lblStatus.setText("");
			CLIPBOARD.setContents(EMPTY, null);
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
			Item item = Dialog.getItemDialog().showDialog(getUser());
			if(item != null) {
				try {
					model.addItem(item);
				} catch(ResultException e) {
					LOG.error(getText(Labels.TITLE_ERROR), e.getMessage(), e);
				}
			}
		}
		
		private void edit() {
			int viewRow = table.getSelectedRow();
			if(viewRow == -1) {
				LOG.warn(getText(Labels.TITLE_WARNING), getText(Labels.MESSAGES_ITEM_NOT_SELECT), null);
				return;
			}
			int row = table.convertRowIndexToModel(viewRow);
			Item item = Dialog.getItemDialog().showDialog(model.getItem(row).clone());
			if(item != null) {
				try {
					model.updateItem(item);
				} catch(ResultException e) {
					LOG.error(getText(Labels.TITLE_ERROR), e.getMessage(), e);
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
				LOG.warn(getText(Labels.TITLE_WARNING), getText(Labels.MESSAGES_ITEM_NOT_SELECT), null);
				return;
			}
			try {
				for(int i = 0; i < rows.length; i++) {
					int row = table.convertRowIndexToModel(rows[i]);
					model.removeItem(row);
				}
				model.fireTableRowsDeleted(0, table.getRowCount() - 1);
			} catch(ResultException e) {
				LOG.error(getText(Labels.TITLE_ERROR), e.getMessage(), e);
			}
		}
		
	}

}
