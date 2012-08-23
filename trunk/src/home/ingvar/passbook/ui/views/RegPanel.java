package home.ingvar.passbook.ui.views;

import home.ingvar.passbook.dao.ResultException;
import home.ingvar.passbook.lang.Labels;
import home.ingvar.passbook.transfer.User;
import home.ingvar.passbook.ui.AbstractPanel;
import home.ingvar.passbook.ui.Form;
import home.ingvar.passbook.ui.GBH;
import home.ingvar.passbook.utils.LOG;

import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class RegPanel extends AbstractPanel {

	private static final long serialVersionUID = 1L;

	private JLabel lblUsername;
	private JLabel lblFullname;
	private JLabel lblPassword;
	private JLabel lblConfirm;
	
	private JTextField fldUsername;
	private JTextField fldFullname;
	private JTextField fldPassword;
	private JTextField fldConfirm;
	
	private JButton btnRegister;
	private JButton btnCancel;
	
	public RegPanel() {
		this.fldUsername = new JTextField(15);
		this.fldFullname = new JTextField(15);
		this.fldPassword = new JPasswordField(15);
		this.fldConfirm  = new JPasswordField(15);
		
		this.lblUsername = new JLabel();
		this.lblFullname = new JLabel();
		this.lblPassword = new JLabel();
		this.lblConfirm  = new JLabel();
		
		this.btnRegister = new JButton();
		this.btnCancel   = new JButton();
		
		btnRegister.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				register();
			}
		});
		btnCancel.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				show(Form.LOGIN);
			}
		});
		
		setLayout(new GridBagLayout());
		
		//zero row
		add(ph(), GBH.get());
		add(ph(), GBH.get().anchor(GBH.LINE_END));
		add(ph(), GBH.get());
		add(ph(), GBH.get().width(GBH.REMAINDER));
		//first
		add(lblUsername, GBH.get());
		add(fldUsername, GBH.get().anchor(GBH.LINE_END).width(GBH.REMAINDER));
		//second
		add(lblPassword, GBH.get());
		add(fldPassword, GBH.get().anchor(GBH.LINE_END).width(GBH.REMAINDER));
		//third
		add(lblConfirm, GBH.get());
		add(fldConfirm, GBH.get().anchor(GBH.LINE_END).width(GBH.REMAINDER));
		//fourth
		add(lblFullname, GBH.get());
		add(fldFullname, GBH.get().anchor(GBH.LINE_END).width(GBH.REMAINDER));
		//fifth
		//I tried use only GridBagLayout, but for this composition it's impossible :(
		JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		add(btns, GBH.get(0, 0, 0, 0).anchor(GBH.LINE_END).width(GBH.REMAINDER));
		btns.add(btnCancel);
		btns.add(btnRegister);
	}

	@Override
	protected void preShow() {
		fldUsername.setText("");
		fldFullname.setText("");
		fldPassword.setText("");
		fldConfirm.setText("");
	}

	@Override
	protected void updateI18n() {
		lblUsername.setText(getText(Labels.LABELS_USERNAME)+":");
		lblFullname.setText(getText(Labels.LABELS_FULLNAME)+":");
		lblPassword.setText(getText(Labels.LABELS_PASSWORD)+":");
		lblConfirm.setText(getText(Labels.LABELS_CONFIRM)+":");
		
		btnRegister.setText(getText(Labels.BUTTONS_REGISTER));
		btnCancel.setText(getText(Labels.BUTTONS_CANCEL));
	}
	
	@Override
	protected JButton getDefaultButton() {
		return btnRegister;
	}

	private void register() {
		String p = fldPassword.getText().trim();
		String c = fldConfirm.getText().trim();
		if(p.isEmpty()) {
			JOptionPane.showMessageDialog(getRoot(), getText(Labels.MESSAGES_PASSWORD_EMPTY), getText(Labels.TITLE_WARNING), JOptionPane.WARNING_MESSAGE);
			fldConfirm.setText("");
		}
		else if(!p.equals(c)) {
			JOptionPane.showMessageDialog(getRoot(), getText(Labels.MESSAGES_PASSWORDS_NOT_EQUALS), getText(Labels.TITLE_WARNING), JOptionPane.WARNING_MESSAGE);
			fldPassword.setText("");
			fldConfirm.setText("");
		}
		else {
			User user = new User(fldUsername.getText().trim(), p, fldFullname.getText().trim());
			try {
				if(!getDaoFactory().isOpen()) {
					getDaoFactory().open();
				}
				getUserDAO().add(user);
				setUser(user);
				show(Form.MAIN);
				
			} catch(ResultException e) {
				fldPassword.setText("");
				fldConfirm.setText("");
				LOG.error(getText(Labels.TITLE_ERROR), e.getMessage(), e);
			}
		}
	}
	
	private JLabel ph() { //placeholder
		JLabel lbl = new JLabel("INVIS");
		//lbl.setForeground(getBackground());
		lbl.setVisible(false);
		return lbl;
	}

}
