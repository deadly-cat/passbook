package home.ingvar.passbook.gui.views;

import home.ingvar.passbook.dao.ResultException;
import home.ingvar.passbook.gui.I18n;
import home.ingvar.passbook.transfer.User;

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
public class RegPanel extends I18nJPanel {
	
	private static final Logger logger = Logger.getLogger(RegPanel.class);
	private static final long serialVersionUID = 1L;
	private final MainFrame frame;
	private final I18n i18n;
	
	private JLabel lblUsername;
	private JLabel lblFullname;
	private JLabel lblPassword;
	private JLabel lblConfirm;
	
	private JTextField username;
	private JTextField fullname;
	private JTextField password;
	private JTextField confirm;
	
	private JButton btnRegister;
	private JButton btnCancel;
	
	public RegPanel(MainFrame frame) {
		this.frame = frame;
		this.i18n  = frame.getI18n();
		this.username = new JTextField(15);
		this.fullname = new JTextField(15);
		this.password = new JPasswordField(15);
		this.confirm  = new JPasswordField(15);
		
		this.lblUsername = new JLabel();
		this.lblFullname = new JLabel();
		this.lblPassword = new JLabel();
		this.lblConfirm  = new JLabel();
		
		this.btnRegister = new JButton();
		this.btnCancel   = new JButton();
		init();
		rei18n();
	}
	
	@Override
	public void rei18n() {
		lblUsername.setText(i18n.get("labels.username")+":");
		lblFullname.setText(i18n.get("labels.fullname")+":");
		lblPassword.setText(i18n.get("labels.password")+":");
		lblConfirm.setText(i18n.get("labels.confirm")+":");
		
		btnRegister.setText(i18n.get("buttons.register"));
		btnCancel.setText(i18n.get("buttons.cancel"));
	}
	
	private void register() {
		String p = password.getText().trim();
		String c = confirm.getText().trim();
		if(p.isEmpty()) {
			JOptionPane.showMessageDialog(frame, i18n.get("messages.password-empty"), i18n.get("labels.warning"), JOptionPane.WARNING_MESSAGE);
			confirm.setText("");
		}
		else if(!p.equals(c)) {
			JOptionPane.showMessageDialog(frame, i18n.get("messages.passwords-not-equals"), i18n.get("labels.warning"), JOptionPane.WARNING_MESSAGE);
			password.setText("");
			confirm.setText("");
		}
		else {
			User user = new User(username.getText().trim(), p, fullname.getText().trim());
			try {
				frame.getUserDAO().add(user);
				frame.setUser(user);
				frame.nextView(new ViewPanel(frame));
				
			} catch(ResultException e) {
				password.setText("");
				confirm.setText("");
				logger.error(e);
				JOptionPane.showMessageDialog(frame, e.getMessage(), i18n.get("title.error"), JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void init() {
		setLayout(new GridBagLayout());
		frame.getRootPane().setDefaultButton(btnRegister);
		
		GBHelper helper = new GBHelper();
		helper.setAnchor(GBHelper.LINE_END);
		add(lblUsername, helper.grid(0, 0));
		add(lblPassword, helper.grid(1, 0));
		add(lblConfirm, helper.grid(2, 0));
		add(lblFullname, helper.grid(3, 0));
		helper.setAnchor(GBHelper.LINE_START);
		add(username, helper.grid(0, 1));
		add(password, helper.grid(1, 1));
		add(confirm, helper.grid(2, 1));
		add(fullname, helper.grid(3, 1));
		
		JPanel buttons = new JPanel();
		add(buttons, helper.grid(4, 0).setWidth(GBHelper.REMAINDER).setAnchor(GBHelper.LINE_END));
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.LINE_AXIS));
		buttons.add(btnCancel);
		buttons.add(Box.createRigidArea(new Dimension(10, 0)));
		buttons.add(btnRegister);
		
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
				frame.nextView(new AuthPanel(frame));
			}
		});
	}

}
