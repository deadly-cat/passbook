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
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
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
	
	private JLabel lblHeaderUsername;
	private JButton btnBack;
	
	private TitledBorder brdChangeFullname;
	private JLabel lblFullname;
	private JTextField fldFullname;
	private JButton btnChangeFullname;
	
	private TitledBorder brdChangePassword;
	private JLabel lblNewPassword;
	private JLabel lblOldPassword;
	private JLabel lblCnfPassword;
	private JTextField fldNewPassword;
	private JTextField fldOldPassword;
	private JTextField fldCnfPassword;
	private JButton btnChangePassword;
	
	private JLabel lblDeleteProfile;
	private JButton btnDeleteProfile;
	
	public ProfilePanel() {
		lblHeaderUsername = new JLabel();
		btnBack = new JButton();
		
		brdChangeFullname = BorderFactory.createTitledBorder("");
		lblFullname = new JLabel();
		fldFullname = new JTextField(15);
		btnChangeFullname = new JButton();
		
		brdChangePassword = BorderFactory.createTitledBorder("");;
		lblNewPassword = new JLabel();
		lblOldPassword = new JLabel();
		lblCnfPassword = new JLabel();
		fldNewPassword = new JPasswordField(15);
		fldOldPassword = new JPasswordField(15);
		fldCnfPassword = new JPasswordField(15);
		btnChangePassword = new JButton();
		
		lblDeleteProfile = new JLabel();
		btnDeleteProfile = new JButton(new ImageIcon(IMG.DELETE_USER.getImage()));
		
		lblHeaderUsername.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		lblDeleteProfile.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		lblDeleteProfile.setForeground(Color.RED);
		
		btnBack.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				back();
			}
		});
		btnChangeFullname.addActionListener(new AbstractAction() {
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
		btnChangePassword.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO: maybe need remove trim() method, but need add info about whitespace
				changePassword(fldOldPassword.getText().trim(), fldNewPassword.getText().trim(), fldCnfPassword.getText().trim());
				fldOldPassword.setText("");
				fldNewPassword.setText("");
				fldCnfPassword.setText("");
			}
		});
		btnDeleteProfile.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteProfile();
			}
		});
		
		setLayout(new GridBagLayout());
		
		JPanel header = new JPanel(new GridBagLayout());
		add(header, GBH.get(0, 0, 5, 0).fill(GBH.HORIZONTAL).weightx(1.0).width(GBH.REMAINDER));
		header.setBorder(BorderFactory.createTitledBorder(""));
		header.add(lblHeaderUsername, GBH.get().fill(GBH.HORIZONTAL).weightx(1.0));
		header.add(btnBack, GBH.get().width(GBH.REMAINDER));
		
		JPanel cngName = new JPanel(new GridBagLayout());
		add(cngName, GBH.get(0, 0, 5, 0).fill(GBH.HORIZONTAL));
		add(new JLabel(), GBH.get(0, 0, 0, 0).weightx(1.0).width(GBH.REMAINDER));
		cngName.setBorder(brdChangeFullname);
		cngName.add(lblFullname, GBH.get());
		cngName.add(new JLabel(), GBH.get(0, 0, 0, 0).weightx(1.0));
		cngName.add(fldFullname, GBH.get().anchor(GBH.LINE_END).width(GBH.REMAINDER));
		cngName.add(btnChangeFullname, GBH.get().width(3).anchor(GBH.LINE_END));
		
		JPanel cngPass = new JPanel(new GridBagLayout());
		add(cngPass, GBH.get(0, 0, 5, 0).fill(GBH.HORIZONTAL));
		add(new JLabel(), GBH.get(0, 0, 0, 0).weightx(1.0).width(GBH.REMAINDER));
		cngPass.setBorder(brdChangePassword);
		cngPass.add(lblOldPassword, GBH.get());
		cngPass.add(fldOldPassword, GBH.get().anchor(GBH.LINE_END).width(GBH.REMAINDER));
		cngPass.add(lblNewPassword, GBH.get());
		cngPass.add(fldNewPassword, GBH.get().anchor(GBH.LINE_END).width(GBH.REMAINDER));
		cngPass.add(lblCnfPassword, GBH.get());
		cngPass.add(fldCnfPassword, GBH.get().anchor(GBH.LINE_END).width(GBH.REMAINDER));
		cngPass.add(btnChangePassword, GBH.get().width(2).anchor(GBH.LINE_END));
		
		//empty space
		add(new JLabel(), GBH.get(0, 0, 0, 0).weightx(1.0).weighty(1.0).width(GBH.REMAINDER));
		
		JPanel delProfile = new JPanel(new GridBagLayout());
		add(delProfile, GBH.get(0, 0, 5, 0).anchor(GBH.LINE_END).width(3));
		delProfile.setBorder(BorderFactory.createTitledBorder(""));
		delProfile.add(lblDeleteProfile, GBH.get().weightx(1.0));
		delProfile.add(btnDeleteProfile, GBH.get().anchor(GBH.LINE_END));
	}

	@Override
	protected void preShow() {
		updateHeaderName();
		fldFullname.setText(getUser().getFullname());
	}
	
	@Override
	protected void updateI18n() {
		brdChangeFullname.setTitle(getText(Labels.LABELS_CHANGE_FULLNAME));
		lblFullname.setText(getText(Labels.LABELS_FULLNAME)+":");
		brdChangePassword.setTitle(getText(Labels.LABELS_CHANGE_PASSWORD));
		lblOldPassword.setText(getText(Labels.LABELS_PASSWORD_OLD)+":");
		lblNewPassword.setText(getText(Labels.LABELS_PASSWORD_NEW)+":");
		lblCnfPassword.setText(getText(Labels.LABELS_CONFIRM)+":");
		lblDeleteProfile.setText(getText(Labels.LABELS_DELETE_PROFILE));
		
		btnBack.setText(getText(Labels.BUTTONS_BACK));
		btnChangeFullname.setText(getText(Labels.BUTTONS_CHANGE));
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

}
