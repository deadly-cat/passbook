package home.ingvar.passbook.gui;

import java.util.Locale;
import java.util.ResourceBundle;

public class I18n {
	
	private static Locale[] available = {Locale.ENGLISH, new Locale("ru")};
	private Locale current;
	private ResourceBundle resource;
	
	public I18n() {
		this.current = available[0];
		this.resource = ResourceBundle.getBundle("home/ingvar/passbook/lang/passbook", current);
	}
	
	public static Locale[] getAvailable() {
		return available.clone();
	}
	
	public void setLocale(Locale locale) {
		current = locale;
		this.resource = ResourceBundle.getBundle("home/ingvar/passbook/lang/passbook", current);
	}
	
	public Locale getLocale() {
		return current;
	}
	
	public String get(String name) {
		return resource.getString(name);
	}
	
}
