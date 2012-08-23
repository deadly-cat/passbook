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
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginPanel extends AbstractPanel {

	private static final long serialVersionUID = 1L;
	
	private JLabel lblUsername;
	private JLabel lblPassword;
	private JTextField fldUsername;
	private JTextField fldPassword;
	private JButton btnLogin;
	private JButton btnRegister;
	
	public LoginPanel() {
		lblUsername = new JLabel();
		lblPassword = new JLabel();
		fldUsername = new JTextField(15);
		fldPassword = new JPasswordField(15);
		btnLogin    = new JButton();
		btnRegister = new JButton();
		
		btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					if(!getDaoFactory().isOpen()) {
						getDaoFactory().open();
					}
					User user = getUserDAO().get(fldUsername.getText().trim(), fldPassword.getText());
					setUser(user);
					show(Form.MAIN);
					
				} catch(ResultException e) {
					fldPassword.setText("");
					LOG.error(getText(Labels.TITLE_ERROR), e.getMessage(), e);
				} catch(SecurityException e) {
					fldPassword.setText("");
					LOG.warn(getText(Labels.TITLE_WARNING), e.getMessage(), e);
				}
			}
		});
		btnRegister.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				show(Form.REGISTER);
			}
		});
		
		setLayout(new GridBagLayout());
		
		
		//first row
		add(lblUsername, GBH.get(5, 5, 5, 5));
		add(fldUsername, GBH.get(5, 5, 5, 5).anchor(GBH.LINE_END).width(GBH.REMAINDER));
		//second row
		add(lblPassword, GBH.get(5, 5, 5, 5));
		add(fldPassword, GBH.get(5, 5, 5, 5).anchor(GBH.LINE_END).width(GBH.REMAINDER));
		//third row
		//I tried use only GridBagLayout, but for this composition it's impossible :(
		JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		add(btns, GBH.get(0, 0, 0, 0).anchor(GBH.LINE_END).width(GBH.REMAINDER));
		btns.add(btnRegister);
		btns.add(btnLogin);
	}

	@Override
	protected void preShow() {
		fldUsername.setText("");
		fldPassword.setText("");
	}

	@Override
	protected void updateI18n() {
		lblUsername.setText(getText(Labels.LABELS_USERNAME) + ":");
		lblPassword.setText(getText(Labels.LABELS_PASSWORD) + ":");
		btnLogin.setText(getText(Labels.BUTTONS_LOGIN));
		btnRegister.setText(getText(Labels.BUTTONS_REGISTRATION));
	}

	@Override
	protected JButton getDefaultButton() {
		return btnLogin;
	}
	
}
