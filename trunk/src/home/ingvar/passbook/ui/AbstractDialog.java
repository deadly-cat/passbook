package home.ingvar.passbook.ui;

import home.ingvar.passbook.ui.res.IMG;
import home.ingvar.passbook.utils.I18n;

import java.awt.Point;

import javax.swing.JButton;
import javax.swing.JDialog;

public abstract class AbstractDialog<T> extends JDialog {

	private static final long serialVersionUID = 1L;
	private I18n i18n;
	private MainFrame frame;
	
	public AbstractDialog(MainFrame frame) {
		this.frame = frame;
		this.i18n  = I18n.getInstance();
		setIconImage(IMG.FAVICON.getImage());
		setResizable(false);
	}
	
	public T showDialog() {
		pack();
		getRootPane().setDefaultButton(getDefaultButton());
		Point l = frame.getLocation();
		int w = frame.getWidth();
		int h = frame.getHeight();
		setLocation(l.x + (w - getWidth()) / 2, l.y + (h - getHeight()) / 2);
		setVisible(true);
		return getResult();
	}
	
	public T showDialog(Object... params) { //default implementation
		return showDialog();
	}
	
	protected String getText(String name) {
		return i18n.get(name);
	}
	
	protected String getLicense() {
		return i18n.getLicenseText();
	}
	
	public abstract void updateI18n();
	protected abstract T getResult();
	protected abstract JButton getDefaultButton();

}
