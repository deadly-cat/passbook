package home.ingvar.passbook.ui.views;

import home.ingvar.passbook.dao.DaoFactory;
import home.ingvar.passbook.lang.Labels;
import home.ingvar.passbook.ui.AbstractPanel;
import home.ingvar.passbook.ui.Form;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class InstallPanel extends AbstractPanel {

	private static final long serialVersionUID = 1L;
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
		int db = (Integer) DaoFactory.STORAGES[database.getSelectedIndex()][1];
		try {
			DaoFactory factory = DaoFactory.newInstance(db);
			factory.install();
			getRoot().setStorage(factory, db);
			JOptionPane.showMessageDialog(getRoot(), getText(Labels.MESSAGES_CREATED), getText(Labels.TITLE_INFO), JOptionPane.INFORMATION_MESSAGE);
			show(Form.REGISTER);
		} catch(Exception e) {
			LOG.error(getText(Labels.TITLE_ERROR), e.getMessage(), e);
		}
	}

	@Override
	protected JButton getDefaultButton() {
		return btnCreate;
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
		
		for(int i = 0; i < DaoFactory.STORAGES.length; i++) {
			database.addItem(DaoFactory.STORAGES[i][0]);
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
