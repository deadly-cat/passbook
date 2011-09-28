package home.ingvar.passbook.gui.views;

import home.ingvar.passbook.dao.ResultException;
import home.ingvar.passbook.transfer.Item;
import home.ingvar.passbook.utils.I18n;
import home.ingvar.passbook.gui.MainFrame;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

/**
 * @author ingvar
 * @version 0.5
 *
 */
public class ViewPanel extends I18nJPanel {

	private static final Logger logger = Logger.getLogger(ViewPanel.class);
	private static final long serialVersionUID = 1L;
	private final MainFrame frame;
	private final I18n i18n;
	private ItemsPanel panel;
	private JButton btnProfile;
	private JButton btnLogout;
	
	public ViewPanel(MainFrame frame) {
		this.frame = frame;
		this.i18n  = frame.getI18n();
		this.panel = new ItemsPanel(frame);
		this.btnProfile = new JButton();
		this.btnLogout  = new JButton();
		
		init();
		rei18n();
	}
	
	@Override
	public void rei18n() {
		//profile.setText(i18n.get("buttons.profile"));
		btnLogout.setText(i18n.get("buttons.logout"));
		panel.rei18n();
	}
	
	private void init() {
		setLayout(new BorderLayout());
		
		add(panel, BorderLayout.CENTER);
		JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		add(infoPanel, BorderLayout.NORTH);
		
		JLabel userLabel = new JLabel(frame.getUser().toString());
		userLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		infoPanel.add(userLabel);
		
		ImageIcon userIcon = new ImageIcon(ClassLoader.getSystemResource("home/ingvar/passbook/gui/resources/user.png"));
		ImageIcon exitIcon = new ImageIcon(ClassLoader.getSystemResource("home/ingvar/passbook/gui/resources/exit.png"));
		infoPanel.add(btnProfile);
		infoPanel.add(btnLogout);
		btnProfile.setIcon(userIcon);
		btnLogout.setIcon(exitIcon);
		
		btnProfile.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.nextView(new ProfilePanel(frame));
			}
		});
		btnLogout.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setUser(null);
				frame.nextView(new AuthPanel(frame));
			}
		});
		
		try {
			List<Item> items = frame.getItemDAO().list(frame.getUser());
			panel.loadItems(items);
		} catch(ResultException e) {
			logger.error(e);
			JOptionPane.showMessageDialog(frame, e, i18n.get("labels.error"), JOptionPane.ERROR_MESSAGE);
		}
	}
	
}
