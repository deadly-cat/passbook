package home.ingvar.passbook.ui.views;

import home.ingvar.passbook.lang.Labels;
import home.ingvar.passbook.transfer.Item;
import home.ingvar.passbook.transfer.User;
import home.ingvar.passbook.ui.GBH;
import home.ingvar.passbook.ui.MainFrame;
import home.ingvar.passbook.ui.res.IMG;
import home.ingvar.passbook.utils.I18n;
import home.ingvar.passbook.utils.PROPS;
import home.ingvar.passbook.utils.PassGen;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
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
	private int passLenght;
	
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
		i18n = I18n.getInstance();
		isOk = false;
		passLenght = PROPS.getInstance().getPasswordLenght();
		
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
		
		setLocationRelativeTo(frame);
		setResizable(false);
		getRootPane().setDefaultButton(btnOk);
		
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
		btnGenerate.setPreferredSize(new Dimension(25, 20));
		btnGenerate.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				password.setText(PassGen.generate(passLenght));
			}
		});
		
		setLayout(new GridBagLayout());
		add(lblService, GBH.get());
		add(service, GBH.get().fill(GBH.HORIZONTAL).width(GBH.REMAINDER));
		add(lblUsername, GBH.get());
		add(username, GBH.get().fill(GBH.HORIZONTAL).width(GBH.REMAINDER));
		add(lblPassword, GBH.get());
		JPanel pwd = new JPanel();
		add(pwd, GBH.get(0, 0, 0, 0).fill(GBH.HORIZONTAL).width(GBH.REMAINDER));
		pwd.add(password);
		pwd.add(btnGenerate);
		add(lblComment, GBH.get());
		add(comment, GBH.get().fill(GBH.HORIZONTAL).width(GBH.REMAINDER));
		JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		add(btns, GBH.get(0, 0, 0, 0).width(2).anchor(GBH.LINE_END));
		btns.add(btnCancel);
		btns.add(btnOk);
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
	
	public void setPasswordLenght(int lenght) {
		passLenght = lenght;
	}
	
	public Item showDialog(User owner) {
		return showDialog(new Item(owner));
	}
	
	public Item showDialog(Item editing) {
		pack();
		isOk = false;
		setItem(editing);
		service.requestFocusInWindow();
		Point l = frame.getLocation();
		int w = frame.getWidth();
		int h = frame.getHeight();
		setLocation(l.x + (w - getWidth()) / 2, l.y + (h - getHeight()) / 2);
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
	
}
