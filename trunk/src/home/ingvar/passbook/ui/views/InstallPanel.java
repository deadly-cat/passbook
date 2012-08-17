package home.ingvar.passbook.ui.views;

import home.ingvar.passbook.dao.DaoFactory;
import home.ingvar.passbook.lang.Labels;
import home.ingvar.passbook.ui.AbstractPanel;
import home.ingvar.passbook.ui.GBHelper;
import home.ingvar.passbook.ui.MainFrame;
import home.ingvar.passbook.utils.LOG;

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
import javax.swing.JPanel;

public class InstallPanel extends AbstractPanel {

	private static final long serialVersionUID = 1L;
	private static final Object[][] databases = {
		{"H2 database", DaoFactory.H2},
		//{"SQLite database", DaoFactory.SQLITE}
	};
	
	private JLabel lblInfo;
	private JLabel lblDatabase;
	private JComboBox database;
	private JButton btnCreate;

	public InstallPanel(MainFrame frame) {
		super(frame);
		this.lblInfo = new JLabel();
		this.lblDatabase = new JLabel();
		this.database    = new JComboBox();
		this.btnCreate   = new JButton();
		//TODO: add license
		composition();
	}

	@Override
	protected void init() {
		//nothing to do here
	}

	@Override
	protected void updateI18n() {
		lblInfo.setText("  " + getText(Labels.MESSAGES_NOT_CREATE));
		lblDatabase.setText(getText(Labels.LABELS_DATABASE)+":");
		btnCreate.setText(getText(Labels.BUTTONS_CREATE));
	}
	
	private void createDB() {
		int db = (Integer) databases[database.getSelectedIndex()][1];
		try {
			DaoFactory factory = DaoFactory.newInstance(db);
			factory.install();
			/*frame.setDAO(factory); TODO:
			frame.setProperty("db", ""+db);
			JOptionPane.showMessageDialog(frame, i18n.get("messages.created"), i18n.get("title.info"), JOptionPane.INFORMATION_MESSAGE);
			frame.nextView(new RegPanel(frame));*/
		} catch(Exception e) {
			LOG.error(getText(Labels.TITLE_ERROR), e.getMessage(), e);
		}
	}

	private void composition() {
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
