package home.ingvar.passbook.ui;

/**
 * Contain instances of panels
 * 
 * @author ingvar
 *
 */
public enum Form {
	
	LOGIN,
	REGISTER,
	INSTALL,
	SETTINGS,
	PROFILE,
	MAIN;
	
	private AbstractPanel panel;
	
	public AbstractPanel getPanel() {
		return panel;
	}
	
	public void setPanel(AbstractPanel panel) {
		this.panel = panel;
	}
	
}
