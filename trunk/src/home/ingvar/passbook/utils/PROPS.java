package home.ingvar.passbook.utils;

import home.ingvar.passbook.dao.DaoFactory;
import home.ingvar.passbook.lang.Exceptions;
import home.ingvar.passbook.lang.Labels;
import home.ingvar.passbook.ui.Theme;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PROPS {
	
	private static final String PROP_FILENAME = "config";
	
	private static final String DB = "db";
	private static final String LANG = "lang";
	private static final String THEME = "theme";
	private static final String PASS_TIMEOUT = "pt";
	private static final String PASS_LENGHT = "pl";
	
	private static final int DEF_DB = DaoFactory.H2;
	private static final String DEF_LANG = "en";
	private static final String DEF_THEME = Theme.STANDART.toString();
	private static final int DEF_PT = 15;
	private static final int DEF_PL = 8;
	
	private static PROPS INSTANCE;
	private final Properties properties;
	private boolean isChanged;
	
	public static synchronized PROPS getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new PROPS();
		}
		return INSTANCE;
	}
	
	private PROPS() {
		properties = new Properties();
		loadProperties();
	}
	
	public int getDB() {
		return get(DB, DEF_DB);
	}
	
	public void setDB(int db) {
		set(DB, db);
	}
	
	public String getLang() {
		return get(LANG, DEF_LANG);
	}
	
	public void setLang(String lang) {
		set(LANG, lang);
	}
	
	public String getTheme() {
		return get(THEME, DEF_THEME);
	}
	
	public void setTheme(String theme) {
		set(THEME, theme);
	}
	
	public int getPasswordTimeout() {
		return get(PASS_TIMEOUT, DEF_PT);
	}
	
	public void setPasswordTimeout(int timeout) {
		set(PASS_TIMEOUT, timeout);
	}
	
	public int getPasswordLenght() {
		return get(PASS_LENGHT, DEF_PL);
	}
	
	public void setPasswordLenght(int lenght) {
		set(PASS_LENGHT, lenght);
	}
	
	public boolean isChanged() {
		return isChanged;
	}
	
	public void saveProperties() {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(PROP_FILENAME);
			properties.store(out, "");
			isChanged = false;
			
		} catch(IOException e) {
			LOG.error(I18n.getInstance().get(Labels.TITLE_PROPERTIES_SAVE), I18n.getInstance().getException(Exceptions.PROPERTIES_SAVE_FAIL), e);
			
		} finally {
			if(out != null) {try {out.close();} catch (IOException e) {}}
		}
	}
	
	private void loadProperties() {
		FileInputStream in = null;
		try {
			in = new FileInputStream(PROP_FILENAME);
			properties.load(in);
			
		} catch(IOException e) {
			LOG.warn(I18n.getInstance().get(Labels.TITLE_PROPERTIES_LOAD), I18n.getInstance().getException(Exceptions.PROPERTIES_LOAD_FAIL), e);
			setDB(DEF_DB);
			setLang(DEF_LANG);
			setTheme(DEF_THEME);
			setPasswordTimeout(DEF_PT);
			setPasswordLenght(DEF_PL);
			saveProperties();
			
		} finally {
			if(in != null) {try {in.close();} catch (IOException e) {}}
		}
	}
	
	private void set(String key, String value) {
		isChanged = true;
		properties.setProperty(key, value);
	}
	
	private void set(String key, int value) {
		set(key, Integer.toString(value));
	}
	
	private String get(String key, String def) {
		String p = properties.getProperty(key);
		if(p == null) {
			set(key, def);
			return def;
		}
		return p;
	}
	
	private int get(String key, int def) {
		try {
			return Integer.valueOf(get(key, Integer.toString(def)));
		} catch (NumberFormatException e) {
			LOG.warn(I18n.getInstance().get(Labels.TITLE_INCOMPOTIBLE_NUMBER), I18n.getInstance().getException(Exceptions.PROPERTY_LOAD_FAIL) + key, e);
			return def;
		}
	}
	
}
