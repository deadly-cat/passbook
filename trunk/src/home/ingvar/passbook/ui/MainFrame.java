package home.ingvar.passbook.ui;

import home.ingvar.passbook.dao.DaoFactory;
import home.ingvar.passbook.dao.ItemDAO;
import home.ingvar.passbook.dao.UserDAO;
import home.ingvar.passbook.lang.Labels;
import home.ingvar.passbook.transfer.User;
import home.ingvar.passbook.ui.res.IMG;
import home.ingvar.passbook.ui.views.InstallPanel;
import home.ingvar.passbook.ui.views.LoginPanel;
import home.ingvar.passbook.ui.views.MainPanel;
import home.ingvar.passbook.ui.views.ProfilePanel;
import home.ingvar.passbook.ui.views.RegPanel;
import home.ingvar.passbook.utils.I18n;
import home.ingvar.passbook.utils.PROPS;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_WIDTH = 640;
	private static final int DEFAULT_HEIGHT = 480;
	
	private final PROPS properties;
	private final I18n i18n;
	private final JMenuBar menuBar;
	
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
		createMenu();
		setJMenuBar(menuBar);
		
		createForms();
		
		//chose view
		nextView(Form.INSTALL);
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
		add(view);
		view.revalidate();
		repaint();
	}
	
	public void logout() {
		//TODO: close connections
		setUser(null);
		nextView(Form.LOGIN);
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
		
		
		/* TODO:
		int db = Integer.parseInt(properties.getProperty("db", "0"));
		if(db > 0) {
			try {
				DaoFactory factory = DaoFactory.newInstance(db);
				setDAO(factory);
				if(!factory.test()) {
					view = new InstallPanel(this);
				} else {
					view = new AuthPanel(this);
				}
			} catch(InstantiationException e) {
				LOG.error(e.getMessage(), e);
				view = new InstallPanel(this);
			}
		} else {
			view = new InstallPanel(this);
		}
		add(view);
		
		 */
	}
	
	private void createMenu() {
		//file menu (0)
		JMenu fileMenu = new JMenu(i18n.get(Labels.MENU_FILE));
		menuBar.add(fileMenu);
		fileMenu.add(i18n.get(Labels.MENU_FILE_EXIT)).addActionListener(new AbstractAction() { //(0.1)
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		
		//settings menu (1)
		JMenu settingsMenu = new JMenu(i18n.get(Labels.MENU_SETTINGS));
		menuBar.add(settingsMenu);
		
		JMenu langMenu = new JMenu(i18n.get(Labels.MENU_SETTINGS_LANG)); //(1.1)
		settingsMenu.add(langMenu);
		for(final Locale a : I18n.getAvailable()) {
			String lbl = a.getDisplayName(a);
			lbl = lbl.substring(0, 1).toUpperCase() + lbl.substring(1);
			langMenu.add(lbl).addActionListener(new AbstractAction() {
				private static final long serialVersionUID = 1L;
				@Override
				public void actionPerformed(ActionEvent e) {
					i18n.setLocale(a);
					properties.setLang(i18n.getLocale().getLanguage());
					updateI18n();
				}
			});
		}
		
		//about menu (2)
		JMenu aboutMenu = new JMenu(i18n.get(Labels.MENU_HELP));
		menuBar.add(aboutMenu);
		
		aboutMenu.add(i18n.get(Labels.MENU_HELP_ABOUT)).addActionListener(new AbstractAction() { //(2.1)
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Created by: Igor Zubenko(igor.a.zubenko@gmail.com)\nLicensed by: http://opensource.org/licenses/BSD-3-Clause", "About", JOptionPane.INFORMATION_MESSAGE); //TODO: i18n
			}
		});
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
			title += " - " + user.getFullname() == null || user.getFullname().isEmpty() ? user.getUsername() : user.getFullname();
		}
		setTitle(title);
	}
	
	private void updateI18n() {
		updateTitle();
		
		//file menu
		JMenu file = menuBar.getMenu(0);
		file.setText(i18n.get(Labels.MENU_FILE));
		file.getItem(0).setText(i18n.get(Labels.MENU_FILE_EXIT));
		
		//settings menu
		JMenu settings = menuBar.getMenu(1);
		settings.setText(i18n.get(Labels.MENU_SETTINGS));
		settings.getItem(0).setText(i18n.get(Labels.MENU_SETTINGS_LANG));
		
		//help menu
		JMenu help = menuBar.getMenu(2);
		help.setText(i18n.get(Labels.MENU_HELP));
		help.getItem(0).setText(i18n.get(Labels.MENU_HELP_ABOUT));
		
		view.updateI18n();
	}
	
	private void close() {
		if(properties.isChanged()) {
			properties.saveProperties();
		}
		dispose();
		System.exit(0);
	}

}
