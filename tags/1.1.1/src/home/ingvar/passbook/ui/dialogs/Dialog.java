package home.ingvar.passbook.ui.dialogs;

import home.ingvar.passbook.ui.AbstractDialog;
import home.ingvar.passbook.ui.MainFrame;

public final class Dialog {

	private static Dialog INSTANCE;
	private AboutDialog about;
	private ErrorDialog error;
	private ItemDialog item;
	
	public static void initialize(MainFrame frame) {
		Dialog dialog = getInstance();
		dialog.init(frame);
	}
	
	public static AboutDialog getAboutDialog() {
		return getInstance().about;
	}
	
	public static ErrorDialog getErrorDialog() {
		return getInstance().error;
	}
	
	public static ItemDialog getItemDialog() {
		return getInstance().item;
	}
	
	@SuppressWarnings("rawtypes")
	public static AbstractDialog[] getDialogs() {
		Dialog d = getInstance();
		return new AbstractDialog[] {d.about, d.error, d.item};
	}
	
	private static synchronized Dialog getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new Dialog();
		}
		return INSTANCE;
	}
	
	private Dialog() {}
	
	private void init(MainFrame frame) {
		about = new AboutDialog(frame);
		error = new ErrorDialog(frame);
		item  = new ItemDialog(frame);
	}
	
}
