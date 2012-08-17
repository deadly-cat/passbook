package home.ingvar.passbook.gui.views;

import home.ingvar.passbook.dao.ResultException;
import home.ingvar.passbook.transfer.User;
import home.ingvar.passbook.ui.GBHelper;
import home.ingvar.passbook.utils.I18n;
import home.ingvar.passbook.gui.MainFrame;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

/**
 * @author ingvar
 * @version 0.1
 *
 */
public class AuthPanel extends I18nJPanel {

	private static final Logger logger = Logger.getLogger(AuthPanel.class);
	private static final long serialVersionUID = 1L;
	private final MainFrame frame;
	private final I18n i18n;
	
	private JLabel lblUsername;
	private JLabel lblPassword;
	
	private JTextField username;
	private JTextField password;
	
	private JButton btnLogin;
	private JButton btnRegistration;
	
	public AuthPanel(final MainFrame frame) {
		this.frame = frame;
		this.i18n  = frame.getI18n();
		this.lblUsername = new JLabel();
		this.lblPassword = new JLabel();
		this.username = new JTextField(15);
		this.password = new JPasswordField(15);
		this.btnLogin = new JButton();
		this.btnRegistration = new JButton();
		init();
		rei18n();
	}
	
	public void login() {
		try {
			User user = frame.getUserDAO().get(username.getText().trim(), password.getText().trim());
			frame.setUser(user);
			frame.nextView(new ViewPanel(frame));
			
		} catch(ResultException e) {
			password.setText("");
			logger.error(e);
			JOptionPane.showMessageDialog(frame, e.getMessage(), i18n.get("title.error"), JOptionPane.ERROR_MESSAGE);
		} catch(SecurityException e) {
			password.setText("");
			JOptionPane.showMessageDialog(frame, e.getMessage(),  i18n.get("title.warning"), JOptionPane.WARNING_MESSAGE);
		}
	}
	
	private void init() {
		setLayout(new GridBagLayout());
		frame.getRootPane().setDefaultButton(btnLogin);
		
		GBHelper helper = new GBHelper();
		helper.setAnchor(GBHelper.LINE_END);
		add(lblUsername, helper.grid(0, 0));
		add(lblPassword, helper.grid(1, 0));
		helper.setAnchor(GBHelper.LINE_START);
		add(username, helper.grid(0, 1));
		add(password, helper.grid(1, 1));
		
		JPanel buttons = new JPanel();
		add(buttons, helper.grid(2, 0).setWidth(GBHelper.REMAINDER).setAnchor(GBHelper.LINE_END));
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.LINE_AXIS));
		buttons.add(btnRegistration);
		buttons.add(Box.createRigidArea(new Dimension(10, 0)));
		buttons.add(btnLogin);
		
		btnLogin.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
		
		btnRegistration.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.nextView(new RegPanel(frame));
			}
		});
	}

	@Override
	public void rei18n() {
		lblUsername.setText(i18n.get("labels.username")+":");
		lblPassword.setText(i18n.get("labels.password")+":");
		btnLogin.setText(i18n.get("buttons.login"));
		btnRegistration.setText(i18n.get("buttons.registration"));
	}

}