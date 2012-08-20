package home.ingvar.passbook.ui.views;

import home.ingvar.passbook.dao.ResultException;
import home.ingvar.passbook.lang.Labels;
import home.ingvar.passbook.transfer.User;
import home.ingvar.passbook.ui.AbstractPanel;
import home.ingvar.passbook.ui.Form;
import home.ingvar.passbook.ui.GBH;
import home.ingvar.passbook.ui.res.IMG;
import home.ingvar.passbook.utils.LOG;

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

public class ProfilePanel extends AbstractPanel {
	
	private static final long serialVersionUID = 1L;
	private static final Dimension H_RIGID = new Dimension(5, 0);
	private static final Dimension V_RIGID = new Dimension(0, 5);

	private JLabel lblHeaderUsername;
	private JLabel lblFullname;
	private JTextField fldFullname;
	private TitledBorder brdChangePassword;
	private JLabel lblNewPassword;
	private JLabel lblOldPassword;
	private JLabel lblCnfPassword;
	private JLabel lblDeleteProfile;
	private JButton btnBack;
	private JButton btnChangeName;
	private JButton btnChangePassword;
	
	public ProfilePanel() {
		this.lblHeaderUsername = new JLabel();
		this.lblFullname = new JLabel();
		this.fldFullname = new JTextField(15);
		this.brdChangePassword = BorderFactory.createTitledBorder("");
		this.lblNewPassword = new JLabel();
		this.lblOldPassword = new JLabel();
		this.lblCnfPassword = new JLabel();
		this.lblDeleteProfile = new JLabel();
		this.btnBack = new JButton();
		this.btnChangeName = new JButton();
		this.btnChangePassword = new JButton();
		
		//TODO: refactor view
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		add(createHeaderPanel());
		add(createFullnamePanel());
		add(createPasswordPanel());
		add(createDeleteProfilePanel());
		add(Box.createVerticalGlue());
	}

	@Override
	protected void init() {
		updateHeaderName();
		fldFullname.setText(getUser().getFullname());
	}

	@Override
	protected void updateI18n() {
		lblFullname.setText(getText(Labels.LABELS_FULLNAME)+":");
		brdChangePassword.setTitle(getText(Labels.LABELS_CHANGE_PASSWORD));
		lblOldPassword.setText(getText(Labels.LABELS_PASSWORD_OLD)+":");
		lblNewPassword.setText(getText(Labels.LABELS_PASSWORD_NEW)+":");
		lblCnfPassword.setText(getText(Labels.LABELS_CONFIRM)+":");
		lblDeleteProfile.setText(getText(Labels.LABELS_DELETE_PROFILE));
		
		btnBack.setText(getText(Labels.BUTTONS_BACK));
		btnChangeName.setText(getText(Labels.BUTTONS_CHANGE));
		btnChangePassword.setText(getText(Labels.BUTTONS_CHANGE));
	}
	
	@Override
	protected JButton getDefaultButton() {
		return btnBack;
	}

	private void updateHeaderName() {
		User user = getUser();
		if(user.getFullname() == null || user.getFullname().isEmpty()) {
			lblHeaderUsername.setText(user.getUsername());
		} else {
			lblHeaderUsername.setText(user.getFullname());
		}
	}
	
	private void changePassword(String oldP, String newP, String cnfP) {
		User user = getUser();
		if(!user.getPassword().equals(oldP)) {
			JOptionPane.showMessageDialog(getRoot(), getText(Labels.MESSAGES_PASSWORD_INCORRECT), getText(Labels.TITLE_WARNING), JOptionPane.WARNING_MESSAGE);
		}
		else if(!newP.equals(cnfP)) {
			JOptionPane.showMessageDialog(getRoot(), getText(Labels.MESSAGES_PASSWORDS_NOT_EQUALS), getText(Labels.TITLE_WARNING), JOptionPane.WARNING_MESSAGE);
		}
		else {
			try {
				user.setPassword(oldP);
				getUserDAO().changePassword(user, newP);
				JOptionPane.showMessageDialog(getRoot(), getText(Labels.MESSAGES_PASSWORD_CHANGE), getText(Labels.TITLE_INFO), JOptionPane.INFORMATION_MESSAGE);
			} catch(ResultException e) {
				LOG.error(getText(Labels.TITLE_ERROR), e.getMessage(), e);
			}
		}
	}
	
