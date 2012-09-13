package home.ingvar.passbook.utils;

import home.ingvar.passbook.lang.Exceptions;
import home.ingvar.passbook.lang.Labels;

import java.io.InputStream;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

public class I18n {
	
	private static final String LANG_PATH = "home/ingvar/passbook/lang/passbook";
	private static final String EXPT_PATH ="home/ingvar/passbook/lang/exceptions";
	private static final String LCNS_PATH = "/home/ingvar/passbook/lang/license";
	private static final Locale[] available = {Locale.ENGLISH, new Locale("ru")};
	private static I18n INSTANCE;
	private Locale current;
	private ResourceBundle resource;
	private ResourceBundle exceptions;
	private String licenseText;
	
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
		exceptions = ResourceBundle.getBundle(EXPT_PATH, current);
		loadLicense();
	}
	
	public void setLocale(String locale) {
		setLocale(new Locale(locale));
	}
	
	public void setLocale(Locale locale) {
		if(isAvailable(locale)) {
			current  = locale;
			resource = ResourceBundle.getBundle(LANG_PATH, current);
			exceptions = ResourceBundle.getBundle(EXPT_PATH, current);
			loadLicense();
		} else {
			LOG.warn(get(Labels.TITLE_LANG_INCOMPOTIBLE), getException(Exceptions.LANG_INCOMPOTIBLE), null);
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
	
	public String getException(String name) {
		return exceptions.getString(name);
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
