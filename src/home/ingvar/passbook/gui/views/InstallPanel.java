package home.ingvar.passbook.gui.views;

import home.ingvar.passbook.dao.DaoFactory;
import home.ingvar.passbook.gui.MainFrame;
import home.ingvar.passbook.ui.GBHelper;
import home.ingvar.passbook.utils.I18n;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

public class InstallPanel extends I18nJPanel {
	
	private static final Object[][] databases = {
		{"H2 database", DaoFactory.H2}
	};
	private static final Logger logger = Logger.getLogger(InstallPanel.class);
	private static final long serialVersionUID = 1L;
	private final MainFrame frame;
	private final I18n i18n;
	
	private JLabel lblInfo;
	private JLabel lblDatabase;
	private JComboBox database;
	private JButton btnCreate;
	
	public InstallPanel(MainFrame frame) {
		this.frame = frame;
		this.i18n  = frame.getI18n();
		this.lblInfo = new JLabel();
		this.lblDatabase = new JLabel();
		this.database    = new JComboBox();
		this.btnCreate   = new JButton();
		init();
		rei18n();
	}

	@Override
	public void rei18n() {
		lblInfo.setText("  " + i18n.get("messages.not-create"));
		lblDatabase.setText(i18n.get("labels.database")+":");
		btnCreate.setText(i18n.get("buttons.create"));
	}
	
	private void createDB() {
		int db = (Integer) databases[database.getSelectedIndex()][1];
		try {
			DaoFactory factory = DaoFactory.newInstance(db);
			factory.install();
			frame.setDAO(factory);
			frame.setProperty("db", ""+db);
			JOptionPane.showMessageDialog(frame, i18n.get("messages.created"), i18n.get("title.info"), JOptionPane.INFORMATION_MESSAGE);
			frame.nextView(new RegPanel(frame));
		} catch(Exception e) {
			logger.error(e);
			JOptionPane.showMessageDialog(frame, e.getMessage(), i18n.get("title.error"), JOptionPane.ERROR_MESSAGE);
		}
	}

	private void init() {
		setLayout(new BorderLayout());
		
		add(lblInfo, BorderLayout.NORTH);
		lblInfo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		lblInfo.setForeground(Color.RED);
		
		JPanel settings = new JPanel(new GridBagLayout());
		//place west panel
		JPanel west = new JPanel(new BorderLayout());
		add(west, BorderLayout.WEST);
		//place north panel
		JPanel north = new JPanel(new FlowLayout(FlowLayout.LEFT));
		west.add(north, BorderLayout.NORTH);
		north.add(settings);
		
		GBHelper helper = new GBHelper();
		settings.add(lblDatabase, helper.grid(0, 0).setAnchor(GBHelper.LINE_END));
		settings.add(database, helper.grid(0, 1).setAnchor(GBHelper.LINE_START));
		
		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		add(buttons, BorderLayout.SOUTH);
		
		buttons.add(btnCreate);
		
		for(int i = 0;  i < databases.length; i++) {
			database.addItem(databases[i][0]);
		}
		
		btnCreate.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				createDB();
			}
		});
	}
	
}
