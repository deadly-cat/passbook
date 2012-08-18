package home.ingvar.passbook.ui.menu;

import home.ingvar.passbook.utils.I18n;

import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class MenuItem extends JMenuItem implements IMenuItem {

	private static final long serialVersionUID = 1L;
	private final I18n i18n = I18n.getInstance();
	private final String i18nName;
	
	public static JMenuItem create(JMenu parent, List<IMenuItem> menus, String i18nName, ActionListener listener) {
		MenuItem item = new MenuItem(i18nName);
		item.addActionListener(listener);
		parent.add(item);
		menus.add(item);
		item.updateI18n();
		return item;
	}
	
	public MenuItem(String i18nName) {
		super("");
		this.i18nName = i18nName;
	}
	
	@Override
	public void updateI18n() {
		setText(i18n.get(i18nName));
	}

}
