package home.ingvar.passbook.utils;

import java.io.InputStream;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

public class I18n {
	
	private static final String LANG_PATH = "home/ingvar/passbook/lang/passbook";
	private static final String LCNS_PATH = "/home/ingvar/passbook/lang/license";
	private static final Locale[] available = {Locale.ENGLISH, new Locale("ru")};
	private static I18n INSTANCE;
	private Locale current;
	private ResourceBundle resource;
	private String licenseText;
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
		loadLicense();
	}
	
	public void setLocale(String locale) {
		setLocale(new Locale(locale));
	}
	
	public void setLocale(Locale locale) {
		if(isAvailable(locale)) {
			current  = locale;
			resource = ResourceBundle.getBundle(LANG_PATH, current);
			loadLicense();
		} else {
			LOG.warn("Incompatible language", "This language is incompatible.\nUsing english language", null); //TODO: i18n
			setLocale(Locale.ENGLISH);
			PROPS.getInstance().setLang(current.getLanguage());
		}
	}
	
	/**
	 * 
	 * @return current locale
	 */
	public Locale getLocale() {
		return current;
	}
	
	public String getLicenseText() {
		return licenseText;
	}
	
	public String get(String name) {
		return resource.getString(name);
	}
	
	private void loadLicense() {
		licenseText = "";
		InputStream io = I18n.class.getResourceAsStream(LCNS_PATH + "_" + current.getLanguage());
		if(io == null) {
			for(Locale l : available) {
				io = I18n.class.getResourceAsStream(LCNS_PATH + "_" + l.getLanguage());
				if(io != null) {
					break;
				}
			}
		}
		if(io != null) {
			StringBuilder tmp = new StringBuilder();
			Scanner in = new Scanner(io);
			while(in.hasNextLine()) {
				tmp.append(in.nextLine()).append("\n");
			}
			licenseText = tmp.toString();
		}
	}
	
}
