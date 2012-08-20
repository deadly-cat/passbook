package home.ingvar.passbook.ui;

import home.ingvar.passbook.utils.I18n;

import java.awt.Font;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Menu {

	private JMenuBar bar;
	private I18n i18n;
	private Map<JMenu, List<JMenuItem>> groups;
	private Map<JMenuItem, String> localized;
	private Map<JMenu, JMenuItem> chosen;
	
	public Menu() {
		bar = new JMenuBar();
		i18n = I18n.getInstance();
		groups = new HashMap<JMenu, List<JMenuItem>>();
		localized = new HashMap<JMenuItem, String>();
		chosen = new HashMap<JMenu, JMenuItem>();
	}
	
	public JMenuBar getBar() {
		return bar;
	}
	
	public JMenu addMenu(String i18nName) {
		return addMenu(null, i18nName);
	}
	
	public JMenu addMenu(JMenu parent, String i18nName) {
		JMenu menu = new JMenu();
		localized.put(menu, i18nName);
		if(parent == null) {
			bar.add(menu);
		} else {
			parent.add(menu);
		}
		return menu;
	}
	
	public JMenuItem addMenuItem(JMenu parent, String i18nName) {
		JMenuItem item = addMenuItemUnilocale(parent, "");
		localized.put(item, i18nName);
		return item;
	}
	
	public JMenuItem addMenuItemUnilocale(JMenu parent, String name) {
		JMenuItem item = new JMenuItem(name);
		parent.add(item);
		List<JMenuItem> items = groups.get(parent);
		if(items == null) {
			items = new LinkedList<JMenuItem>();
			groups.put(parent, items);
		}
		items.add(item);
		return item;
	}
	
	public void chose(JMenuItem item) {
		JMenu p = findParent(item);
		chosen.put(p, item);
		
		Font df = p.getFont();
		Font bf = new Font(df.getFamily(), df.isBold() ? (Font.BOLD | Font.ITALIC) : Font.BOLD, df.getSize());
		for(JMenuItem m : groups.get(p)) {
			m.setFont(m.equals(item) ? bf : df);
		}
	}
	
	public void updateI18n() {
		for(Map.Entry<JMenuItem, String> e : localized.entrySet()) {
			e.getKey().setText(i18n.get(e.getValue()));
		}
	}
	
	public void updateMenuStyle() {
		for(Map.Entry<JMenu, JMenuItem> e : chosen.entrySet()) {
			chose(e.getValue());
		}
	}
	
	private JMenu findParent(JMenuItem item) {
		for(Map.Entry<JMenu, List<JMenuItem>> e : groups.entrySet()) {
			for(JMenuItem i : e.getValue()) {
				if(item.equals(i)) {
					return e.getKey();
				}
			}
		}
		return null;
	}
	
}
