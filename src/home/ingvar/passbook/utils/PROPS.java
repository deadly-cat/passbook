package home.ingvar.passbook.utils;

import home.ingvar.passbook.dao.DaoFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PROPS {
	
	private static final String PROP_FILENAME = "config";
	private static final String DB = "db";
	private static final String LANG = "lang";
	
	private static final int DEF_DB = DaoFactory.H2;
	private static final String DEF_LANG = "en";
	
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
		try {
			return Integer.valueOf(properties.getProperty(DB), DEF_DB);
		} catch (NumberFormatException e) {
			LOG.warn("Incompatible number", "Config file incorrect.\nUsing default value for database", e);
			return DEF_DB;
		}
	}
	
	public void setDB(int db) {
		set(DB, Integer.toString(db));
	}
	
	public String getLang() {
		return properties.getProperty(LANG, DEF_LANG);
	}
	
	public void setLang(String lang) {
		set(LANG, lang);
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
			LOG.error("Save properties", "Save properties fail.", e); //TODO: i18n
			
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
			LOG.warn("Load properties", "Load properties fail.\nCreate new properties file", e);
			setDB(DEF_DB);
			setLang(DEF_LANG);
			saveProperties();
			
		} finally {
			if(in != null) {try {in.close();} catch (IOException e) {}}
		}
	}
	
	private void set(String key, String value) {
		isChanged = true;
		properties.setProperty(key, value);
	}
	
}
