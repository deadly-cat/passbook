package home.ingvar.passbook.ui;

import home.ingvar.passbook.dao.DaoFactory;
import home.ingvar.passbook.dao.ItemDAO;
import home.ingvar.passbook.dao.UserDAO;
import home.ingvar.passbook.transfer.User;
import home.ingvar.passbook.utils.I18n;

import javax.swing.JButton;
import javax.swing.JPanel;

public abstract class AbstractPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private I18n i18n;
	private MainFrame frame;
	
	public AbstractPanel(MainFrame frame) {
		this.frame = frame;
		this.i18n  = I18n.getInstance();
	}
	
	public void show(Form form) {
		frame.nextView(form);
	}
	
	public String getText(String name) {
		return i18n.get(name);
	}
	
	public String getLicense() {
		return i18n.getLicenseText();
	}
	
	public MainFrame getRoot() {
		return frame;
	}
	
	public DaoFactory getDaoFactory() {
		return frame.getDaoFactory();
	}
	
	public UserDAO getUserDAO() {
		return frame.getUserDAO();
	}
	
	public ItemDAO getItemDAO() {
		return frame.getItemDAO();
	}
	
	public User getUser() {
		return frame.getUser();
	}
	
	public void setUser(User user) {
		frame.setUser(user);
	}
	
	protected abstract void init();
	protected abstract void updateI18n();
	protected abstract JButton getDefaultButton();
	
}
