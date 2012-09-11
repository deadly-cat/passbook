package home.ingvar.passbook.ui.views;

import home.ingvar.passbook.dao.DaoFactory;
import home.ingvar.passbook.lang.Labels;
import home.ingvar.passbook.ui.AbstractPanel;
import home.ingvar.passbook.ui.Form;
import home.ingvar.passbook.ui.GBH;
import home.ingvar.passbook.ui.Theme;
import home.ingvar.passbook.utils.I18n;
import home.ingvar.passbook.utils.PROPS;

import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
	
	private List<ListItem<String>> languages;
	private List<ListItem<Integer>> databases;
	private List<ListItem<String>> themes;
	
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
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				back();
			}
		});
		btnAccept.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean isChanged = false;
				String lang  = ((ListItem<String>) cmbLanguage.getSelectedItem()).getValue();
				String theme = ((ListItem<String>) cmbTheme.getSelectedItem()).getValue();
				Integer db = ((ListItem<Integer>) cmbDatabase.getSelectedItem()).getValue();
				Integer pt = Integer.valueOf(fldPTimeout.getText());
				Integer pl = Integer.valueOf(fldPLenght.getText());
				
				if(!pt.equals(properties.getPasswordTimeout())) {
					properties.setPasswordTimeout(pt);
					((MainPanel) Form.MAIN.getPanel()).setPasswordTimeout(pt);
					isChanged = true;
				}
				if(!pl.equals(properties.getPasswordLenght())) {
					properties.setPasswordLenght(pl);
					getItemDialog().setPasswordLenght(pl);
					isChanged = true;
				}
				if(!lang.equals(properties.getLang())) {
					getRoot().setProgramLocale(new Locale(lang));
					isChanged = true;
				}
				if(!theme.equals(properties.getTheme())) {
					getRoot().setTheme(Theme.valueOf(theme));
					isChanged = true;
				}
				if(!db.equals(properties.getDB())) {
					properties.setDB(db);
					isChanged = true;
					JOptionPane.showMessageDialog(getRoot(), getText(Labels.MESSAGES_RESTART_TO_APPLY), getText(Labels.TITLE_INFO), JOptionPane.INFORMATION_MESSAGE);
				}
				
				if(isChanged) {
					JOptionPane.showMessageDialog(getRoot(), getText(Labels.MESSAGES_SETTINGS_CHANGED), getText(Labels.TITLE_INFO), JOptionPane.INFORMATION_MESSAGE);
				}
				back();
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
	protected AbstractPanel postConstruct() {
		super.postConstruct();
		initLists();
		return this;
	}
	
	@Override
	protected void preShow() {
		cmbLanguage.setSelectedItem(getItem(properties.getLang(), languages));
		cmbDatabase.setSelectedItem(getItem(properties.getDB(), databases));
		cmbTheme.setSelectedItem(getItem(properties.getTheme(), themes));
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
		
		for(Theme t : Theme.values()) {
			ListItem<String> item = getItem(t.toString(), themes);
			if(item != null) {
				item.setName(getText(t.getI18nName()));
			}
		}
	}

	@Override
	protected JButton getDefaultButton() {
		return btnAccept;
	}
	
	private void initLists() {
		languages = new LinkedList<ListItem<String>>();
		for(Locale l : I18n.getAvailable()) {
			String lbl = l.getDisplayName(l);
			lbl = lbl.substring(0, 1).toUpperCase() + lbl.substring(1);
			ListItem<String> item = new ListItem<String>(lbl, l.getLanguage());
			languages.add(item);
			cmbLanguage.addItem(item);
		}
		
		databases = new LinkedList<ListItem<Integer>>();
		Object[][] db = DaoFactory.STORAGES;
		for(int i = 0; i < db.length; i++) {
			ListItem<Integer> item = new ListItem<Integer>((String) db[i][0], (Integer) db[i][1]);
			databases.add(item);
			cmbDatabase.addItem(item);
		}
		
		themes = new LinkedList<ListItem<String>>();
		for(Theme t : Theme.values()) {
			ListItem<String> item = new ListItem<String>(getText(t.getI18nName()), t.toString());
			themes.add(item);
			cmbTheme.addItem(item);
		}
	}
	
	private <T> ListItem<T> getItem(T value, List<ListItem<T>> list) {
		for(ListItem<T> i : list) {
			if(value.equals(i.getValue())) {
				return i;
			}
		}
		return null;
	}
	
	private class ListItem<T> {
		
		private String name;
		private T value;
		
		public ListItem(String name, T value) {
			this.name = name;
			this.value = value;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public T getValue() {
			return value;
		}
		
		@Override
		public String toString() {
			return name;
		}
		
	}

}