	private void deleteProfile() {
		String password = JOptionPane.showInputDialog(getRoot(), getText(Labels.MESSAGES_DELETE_PROFILE), getText(Labels.TITLE_WARNING), JOptionPane.WARNING_MESSAGE);
		if(password == null) {
			return;
		}
		if(password.equals(getUser().getPassword())) {
			try {
				getUserDAO().delete(getUser());
				setUser(null);
				show(Form.LOGIN);
			} catch(ResultException e) {
				LOG.error(getText(Labels.TITLE_ERROR), e.getMessage(), e);
			}
		} else {
			JOptionPane.showMessageDialog(getRoot(), getText(Labels.MESSAGES_PASSWORD_INCORRECT), getText(Labels.TITLE_WARNING), JOptionPane.WARNING_MESSAGE);
		}
	}
	
	private JPanel createDeleteProfilePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		panel.add(Box.createRigidArea(V_RIGID));
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.LINE_AXIS));
		lblDeleteProfile.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		lblDeleteProfile.setForeground(Color.RED);
		JButton btnDelete = new JButton(new ImageIcon(IMG.DELETE_USER.getImage()));
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
	
	private JPanel createPasswordPanel() {
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
		GBH helper = new GBH();
		helper.setAnchor(GBH.LINE_END);
		helper.setWidth(GBH.RELATIVE);
		grid.add(lblOldPassword, helper.grid(1, 0));
		grid.add(lblNewPassword, helper.grid(2, 0));
		grid.add(lblCnfPassword, helper.grid(3, 0));
		helper.setAnchor(GBH.LINE_START);
		grid.add(oldPassword, helper.grid(1, 1));
		grid.add(newPassword, helper.grid(2, 1));
		grid.add(cnfPassword, helper.grid(3, 1));
		grid.add(btnChangePassword, helper.grid(4, 0).setWidth(GBH.REMAINDER).setAnchor(GBH.LINE_END));
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
	
	private JPanel createFullnamePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		panel.add(Box.createRigidArea(V_RIGID));
		fldFullname.setMaximumSize(new Dimension(40, 20));
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.LINE_AXIS));
		content.add(Box.createRigidArea(H_RIGID));
		content.add(lblFullname);
		content.add(Box.createRigidArea(H_RIGID));
		content.add(fldFullname);
		content.add(Box.createRigidArea(H_RIGID));
		content.add(btnChangeName);
		content.add(Box.createHorizontalGlue());
		panel.add(content);
		panel.add(Box.createRigidArea(V_RIGID));
		
		btnChangeName.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent event) {
				User user = getUser();
				user.setFullname(fldFullname.getText());
				try {
					getUserDAO().update(user);
					setUser(user); //need to update title
					updateHeaderName();
				} catch (ResultException e) {
					LOG.error(getText(Labels.TITLE_ERROR), e.getMessage(), e);
				}
			}
		});
		
		return panel;
	}
	
	private JPanel createHeaderPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setBorder(BorderFactory.createEtchedBorder());
		
		panel.add(Box.createRigidArea(V_RIGID));
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.LINE_AXIS));
		lblHeaderUsername.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		content.add(Box.createRigidArea(H_RIGID));
		content.add(lblHeaderUsername);
		content.add(Box.createHorizontalGlue());
		content.add(btnBack);
		content.add(Box.createRigidArea(H_RIGID));
		panel.add(content);
		panel.add(Box.createRigidArea(V_RIGID));
		
		btnBack.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				show(Form.MAIN);
			}
		});
		
		return panel;
	}

}
