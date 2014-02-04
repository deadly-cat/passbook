package home.ingvar.passbook.ui.views;

import home.ingvar.passbook.dao.ResultException;
import home.ingvar.passbook.lang.Labels;
import home.ingvar.passbook.transfer.Item;
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
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
	
	private TitledBorder brdTransfer;
	private JCheckBox chbWithoutEncode;
	private JButton btnImport;
	private JButton btnExport;
	
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
		
		brdTransfer = BorderFactory.createTitledBorder("");;
		chbWithoutEncode = new JCheckBox();
		btnImport = new JButton();
		btnExport = new JButton();
		
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
		btnImport.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				importData();
			}
		});
		btnExport.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				exportData();
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
		
		JPanel cngTrans = new JPanel(new GridBagLayout());
		add(cngTrans, GBH.get(0, 0, 5, 0).fill(GBH.HORIZONTAL));
		cngTrans.setBorder(brdTransfer);
		cngTrans.add(chbWithoutEncode, GBH.get().width(GBH.REMAINDER).anchor(GBH.LINE_START));
		JPanel cngTransBtns = new JPanel();
		cngTrans.add(cngTransBtns, GBH.get().width(GBH.RELATIVE).anchor(GBH.LINE_START).weightx(1.0));
		cngTransBtns.add(btnImport);
		cngTransBtns.add(btnExport);
		
		JPanel delProfile = new JPanel(new GridBagLayout());
		add(delProfile, GBH.get(0, 0, 5, 0).anchor(GBH.LAST_LINE_END).weightx(1.0).width(GBH.REMAINDER));
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
		
		brdTransfer.setTitle(getText(Labels.LABELS_TRANSFER));
		chbWithoutEncode.setText(getText(Labels.LABELS_WITHOUT_ENCODE));
		btnImport.setText(getText(Labels.BUTTONS_IMPORT));
		btnExport.setText(getText(Labels.BUTTONS_EXPORT));
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
	
	private void importData() {
		JFileChooser chooser = new JFileChooser(".");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int result = chooser.showSaveDialog(this);
		if(JFileChooser.APPROVE_OPTION == result) {
			File transfer = chooser.getSelectedFile();
			StringBuilder data = new StringBuilder();
			
			try {
				BufferedInputStream in = new BufferedInputStream(new FileInputStream(transfer));
				byte[] buf = new byte[1024];
				int c;
				while((c = in.read(buf)) != -1) {
					data.append(new String(buf, 0, c));
				}
			} catch(IOException e) {
				LOG.error(getText(Labels.TITLE_ERROR), e.getMessage(), e);
			}
			
			if(!chbWithoutEncode.isSelected()) {
				//TODO: encoding
			}
			
			try {
				JSONObject main = new JSONObject(data.toString());
				JSONArray jis = main.getJSONArray("items");
				for(int i = 0; i < jis.length(); i++) {
					JSONObject ji = jis.getJSONObject(i);
					Item item = new Item();
					item.setOwner(getUser());
					item.setService(ji.getString("service"));
					item.setUsername(ji.getString("username"));
					item.setPassword(ji.getString("password"));
					item.setModifyDate(new Date(ji.getLong("modify_date")));
					item.setComment(ji.getString("comment"));
					
					Item upd = null;
					try {
						upd = getItemDAO().get(getUser(), item.getService(), item.getUsername());
					} catch(ResultException e) {}
					
					if(upd == null) {
						getItemDAO().add(item);
					} else {					
						if(item.getModifyDate() != null && item.getModifyDate().after(upd.getModifyDate())) {
							item.setId(upd.getId());
							getItemDAO().update(item);
						}
					}
				}
				//TODO: show successful message
			} catch(JSONException e) {
				LOG.error(getText(Labels.TITLE_ERROR), e.getMessage(), e);
			} catch(ResultException e) {
				LOG.error(getText(Labels.TITLE_ERROR), e.getMessage(), e);
			}
		}
	}
	
	private void exportData() {
		String password = JOptionPane.showInputDialog(this, getText(Labels.MESSAGES_EXPORT_CONFIRMATION), getText(Labels.TITLE_EXPORT_CONFIRMATION), JOptionPane.INFORMATION_MESSAGE);
		if(!getUser().getPassword().equals(password)) {
			JOptionPane.showMessageDialog(this, getText(Labels.MESSAGES_PASSWORD_INCORRECT), getText(Labels.TITLE_EXPORT_CONFIRMATION), JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		JFileChooser chooser = new JFileChooser(".");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int result = chooser.showOpenDialog(this);
		if(JFileChooser.APPROVE_OPTION == result) {
			File transfer = chooser.getSelectedFile();
			String data = "";
			
			try {
				List<Item> items = getItemDAO().list(getUser());
				JSONObject main = new JSONObject();
				JSONArray jis = new JSONArray();
				main.put("items", jis);
				for(Item item : items) {
					JSONObject ji = new JSONObject();
					jis.put(ji);
					ji.put("service", item.getService());
					ji.put("username", item.getUsername());
					ji.put("password", item.getPassword());
					ji.put("comment", item.getComment());
					ji.put("modify_date", item.getModifyDate().getTime());
				}
				data = main.toString();
				
			} catch(ResultException e) {
				LOG.error(getText(Labels.TITLE_ERROR), e.getMessage(), e);
			}
			
			if(!chbWithoutEncode.isSelected()) {
				//TODO: encoding
			}
			
			if(data != null && !data.isEmpty()) {
				BufferedOutputStream out = null;
				try {
					out = new BufferedOutputStream(new FileOutputStream(transfer));
					out.write(data.getBytes("UTF-8"));
					out.flush();
					JOptionPane.showMessageDialog(getRoot(), getText(Labels.MESSAGES_EXPORT_SUCCESSFUL), getText(Labels.TITLE_INFO), JOptionPane.INFORMATION_MESSAGE);
				} catch(IOException e) {
					LOG.error(getText(Labels.TITLE_ERROR), e.getMessage(), e);
				} finally {
					try{if(out != null) out.close();}catch(IOException e) {}
				}
				//TODO: show successful message
			} else {
				JOptionPane.showMessageDialog(getRoot(), getText(Labels.MESSAGES_NOTHING_TO_TRANSFER), getText(Labels.TITLE_WARNING), JOptionPane.WARNING_MESSAGE);
			}
		}
	}

}
