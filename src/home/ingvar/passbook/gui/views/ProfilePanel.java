package home.ingvar.passbook.gui.views;

import home.ingvar.passbook.dao.ResultException;
import home.ingvar.passbook.gui.GBHelper;
import home.ingvar.passbook.gui.MainFrame;
import home.ingvar.passbook.transfer.User;
import home.ingvar.passbook.utils.I18n;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

public class ProfilePanel extends I18nJPanel {

	private static final Logger logger = Logger.getLogger(ProfilePanel.class);
	private static final long serialVersionUID = 1L;
	private static final Dimension H_RIGID = new Dimension(5, 0);
	private static final Dimension V_RIGID = new Dimension(0, 5);
	private final MainFrame frame;
	private final I18n i18n;
	
	private JLabel lblUsername;
	private JLabel lblFullname;
	private TitledBorder brdChangePassword;
	private JLabel lblNewPassword;
	private JLabel lblOldPassword;
	private JLabel lblCnfPassword;
	private JLabel lblDeleteProfile;
	
	private JButton btnBack;
	private JButton btnChangeName;
	private JButton btnChangePassword;
	
	public ProfilePanel(MainFrame frame) {
		this.frame = frame;
		this.i18n  = frame.getI18n();
		
		this.lblUsername = new JLabel();
		this.lblFullname = new JLabel();
		this.brdChangePassword = BorderFactory.createTitledBorder("");
		this.lblNewPassword = new JLabel();
		this.lblOldPassword = new JLabel();
		this.lblCnfPassword = new JLabel();
		this.lblDeleteProfile = new JLabel();
		
		this.btnBack = new JButton();
		this.btnChangeName = new JButton();
		this.btnChangePassword = new JButton();
		
		init();
		rei18n();
	}
	
	@Override
	public void rei18n() {
		lblFullname.setText(i18n.get("labels.fullname")+":");
		brdChangePassword.setTitle(i18n.get("labels.change-password"));
		lblOldPassword.setText(i18n.get("labels.password.old")+":");
		lblNewPassword.setText(i18n.get("labels.password.new")+":");
		lblCnfPassword.setText(i18n.get("labels.confirm")+":");
		lblDeleteProfile.setText(i18n.get("labels.delete-profile"));
		
		btnBack.setText(i18n.get("buttons.back"));
		btnChangeName.setText(i18n.get("buttons.change"));
		btnChangePassword.setText(i18n.get("buttons.change"));
	}
	
	private void changePassword(String oldP, String newP, String cnfP) {
		User user = frame.getUser();
		if(!user.getPassword().equals(oldP)) {
			JOptionPane.showMessageDialog(frame, i18n.get("messages.password-incorrect"), i18n.get("title.warning"), JOptionPane.WARNING_MESSAGE);
		}
		else if(!newP.equals(cnfP)) {
			JOptionPane.showMessageDialog(frame, i18n.get("messages.passwords-not-equals"), i18n.get("title.warning"), JOptionPane.WARNING_MESSAGE);
		}
		else {
			try {
				user.setPassword(oldP);
				frame.getUserDAO().changePassword(user, newP);
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
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		add(getHeader());
		add(getFullname());
		add(getPassword());
		add(getDeleteProfile());
		add(Box.createVerticalGlue());
	}
	
	private JPanel getDeleteProfile() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		panel.add(Box.createRigidArea(V_RIGID));
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.LINE_AXIS));
		lblDeleteProfile.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		lblDeleteProfile.setForeground(Color.RED);
		JButton btnDelete = new JButton(new ImageIcon(ClassLoader.getSystemResource("home/ingvar/passbook/gui/resources/delete_user.png")));
		content.add(Box.createRigidArea(H_RIGID));
		content.add(lblDeleteProfile);
		content.add(Box.createRigidArea(H_RIGID));
		content.add(btnDelete);
		content.add(Box.createRigidArea(H_RIGID));
		content.add(Box.createHorizontalGlue());
		panel.add(content);
		panel.add(Box.createRigidArea(V_RIGID));
		
		btnDelete.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteProfile();
			}
		});
		
		return panel;
	}
	
	private JPanel getPassword() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		panel.add(Box.createRigidArea(V_RIGID));
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.LINE_AXIS));
		
		JPanel grid = new JPanel(new GridBagLayout());
		grid.setBorder(brdChangePassword);
		final JTextField oldPassword = new JPasswordField(15);
		final JTextField newPassword = new JPasswordField(15);
		final JTextField cnfPassword = new JPasswordField(15);
		GBHelper helper = new GBHelper();
		helper.setAnchor(GBHelper.LINE_END);
		helper.setWidth(GBHelper.RELATIVE);
		grid.add(lblOldPassword, helper.grid(1, 0));
		grid.add(lblNewPassword, helper.grid(2, 0));
		grid.add(lblCnfPassword, helper.grid(3, 0));
		helper.setAnchor(GBHelper.LINE_START);
		grid.add(oldPassword, helper.grid(1, 1));
		grid.add(newPassword, helper.grid(2, 1));
		grid.add(cnfPassword, helper.grid(3, 1));
		grid.add(btnChangePassword, helper.grid(4, 0).setWidth(GBHelper.REMAINDER).setAnchor(GBHelper.LINE_END));
		grid.setMaximumSize(new Dimension(400, 400));
		content.add(grid);
		content.add(Box.createHorizontalGlue());
		panel.add(content);
		panel.add(Box.createRigidArea(V_RIGID));
		
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
		
		return panel;
	}
	
	private JPanel getFullname() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		panel.add(Box.createRigidArea(V_RIGID));
		final User user = frame.getUser();
		final JTextField fullname = new JTextField(15);
		fullname.setMaximumSize(new Dimension(40, 20));
		fullname.setText(user.getFullname());
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.LINE_AXIS));
		content.add(Box.createRigidArea(H_RIGID));
		content.add(lblFullname);
		content.add(Box.createRigidArea(H_RIGID));
		content.add(fullname);
		content.add(Box.createRigidArea(H_RIGID));
		content.add(btnChangeName);
		content.add(Box.createHorizontalGlue());
		panel.add(content);
		panel.add(Box.createRigidArea(V_RIGID));
		
		btnChangeName.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				user.setFullname(fullname.getText());
				try {
					frame.getUserDAO().update(user);
					lblUsername.setText(user.getFullname());
				} catch (ResultException e1) {
					logger.error(e1);
					JOptionPane.showMessageDialog(frame, e1.getMessage(), i18n.get("title.error"), JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		return panel;
	}
	
	private JPanel getHeader() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setBorder(BorderFactory.createEtchedBorder());
		
		panel.add(Box.createRigidArea(V_RIGID));
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.LINE_AXIS));
		lblUsername.setText(frame.getUser().toString());
		lblUsername.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		content.add(Box.createRigidArea(H_RIGID));
		content.add(lblUsername);
		content.add(Box.createHorizontalGlue());
		content.add(btnBack);
		content.add(Box.createRigidArea(H_RIGID));
		panel.add(content);
		panel.add(Box.createRigidArea(V_RIGID));
		
		btnBack.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.nextView(new ViewPanel(frame));
			}
		});
		
		return panel;
	}

}
