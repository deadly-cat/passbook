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

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
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
	
	private JTextField username;
	private JTextField fullname;
	private JTextField password;
	private JTextField confirm;
	
	private JButton btnRegister;
	private JButton btnCancel;
	
	public RegPanel(MainFrame frame) {
		super(frame);
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
		
		setLayout(new GridBagLayout());
		
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
				show(Form.LOGIN);
			}
		});
	}

	@Override
	protected void init() {
		username.setText("");
		fullname.setText("");
		password.setText("");
		confirm.setText("");
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
		String p = password.getText().trim();
		String c = confirm.getText().trim();
		if(p.isEmpty()) {
			JOptionPane.showMessageDialog(getRoot(), getText(Labels.MESSAGES_PASSWORD_EMPTY), getText(Labels.TITLE_WARNING), JOptionPane.WARNING_MESSAGE);
			confirm.setText("");
		}
		else if(!p.equals(c)) {
			JOptionPane.showMessageDialog(getRoot(), getText(Labels.MESSAGES_PASSWORDS_NOT_EQUALS), getText(Labels.TITLE_WARNING), JOptionPane.WARNING_MESSAGE);
			password.setText("");
			confirm.setText("");
		}
		else {
			User user = new User(username.getText().trim(), p, fullname.getText().trim());
			try {
				if(!getDaoFactory().isOpen()) {
					getDaoFactory().open();
				}
				getUserDAO().add(user);
				setUser(user);
				show(Form.MAIN);
				
			} catch(ResultException e) {
				password.setText("");
				confirm.setText("");
				LOG.error(getText(Labels.TITLE_ERROR), e.getMessage(), e);
			}
		}
	}

}
