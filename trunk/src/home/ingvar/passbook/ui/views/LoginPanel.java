package home.ingvar.passbook.ui.views;

import home.ingvar.passbook.dao.ResultException;
import home.ingvar.passbook.lang.Labels;
import home.ingvar.passbook.transfer.User;
import home.ingvar.passbook.ui.AbstractPanel;
import home.ingvar.passbook.ui.Form;
import home.ingvar.passbook.ui.GBHelper;
import home.ingvar.passbook.ui.MainFrame;
import home.ingvar.passbook.utils.LOG;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
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
	
	public LoginPanel(MainFrame frame) {
		super(frame);
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
		
		//composition
		setLayout(new GridBagLayout());
		
		GBHelper helper = new GBHelper();
		helper.setAnchor(GBHelper.LINE_END);
		add(lblUsername, helper.grid(0, 0));
		add(lblPassword, helper.grid(1, 0));
		helper.setAnchor(GBHelper.LINE_START);
		add(fldUsername, helper.grid(0, 1));
		add(fldPassword, helper.grid(1, 1));
		
		JPanel buttons = new JPanel();
		add(buttons, helper.grid(2, 0).setWidth(GBHelper.REMAINDER).setAnchor(GBHelper.LINE_END));
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.LINE_AXIS));
		buttons.add(btnRegister);
		buttons.add(Box.createRigidArea(new Dimension(10, 0)));
		buttons.add(btnLogin);
	}

	@Override
	protected void init() {
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
