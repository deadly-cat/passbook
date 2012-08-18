package home.ingvar.passbook.ui.menu;

import home.ingvar.passbook.utils.I18n;

import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class Menu extends JMenu implements IMenuItem {

	private static final long serialVersionUID = 1L;
	private final I18n i18n = I18n.getInstance();
	private final String i18nName;
	
	public static JMenu create(JMenuBar parent, List<IMenuItem> menus, String i18nName) {
		Menu menu = new Menu(i18nName);
		parent.add(menu);
		menus.add(menu);
		menu.updateI18n();
		return menu;
	}
	
	public static Menu create(JMenu parent, List<IMenuItem> menus, String i18nName) {
		Menu menu = new Menu(i18nName);
		parent.add(menu);
		menus.add(menu);
		return menu;
	}
	
	public Menu(String i18nName) {
		super("");
		this.i18nName = i18nName;
	}
	
	@Override
	public void updateI18n() {
		setText(i18n.get(i18nName));
	}

}
