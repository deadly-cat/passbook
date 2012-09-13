package home.ingvar.passbook.ui.dialogs;

import home.ingvar.passbook.lang.Exceptions;
import home.ingvar.passbook.lang.Labels;
import home.ingvar.passbook.transfer.Item;
import home.ingvar.passbook.transfer.User;
import home.ingvar.passbook.ui.AbstractDialog;
import home.ingvar.passbook.ui.GBH;
import home.ingvar.passbook.ui.MainFrame;
import home.ingvar.passbook.ui.res.IMG;
import home.ingvar.passbook.utils.LOG;
import home.ingvar.passbook.utils.PROPS;
import home.ingvar.passbook.utils.PassGen;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author ingvar
 * @version 0.2
 *
 */
public class ItemDialog extends AbstractDialog<Item> {
	
	private static final long serialVersionUID = 1L;
	private Item item;
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
		super(frame);
		passLenght = PROPS.getInstance().getPasswordLenght();
		
		this.lblService  = new JLabel();
		this.lblUsername = new JLabel();
		this.lblPassword = new JLabel();
		this.lblComment  = new JLabel();
		
		this.service  = new JTextField(15);
		this.username = new JTextField(15);
		this.password = new JTextField(12);
		this.comment  = new JTextField(15);
		
		this.btnOk = new JButton();
		this.btnCancel = new JButton();
		this.btnGenerate = new JButton();
		
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if(isCorrect()) {
					setVisible(false);
				}
			}
		});
		btnCancel.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				item = null;
				setVisible(false);
			}
		});
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				item = null;
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

	@Override
	public void updateI18n() {
		setTitle(getText(Labels.TITLE_ITEM_DIALOG));
		lblService.setText(getText(Labels.LABELS_SERVICE)+":");
		lblUsername.setText(getText(Labels.LABELS_USERNAME)+":");
		lblPassword.setText(getText(Labels.LABELS_PASSWORD)+":");
		lblComment.setText(getText(Labels.LABELS_COMMENT)+":");
		btnOk.setText(getText(Labels.BUTTONS_OK));
		btnCancel.setText(getText(Labels.BUTTONS_CANCEL));
	}
	
	public void setPasswordLenght(int lenght) {
		passLenght = lenght;
	}
	
	@Override
	public Item showDialog(Object... params) {
		if(params.length > 0) {
			Item tmp = null;
			if(params[0] instanceof Item) {
				tmp = (Item) params[0];
			} else if(params[0] instanceof User) {
				tmp = new Item((User) params[0]);
			} else {
				LOG.error(getText(Labels.TITLE_ERROR), getException(Exceptions.ITEM_MOD_FAIL), null);
				return null;
			}
			item = tmp;
			service.setText(item.getService());
			username.setText(item.getUsername());
			password.setText(item.getPassword());
			comment.setText(item.getComment());
		}
		service.requestFocusInWindow();
		return showDialog();
	}

	@Override
	protected Item getResult() {
		if(item != null) {
			item.setService(service.getText());
			item.setUsername(username.getText());
			item.setPassword(password.getText());
			item.setComment(comment.getText());
		}
		return item;
	}

	@Override
	protected JButton getDefaultButton() {
		return btnOk;
	}
	
	private boolean isCorrect() {
		StringBuilder message = new StringBuilder();
		
		if(service.getText().isEmpty()) {
			message.append(getText(Labels.MESSAGES_SERVICE_EMPTY)).append("\n");
		}
		if(username.getText().isEmpty()) {
			message.append(getText(Labels.MESSAGES_USERNAME_EMPTY)).append("\n");
		}
		
		if(message.length() > 0) {
			JOptionPane.showMessageDialog(null, message, getText(Labels.TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
	
}
