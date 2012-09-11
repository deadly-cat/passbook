package home.ingvar.passbook.ui;

import home.ingvar.passbook.lang.Labels;

import javax.swing.UIManager;

public enum Theme {
	
	SYSTEM(Labels.LABELS_SYSTEM, UIManager.getSystemLookAndFeelClassName()),
	STANDART(Labels.LABELS_STANDARD, UIManager.getCrossPlatformLookAndFeelClassName());
	
	private String i18nName;
	private String className;
	
	Theme(String i18nName, String className) {
		this.i18nName  = i18nName;
		this.className = className;
	}
	
	public String getI18nName() {
		return i18nName;
	}
	
	public String getClassName() {
		return className;
	}
	
}
