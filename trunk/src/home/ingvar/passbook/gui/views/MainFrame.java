package home.ingvar.passbook.gui.views;

import home.ingvar.passbook.dao.DaoFactory;
import home.ingvar.passbook.dao.ItemDAO;
import home.ingvar.passbook.dao.UserDAO;
import home.ingvar.passbook.transfer.User;
import home.ingvar.passbook.gui.I18n;
import home.ingvar.passbook.installer.InstallPanel;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

/**
 * @author ingvar
 * @version 0.2
 *
 */
public class MainFrame extends JFrame {
	
	private static final Logger logger = Logger.getLogger(InstallPanel.class);
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_WIDTH = 640;
	private static final int DEFAULT_HEIGTH = 480;
	private final Properties properties;
	private final I18n i18n;
	private UserDAO userDAO;
	private ItemDAO itemDAO;
	private User user;
	private I18nJPanel view;
	private JMenuBar menuBar;
	
	public MainFrame() {
		this.properties = new Properties();
		this.menuBar = new JMenuBar();
		this.i18n = new I18n();
		loadProperties();
		createMenu();
		init();
		rei18n();
	}
	
	public void nextView(I18nJPanel next) {
		remove(view);
		view = next;
		add(view);
		view.revalidate();
	}
	
	public UserDAO getUserDAO() {
		return userDAO;
	}
	
	public ItemDAO getItemDAO() {
		return itemDAO;
	}
	
	public void setDAO(DaoFactory factory) {
		userDAO = factory.getUserDAO();
		itemDAO = factory.getItemDAO();
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User value) {
		user = value;
	}
	
	public I18n getI18n() {
		return i18n;
	}
	
	public void rei18n() {
		setTitle(i18n.get("title.main"));
		
		//file menu
		JMenu file = menuBar.getMenu(0);
		file.setText(i18n.get("menu.file"));
		file.getItem(0).setText(i18n.get("menu.file.exit"));
		
		//settings menu
		JMenu settings = menuBar.getMenu(1);
		settings.setText(i18n.get("menu.settings"));
		settings.getItem(0).setText(i18n.get("menu.settings.lang"));
		
		//help menu
		JMenu help = menuBar.getMenu(2);
		help.setText(i18n.get("menu.help"));
		help.getItem(0).setText(i18n.get("menu.help.about"));
		
		view.rei18n();
	}
	
	public void setProperty(String name, String value) {
		properties.setProperty(name, value);
	}
	
	public String getProperty(String name) {
		return properties.getProperty(name);
	}
	
	private void init() {
		Toolkit tools = Toolkit.getDefaultToolkit();
		Dimension screen = tools.getScreenSize();
		Image favicon = tools.getImage(ClassLoader.getSystemResource("home/ingvar/passbook/gui/resources/favicon.png"));
		
		setIconImage(favicon);
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGTH);
		setLocation((screen.width - DEFAULT_WIDTH) / 2, (screen.height - DEFAULT_HEIGTH) / 2);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setJMenuBar(menuBar);
		
		String lang = properties.getProperty("lang", "en");
		if(!lang.isEmpty()) {
			i18n.setLocale(new Locale(lang));
		}
		
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
				logger.error(e.getMessage(), e);
				view = new InstallPanel(this);
			}
		} else {
			view = new InstallPanel(this);
		}
		add(view);
		
		addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {}
			@Override
			public void windowIconified(WindowEvent e) {}
			@Override
			public void windowDeiconified(WindowEvent e) {}
			@Override
			public void windowDeactivated(WindowEvent e) {}
			@Override
			public void windowClosing(WindowEvent e) {
				saveProperties();
				dispose();
				System.exit(0);
			}
			@Override
			public void windowClosed(WindowEvent e) {}
			@Override
			public void windowActivated(WindowEvent e) {}
		});
	}
	
	private void createMenu() {
		//file menu
		JMenu fileMenu = new JMenu();
		menuBar.add(fileMenu);
		
		fileMenu.add("exit").addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		//settings menu
		JMenu settingsMenu = new JMenu();
		menuBar.add(settingsMenu);
		
		JMenu langMenu = new JMenu();
		settingsMenu.add(langMenu);
		for(final Locale a : I18n.getAvailable()) {
			langMenu.add(a.getDisplayName(a)).addActionListener(new AbstractAction() {
				private static final long serialVersionUID = 1L;
				@Override
				public void actionPerformed(ActionEvent e) {
					i18n.setLocale(a);
					properties.setProperty("lang", i18n.getLocale().getLanguage());
					rei18n();
				}
			});
		}
		
		//about menu
		JMenu aboutMenu = new JMenu();
		menuBar.add(aboutMenu);
		
		aboutMenu.add("about").addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Created by: Ingvar", "About", JOptionPane.INFORMATION_MESSAGE);
			}
		});
	}
	
	private void loadProperties() {
		try {
			FileInputStream in = new FileInputStream("config");
			properties.load(in);
			in.close();
		} catch(IOException e) {}
	}
	
	private void saveProperties() {
		try {
			FileOutputStream out = new FileOutputStream("config");
			properties.store(out, "");
			out.close();
		} catch(IOException e) {}
	}
	
}
