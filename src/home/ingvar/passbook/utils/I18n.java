package home.ingvar.passbook.utils;

import java.util.Locale;
import java.util.ResourceBundle;

public class I18n {
	
	private static final String LANG_PATH = "home/ingvar/passbook/lang/passbook";
	private static final Locale[] available = {Locale.ENGLISH, new Locale("ru")};
	private static I18n INSTANCE;
	private Locale current;
	private ResourceBundle resource;
	//TODO: add exceptions bundle
	
	public static synchronized I18n getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new I18n();
		}
		return INSTANCE;
	}
	
	public static Locale[] getAvailable() {
		return available;
	}
	
	public static boolean isAvailable(Locale locale) {
		for(Locale l : available) {
			if(l.equals(locale)) {
				return true;
			}
		}
		return false;
	}
	
	private I18n() {
		current = available[0];
		resource = ResourceBundle.getBundle(LANG_PATH, current);
	}
	
	public void setLocale(String locale) {
		setLocale(new Locale(locale));
	}
	
	public void setLocale(Locale locale) {
		if(isAvailable(locale)) {
			current  = locale;
			resource = ResourceBundle.getBundle(LANG_PATH, current);
		} else {
			LOG.warn("Incompatible language", "This language is incompatible.", null); //TODO: i18n
		}
	}
	
	public Locale getLocale() {
		return current;
	}
	
	public String get(String name) {
		return resource.getString(name);
	}
	
}
