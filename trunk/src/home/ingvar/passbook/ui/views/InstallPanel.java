package home.ingvar.passbook.ui.views;

import home.ingvar.passbook.dao.DaoFactory;
import home.ingvar.passbook.lang.Labels;
import home.ingvar.passbook.ui.AbstractPanel;
import home.ingvar.passbook.ui.Form;
import home.ingvar.passbook.ui.GBH;
import home.ingvar.passbook.utils.LOG;

import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class InstallPanel extends AbstractPanel {

	private static final long serialVersionUID = 1L;
	private JLabel lblDatabase;
	private JComboBox database;
	private JTextArea txaLicense;
	private JButton btnCreate;

	public InstallPanel() {
		lblDatabase = new JLabel();
		database    = new JComboBox();
		txaLicense  = new JTextArea();
		btnCreate   = new JButton();
		
		txaLicense.setEditable(false);
		txaLicense.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
		txaLicense.setLineWrap(true);
		txaLicense.setWrapStyleWord(true);
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
		
		setLayout(new GridBagLayout());
		
		//first row
		add(lblDatabase, GBH.get());
		add(database, GBH.get());
		add(new JLabel(), GBH.get().width(GBH.REMAINDER).weightx(1.0));
		//second row
		add(new JScrollPane(txaLicense), GBH.get().fill(GBH.BOTH).width(GBH.REMAINDER).weighty(1.0));
		//third row
		add(new JLabel(), GBH.get().width(2).weightx(1.0));
		add(btnCreate, GBH.get().anchor(GBH.LINE_END));
	}

	@Override
	protected void preShow() {
		//nothing to do here
	}

	@Override
	protected void updateI18n() {
		lblDatabase.setText(getText(Labels.LABELS_DATABASE)+":");
		btnCreate.setText(getText(Labels.BUTTONS_CREATE));
		txaLicense.setText(getLicense());
		txaLicense.setCaretPosition(0);
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
	
}
