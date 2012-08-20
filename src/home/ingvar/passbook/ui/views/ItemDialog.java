package home.ingvar.passbook.ui.views;

import home.ingvar.passbook.ui.MainFrame;
import home.ingvar.passbook.lang.Labels;
import home.ingvar.passbook.transfer.Item;
import home.ingvar.passbook.transfer.User;
import home.ingvar.passbook.ui.GBH;
import home.ingvar.passbook.ui.res.IMG;
import home.ingvar.passbook.utils.I18n;
import home.ingvar.passbook.utils.PassGen;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author ingvar
 * @version 0.2
 *
 */
public class ItemDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final MainFrame frame;
	private final I18n i18n;
	private Item item;
	private boolean isOk;
	
	private JLabel lblService;
	private JLabel lblUsername;
	private JLabel lblPassword;
	private JLabel lblComment;
	
	private JTextField service;
	private JTextField username;
	private JTextField password;
	private JTextField comment;
	
	private JButton btnOk;
	private JButton btnCancel;
	private JButton btnGenerate;
	
	public ItemDialog(MainFrame frame) {
		super(frame, "", true);
		this.frame = frame;
		this.i18n  = I18n.getInstance();
		
		this.lblService  = new JLabel();
		this.lblUsername = new JLabel();
		this.lblPassword = new JLabel();
		this.lblComment  = new JLabel();
		
		this.service  = new JTextField(15);
		this.username = new JTextField(15);
		this.password = new JTextField(12);
		this.comment  = new JTextField(15);
		
		this.btnOk    = new JButton();
		this.btnCancel = new JButton();
		this.btnGenerate = new JButton();
		
		composition();
		updateI18n();
	}
	
	public void setItem(Item value) {
		item = value;
		service.setText(item.getService());
		username.setText(item.getUsername());
		password.setText(item.getPassword());
		comment.setText(item.getComment());
	}
	
	public Item getItem() {
		item.setService(service.getText());
		item.setUsername(username.getText());
		item.setPassword(password.getText());
		item.setComment(comment.getText());
		return item;
	}
	
	public Item showDialog(User owner) {
		return showDialog(new Item(owner));
	}
	
	public Item showDialog(Item editing) {
		isOk = false;
		setItem(editing);
		service.requestFocusInWindow();
		setVisible(true);
		if(isOk) {
			return getItem();
		}
		return null;
	}
	
	public void updateI18n() {
		setTitle(i18n.get(Labels.TITLE_ITEM_DIALOG));
		lblService.setText(i18n.get(Labels.LABELS_SERVICE)+":");
		lblUsername.setText(i18n.get(Labels.LABELS_USERNAME)+":");
		lblPassword.setText(i18n.get(Labels.LABELS_PASSWORD)+":");
		lblComment.setText(i18n.get(Labels.LABELS_COMMENT)+":");
		btnOk.setText(i18n.get(Labels.BUTTONS_OK));
		btnCancel.setText(i18n.get(Labels.BUTTONS_CANCEL));
		pack();
	}
	
	private boolean isCorrect() {
		StringBuilder message = new StringBuilder();
		
		if(service.getText().isEmpty()) {
			message.append(i18n.get(Labels.MESSAGES_SERVICE_EMPTY)).append("\n");
		}
		if(username.getText().isEmpty()) {
			message.append(i18n.get(Labels.MESSAGES_USERNAME_EMPTY)).append("\n");
		}
		
		if(message.length() > 0) {
			JOptionPane.showMessageDialog(null, message, i18n.get(Labels.TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
	
	private void composition() {
		setLocationRelativeTo(frame);
		setResizable(false);
		getRootPane().setDefaultButton(btnOk);
		
		//buttons panel
		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.LINE_AXIS));
		buttons.add(btnOk);
		buttons.add(Box.createRigidArea(new Dimension(10, 0)));
		buttons.add(btnCancel);
		buttons.add(Box.createRigidArea(new Dimension(6, 0)));
		
		JPanel view = new JPanel(new GridBagLayout());
		add(view, BorderLayout.WEST);
		GBH helper = new GBH();
		
		helper.setAnchor(GBH.LINE_END);
		view.add(lblService, helper.grid(0, 0));
		view.add(lblUsername, helper.grid(1, 0));
		view.add(lblPassword, helper.grid(2, 0));
		view.add(lblComment, helper.grid(3, 0));
		helper.setAnchor(GBH.LINE_START);
		helper.setWidth(GBH.REMAINDER);
		view.add(service, helper.grid(0, 1));
		view.add(username, helper.grid(1, 1));
		view.add(comment, helper.grid(3, 1));
		
		helper.setWidth(GBH.RELATIVE);
		view.add(password, helper.grid(2, 1));
		helper.setInsets(0, 2, 0, 0);
		view.add(btnGenerate, helper.grid(2, 3));
		
		helper.setWidth(GBH.REMAINDER);
		helper.setInsets(10, 0, 10, 0);
		view.add(buttons, helper.grid(4, 0).setAnchor(GBH.LINE_END));
		
		btnOk.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent event) {
				if(isCorrect()) {
					isOk = true;
					setVisible(false);
				}
			}
		});
		btnCancel.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnGenerate.setIcon(new ImageIcon(IMG.PASSGEN.getImage()));
		btnGenerate.setPreferredSize(new Dimension(25, 19));
		btnGenerate.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				password.setText(PassGen.generate(8));
			}
		});
	}

}
