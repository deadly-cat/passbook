package home.ingvar.passbook.ui;

import home.ingvar.passbook.dao.DaoFactory;
import home.ingvar.passbook.dao.ItemDAO;
import home.ingvar.passbook.dao.UserDAO;
import home.ingvar.passbook.lang.Labels;
import home.ingvar.passbook.transfer.User;
import home.ingvar.passbook.ui.menu.IMenuItem;
import home.ingvar.passbook.ui.menu.Menu;
import home.ingvar.passbook.ui.menu.MenuItem;
import home.ingvar.passbook.ui.res.IMG;
import home.ingvar.passbook.ui.views.InstallPanel;
import home.ingvar.passbook.ui.views.LoginPanel;
import home.ingvar.passbook.ui.views.MainPanel;
import home.ingvar.passbook.ui.views.ProfilePanel;
import home.ingvar.passbook.ui.views.RegPanel;
import home.ingvar.passbook.utils.I18n;
import home.ingvar.passbook.utils.LOG;
import home.ingvar.passbook.utils.PROPS;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_WIDTH = 640;
	private static final int DEFAULT_HEIGHT = 480;
	
	private final PROPS properties;
	private final I18n i18n;
	private final JMenuBar menuBar;
	private final List<IMenuItem> menuItems;
	
	private AbstractPanel view; //current view
	private User user; //current user
	
	private DaoFactory daoFactory;
	private UserDAO userDAO;
	private ItemDAO itemDAO;
	
	public MainFrame() {
		properties = PROPS.getInstance();
		i18n = I18n.getInstance();
		i18n.setLocale(properties.getLang());
		setPreference();
		
		menuBar = new JMenuBar();
		menuItems = new LinkedList<IMenuItem>();
		setJMenuBar(menuBar);
		createMenu();
		//set theme
		try {
			UIManager.setLookAndFeel(Themes.valueOf(properties.getTheme()).getClassName());
		} catch (Exception e) {
			LOG.error(i18n.get(Labels.TITLE_ERROR), "Can't load system theme.\nUsing default", e); //TODO:
			try {UIManager.setLookAndFeel(Themes.STANDART.getClassName());} catch(Exception ex) {}
			properties.setTheme(Themes.STANDART.toString());
		}
		//chose view
		Form form = null;
		try {
			int db = properties.getDB();
			daoFactory = DaoFactory.newInstance(db);
			userDAO = daoFactory.getUserDAO();
			itemDAO = daoFactory.getItemDAO();
			form = daoFactory.test() ? Form.LOGIN : Form.INSTALL;
		} catch(InstantiationException e) {
			LOG.error(i18n.get(Labels.TITLE_ERROR), "Can't create connection to storage.\nMaybe config file was incorrect.\nOpen setting to check it\nor create new storage", e); //TODO:
			form  = Form.INSTALL;
		}
		createForms();
		nextView(form);
	}
	
	public DaoFactory getDaoFactory() {
		return daoFactory;
	}
	
	public UserDAO getUserDAO() {
		return userDAO;
	}
	
	public ItemDAO getItemDAO() {
		return itemDAO;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
		updateTitle();
	}
	
	public void nextView(Form form) {
		if(view != null) {
			remove(view);
		}
		view = form.getPanel();
		view.init();
		view.updateI18n();
		getRootPane().setDefaultButton(view.getDefaultButton());
		add(view);
		view.revalidate();
		repaint();
	}
	
	public void logout() {
		daoFactory.close();
		setUser(null);
		nextView(Form.LOGIN);
	}
	
	public void setStorage(DaoFactory factory, int id) {
		properties.setDB(id);
		daoFactory = factory;
		userDAO = daoFactory.getUserDAO();
		itemDAO = daoFactory.getItemDAO();
	}
	
	private void setPreference() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screen = tk.getScreenSize();
		
		setTitle(i18n.get(Labels.TITLE_MAIN));
		setIconImage(IMG.FAVICON.getImage());
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setLocation((screen.width - DEFAULT_WIDTH) / 2, (screen.height - DEFAULT_HEIGHT) / 2);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setJMenuBar(menuBar);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				close();				
			}
		});
	}
	
	private void createMenu() {
		JMenu fileMenu = Menu.create(menuBar, menuItems, Labels.MENU_FILE);
		MenuItem.create(fileMenu, menuItems, Labels.MENU_FILE_EXIT, new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		
		//TODO: make current chose bold
		JMenu settingsMenu = Menu.create(menuBar, menuItems, Labels.MENU_SETTINGS);
		Menu langMenu = Menu.create(settingsMenu, menuItems, Labels.MENU_SETTINGS_LANG);
		//Font pf = langMenu.getFont();
		//final Font boldFont = new Font(pf.getFamily(), Font.BOLD, pf.getSize());
		for(final Locale a : I18n.getAvailable()) {
			String lbl = a.getDisplayName(a);
			lbl = lbl.substring(0, 1).toUpperCase() + lbl.substring(1);
			langMenu.add(lbl).addActionListener(new AbstractAction() {
				private static final long serialVersionUID = 1L;
				@Override
				public void actionPerformed(ActionEvent e) {
					i18n.setLocale(a);
					properties.setLang(i18n.getLocale().getLanguage());
					//setFont(boldFont);
					updateI18n();
				}
			});
		}
		Menu themeMenu = Menu.create(settingsMenu, menuItems, Labels.MENU_SETTINGS_THEME);
		for(final Themes theme : Themes.values()) {
			MenuItem.create(themeMenu, menuItems, theme.getI18nName(), new AbstractAction() {
				private static final long serialVersionUID = 1L;
				@Override
				public void actionPerformed(ActionEvent e) {
					setTheme(theme);
				}
			});
		}
		/*for(LookAndFeelInfo lf : UIManager.getInstalledLookAndFeels()) {
			themeMenu.add(lf.getName()).addActionListener(getLFAction(lf.getClassName()));
		}*/
		
		JMenu aboutMenu = Menu.create(menuBar, menuItems, Labels.MENU_HELP);
		MenuItem.create(aboutMenu, menuItems, Labels.MENU_HELP_ABOUT, new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Created by: Igor Zubenko(igor.a.zubenko@gmail.com)\nLicensed by: http://opensource.org/licenses/BSD-3-Clause", "About", JOptionPane.INFORMATION_MESSAGE); //TODO: i18n
			}
		});
		
		for(IMenuItem m : menuItems) {
			m.updateI18n();
		}
	}
	
	private void createForms() {
		Form.INSTALL.setPanel(new InstallPanel(this));
		Form.REGISTER.setPanel(new RegPanel(this));
		Form.LOGIN.setPanel(new LoginPanel(this));
		Form.MAIN.setPanel(new MainPanel(this));
		Form.PROFILE.setPanel(new ProfilePanel(this));
		Form.SETTINGS.setPanel(null); //TODO: create
	}
	
	private void updateTitle() {
		String title = i18n.get(Labels.TITLE_MAIN);
		if(user != null) {
			title += " - " + ((user.getFullname() == null || user.getFullname().isEmpty()) ? user.getUsername() : user.getFullname());
		}
		setTitle(title);
	}
	
	private void updateI18n() {
		updateTitle();
		for(IMenuItem m : menuItems) {
			m.updateI18n();
		}
		view.updateI18n();
	}
	
	private void close() {
		if(properties.isChanged()) {
			properties.saveProperties();
		}
		dispose();
		System.exit(0);
	}
	
	private void setTheme(Themes theme) {
		setVisible(false);
		try {
			UIManager.setLookAndFeel(theme.getClassName());
			properties.setTheme(theme.toString());
		} catch(Exception e) {
			LOG.error(i18n.get(Labels.TITLE_ERROR), e.getMessage(), e);
		}
		setVisible(true);
	}
	
	// ------------------ INNER CLASSES ------------------ //
	
	private enum Themes {
		
		SYSTEM(Labels.LABELS_SYSTEM, UIManager.getSystemLookAndFeelClassName()),
		STANDART(Labels.LABELS_STANDARD, UIManager.getCrossPlatformLookAndFeelClassName());
		
		private String i18nName;
		private String className;
		
		Themes(String i18nName, String className) {
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

}
