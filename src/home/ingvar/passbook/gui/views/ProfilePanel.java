package home.ingvar.passbook.gui.views;

import home.ingvar.passbook.dao.ResultException;
import home.ingvar.passbook.transfer.User;
import home.ingvar.passbook.gui.I18n;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

public class ProfilePanel extends I18nJPanel {

	private static final Logger logger = Logger.getLogger(ProfilePanel.class);
	private static final long serialVersionUID = 1L;
	private final MainFrame frame;
	private final I18n i18n;
	
	private JLabel lblChangePassword;
	private JLabel lblNewPassword;
	private JLabel lblOldPassword;
	private JLabel lblCnfPassword;
	
	private JButton btnBack;
	private JButton btnChangePassword;
	private JButton btnDelProfile;
	
	public ProfilePanel(MainFrame frame) {
		this.frame = frame;
		this.i18n  = frame.getI18n();
		
		this.lblChangePassword = new JLabel();
		this.lblNewPassword = new JLabel();
		this.lblOldPassword = new JLabel();
		this.lblCnfPassword = new JLabel();
		
		this.btnBack = new JButton();
		this.btnChangePassword = new JButton();
		this.btnDelProfile = new JButton();
		
		init();
		rei18n();
	}
	
	@Override
	public void rei18n() {
		lblChangePassword.setText(i18n.get("labels.change-password"));
		lblOldPassword.setText(i18n.get("labels.password.old")+":");
		lblNewPassword.setText(i18n.get("labels.password.new")+":");
		lblCnfPassword.setText(i18n.get("labels.confirm")+":");
		
		btnBack.setText(i18n.get("buttons.back"));
		btnChangePassword.setText(i18n.get("buttons.change"));
		btnDelProfile.setText(i18n.get("buttons.profile.delete"));
	}
	
	private void changePassword(String o, String n, String c) {
		User user = frame.getUser();
		if(!user.getPassword().equals(o)) {
			JOptionPane.showMessageDialog(frame, i18n.get("messages.password-incorrect"), i18n.get("title.warning"), JOptionPane.WARNING_MESSAGE);
		}
		else if(!n.equals(c)) {
			JOptionPane.showMessageDialog(frame, i18n.get("messages.passwords-not-equals"), i18n.get("title.warning"), JOptionPane.WARNING_MESSAGE);
		}
		else {
			user.setPassword(n);
			try {
				frame.getUserDAO().update(user);
				JOptionPane.showMessageDialog(frame, i18n.get("messages.password-change"), i18n.get("title.info"), JOptionPane.INFORMATION_MESSAGE);
			} catch(ResultException e) {
				logger.error(e);
				JOptionPane.showMessageDialog(frame, e.getMessage(), i18n.get("title.error"), JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void deleteProfile() {
		String message = i18n.get("messages.delete-profile");
		String password = JOptionPane.showInputDialog(frame, message, i18n.get("title.warning"), JOptionPane.WARNING_MESSAGE);
		if(password == null) {
			return;
		}
		if(password.equals(frame.getUser().getPassword())) {
			try {
				frame.getUserDAO().delete(frame.getUser());
				frame.setUser(null);
				frame.nextView(new AuthPanel(frame));
			} catch(ResultException e) {
				logger.error(e);
				JOptionPane.showMessageDialog(frame, e.getMessage(), i18n.get("title.error"), JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(frame, i18n.get("messages.password-incorrect"), i18n.get("title.warning"), JOptionPane.WARNING_MESSAGE);
		}
	}
	
	private void init() {
		setLayout(new BorderLayout());
		header();
		
		lblChangePassword.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		
		final JTextField oldPassword = new JPasswordField(15);
		final JTextField newPassword = new JPasswordField(15);
		final JTextField cnfPassword = new JPasswordField(15);
		
		JPanel settings = new JPanel(new GridBagLayout());
		//place west panel
		JPanel west = new JPanel(new BorderLayout());
		add(west, BorderLayout.WEST);
		//place north panel
		JScrollPane north = new JScrollPane(settings);
		north.setBorder(null);
		west.add(north, BorderLayout.NORTH);
		
		GBHelper helper = new GBHelper();
		settings.add(lblChangePassword, helper.grid(0, 0).setWidth(GBHelper.REMAINDER).setAnchor(GBHelper.LINE_START));
		helper.setAnchor(GBHelper.LINE_END);
		helper.setWidth(GBHelper.RELATIVE);
		settings.add(lblOldPassword, helper.grid(1, 0));
		settings.add(lblNewPassword, helper.grid(2, 0));
		settings.add(lblCnfPassword, helper.grid(3, 0));
		helper.setAnchor(GBHelper.LINE_START);
		settings.add(oldPassword, helper.grid(1, 1));
		settings.add(newPassword, helper.grid(2, 1));
		settings.add(cnfPassword, helper.grid(3, 1));
		settings.add(btnChangePassword, helper.grid(4, 0).setWidth(GBHelper.REMAINDER).setAnchor(GBHelper.LINE_END));
		//other settings
		helper.setInsets(30, 5, 5, 5).setAnchor(GBHelper.LINE_END).setWidth(GBHelper.REMAINDER);
		settings.add(btnDelProfile, helper.grid(5, 0));
		
		btnChangePassword.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				changePassword(oldPassword.getText().trim(), newPassword.getText().trim(), cnfPassword.getText().trim());
				oldPassword.setText("");
				newPassword.setText("");
				cnfPassword.setText("");
			}
		});
		btnDelProfile.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteProfile();
			}
		});
		
		//TODO: delete after complete another things
		btnChangePassword.setEnabled(false);
		btnDelProfile.setEnabled(false);
	}
	
	private void header() {
		JPanel header = new JPanel(new BorderLayout());
		add(header, BorderLayout.NORTH);
		header.setBorder(BorderFactory.createEtchedBorder());
		
		JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		header.add(namePanel, BorderLayout.WEST);
		JLabel userLabel = new JLabel(frame.getUser().getUsername());
		userLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		namePanel.add(userLabel);
		
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		header.add(buttonsPanel, BorderLayout.EAST);
		buttonsPanel.add(btnBack);
		
		btnBack.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.nextView(new ViewPanel(frame));
			}
		});
	}

}
