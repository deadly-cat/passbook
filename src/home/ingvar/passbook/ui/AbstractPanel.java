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
	
	public void show(Form form) {
		frame.nextView(form);
	}
	
	public void back() {
		frame.prevView();
	}
	
	protected String getText(String name) {
		return i18n.get(name);
	}
	
	protected String getLicense() {
		return i18n.getLicenseText();
	}
	
	protected MainFrame getRoot() {
		return frame;
	}
	
	protected DaoFactory getDaoFactory() {
		return frame.getDaoFactory();
	}
	
	protected UserDAO getUserDAO() {
		return frame.getUserDAO();
	}
	
	protected ItemDAO getItemDAO() {
		return frame.getItemDAO();
	}
	
	protected User getUser() {
		return frame.getUser();
	}
	
	protected void setUser(User user) {
		frame.setUser(user);
	}
	
	protected AbstractPanel inject(MainFrame frame) {
		this.frame = frame;
		this.i18n  = I18n.getInstance();
		return this;
	}
	
	protected AbstractPanel postConstruct() {
		return this;
	}
	
	protected abstract void preShow();
	protected abstract void updateI18n();
	protected abstract JButton getDefaultButton();
	
}
