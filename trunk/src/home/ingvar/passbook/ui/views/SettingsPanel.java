package home.ingvar.passbook.ui.views;

import home.ingvar.passbook.dao.DaoFactory;
import home.ingvar.passbook.lang.Labels;
import home.ingvar.passbook.ui.AbstractPanel;
import home.ingvar.passbook.ui.GBH;
import home.ingvar.passbook.ui.Theme;
import home.ingvar.passbook.utils.I18n;
import home.ingvar.passbook.utils.PROPS;

import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

//TODO:
public class SettingsPanel extends AbstractPanel {

	private static final long serialVersionUID = 1L;
	private PROPS properties;
	private JLabel lblLanguage;
	private JLabel lblDatabase;
	private JLabel lblTheme;
	private JLabel lblPTimeout;
	private JLabel lblPLenght;
	private JComboBox cmbLanguage;
	private JComboBox cmbDatabase;
	private JComboBox cmbTheme;
	private JTextField fldPTimeout;
	private JTextField fldPLenght;
	private JButton btnBack;
	private JButton btnAccept;
	
	public SettingsPanel() {
		properties  = PROPS.getInstance();
		lblLanguage = new JLabel();
		lblDatabase = new JLabel();
		lblTheme    = new JLabel();
		lblPTimeout = new JLabel();
		lblPLenght  = new JLabel();
		cmbLanguage = new JComboBox();
		cmbDatabase = new JComboBox();
		cmbTheme    = new JComboBox();
		fldPTimeout = new JFormattedTextField(NumberFormat.getIntegerInstance());
		fldPLenght  = new JFormattedTextField(NumberFormat.getIntegerInstance());
		btnBack     = new JButton();
		btnAccept   = new JButton();
		
		fldPTimeout.setColumns(5);
		fldPLenght.setColumns(5);
		for(Locale l : I18n.getAvailable()) {
			String lbl = l.getDisplayName(l);
			lbl = lbl.substring(0, 1).toUpperCase() + lbl.substring(1);
			cmbLanguage.addItem(lbl);
		}
		Object[][] db = DaoFactory.STORAGES;
		for(int i = 0; i < db.length; i++) {
			cmbDatabase.addItem(db[i][0].toString());
		}
		
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				back();
			}
		});
		btnAccept.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		setLayout(new GridBagLayout());
		
		add(lblLanguage, GBH.get());
		add(cmbLanguage, GBH.get().fill(GBH.HORIZONTAL).weightx(0.1));
		add(new JLabel(), GBH.get(0, 0, 0, 0).width(GBH.REMAINDER).weightx(0.9));
		add(lblDatabase, GBH.get());
		add(cmbDatabase, GBH.get().fill(GBH.HORIZONTAL).weightx(0.1));
		add(new JLabel(), GBH.get(0, 0, 0, 0).width(GBH.REMAINDER).weightx(0.9));
		add(lblTheme, GBH.get());
		add(cmbTheme, GBH.get().fill(GBH.HORIZONTAL).weightx(0.1));
		add(new JLabel(), GBH.get(0, 0, 0, 0).width(GBH.REMAINDER).weightx(0.9));
		add(lblPTimeout, GBH.get());
		add(fldPTimeout, GBH.get().width(GBH.REMAINDER));
		add(lblPLenght, GBH.get());
		add(fldPLenght, GBH.get().width(GBH.REMAINDER));
		add(new JLabel(), GBH.get().weighty(1.0).width(GBH.REMAINDER));
		JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btns.add(btnBack);
		btns.add(btnAccept);
		add(btns, GBH.get(0, 0, 0, 0).width(3).anchor(GBH.LINE_END));
	}
	
	@Override
	protected void preShow() {
		//cmbLanguage.setSelectedItem(new CBItem<String>(properties.getLang(), null)); //TODO
		//cmbDatabase.setSelectedItem(new CBItem<Integer>(properties.getDB(), null));
		//cmbTheme.setSelectedItem(new CBItem<String>(properties.getTheme(), null));
		fldPTimeout.setText(Integer.toString(properties.getPasswordTimeout()));
		fldPLenght.setText(Integer.toString(properties.getPasswordLenght()));
	}

	@Override
	protected void updateI18n() {
		lblLanguage.setText(getText(Labels.MENU_SETTINGS_LANG) + ":");
		lblDatabase.setText(getText(Labels.LABELS_DATABASE) + ":");
		lblTheme.setText(getText(Labels.MENU_SETTINGS_THEME) + ":");
		lblPTimeout.setText(getText(Labels.LABELS_PASSWORD_TIMEOUT) + ":");
		lblPLenght.setText(getText(Labels.LABELS_PASSWORD_LENGHT) + ":");
		btnBack.setText(getText(Labels.BUTTONS_BACK));
		btnAccept.setText(getText(Labels.BUTTONS_APPLY));
		
		cmbTheme.removeAllItems();
		for(Theme t : Theme.values()) {
			cmbTheme.addItem(getText(t.getI18nName()));
		}
	}

	@Override
	protected JButton getDefaultButton() {
		return btnAccept;
	}

}
