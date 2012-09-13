package home.ingvar.passbook.ui.dialogs;

import home.ingvar.passbook.lang.Labels;
import home.ingvar.passbook.ui.AbstractDialog;
import home.ingvar.passbook.ui.GBH;
import home.ingvar.passbook.ui.Link;
import home.ingvar.passbook.ui.MainFrame;
import home.ingvar.passbook.ui.res.IMG;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class AboutDialog extends AbstractDialog<Object> {

	private static final long serialVersionUID = 1L;
	private static final int DEF_WIDTH = 400;
	private static final String AUTHOR = "Igor Zubenko";
	private static final String VERSION = "1.1.1";

	private BufferedImage imgLogo;
	private JTextArea txaAbout;
	private Link lnkReleases;
	private Link lnkTracker;
	private Link lnkLicense;
	private JScrollPane pnlLicense;
	private JTextArea txaLicense;
	private JButton btnOk;

	public AboutDialog(MainFrame frame) {
		super(frame);
		imgLogo = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		imgLogo.getGraphics().drawImage(IMG.LOGO.getImage(), 0, 0, imgLogo.getWidth(), imgLogo.getHeight(), null);
		txaAbout = new JTextArea();
		try {
			lnkReleases = new Link(new URI("http://code.google.com/p/passbook/downloads/list"));
			lnkTracker = new Link(new URI("http://code.google.com/p/passbook/issues/list"));
		} catch (URISyntaxException e) {/* not good */}
		lnkLicense = new Link(null, "License") {
			private static final long serialVersionUID = 1L;
			@Override
			public void browse() {
				pnlLicense.setVisible(!pnlLicense.isVisible());
				if(pnlLicense.isVisible()) {
					txaLicense.setCaretPosition(0);
				}
				pack();
			}
		};
		txaLicense = new JTextArea();
		pnlLicense = new JScrollPane(txaLicense);
		btnOk = new JButton();
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		txaAbout.setEditable(false);
		txaAbout.setOpaque(false);
		txaAbout.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
		txaAbout.setPreferredSize(new Dimension(DEF_WIDTH - imgLogo.getWidth(), 70));
		txaLicense.setEditable(false);
		txaLicense.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
		txaLicense.setLineWrap(true);
		txaLicense.setWrapStyleWord(true);
		pnlLicense.setPreferredSize(new Dimension(DEF_WIDTH, 200));

		setLayout(new GridBagLayout());

		add(new JLabel(new ImageIcon(imgLogo)), GBH.get().height(2).anchor(GBH.FIRST_LINE_START));
		add(txaAbout, GBH.get().width(3).fill(GBH.BOTH).weightx(1.0).anchor(GBH.FIRST_LINE_START));
		add(new JLabel(), GBH.get(0, 0, 0, 0).width(GBH.REMAINDER));
		add(lnkReleases, GBH.get().anchor(GBH.LINE_END).weightx(1.0));
		add(lnkTracker, GBH.get().anchor(GBH.LINE_END));
		add(lnkLicense, GBH.get().anchor(GBH.LINE_END));
		add(new JLabel(), GBH.get(0, 0, 0, 0).width(GBH.REMAINDER));
		add(pnlLicense, GBH.get().width(4).fill(GBH.BOTH).weighty(1.0));
		add(new JLabel(), GBH.get(0, 0, 0, 0).width(GBH.REMAINDER));
		add(btnOk, GBH.get().width(5).anchor(GBH.CENTER));
	}
	
	@Override
	public Object showDialog() {
		pnlLicense.setVisible(false);
		return super.showDialog();
	}

	@Override
	protected Object getResult() {
		return null;
	}

	@Override
	public void updateI18n() {
		setTitle(getText(Labels.TITLE_ABOUT));
		StringBuilder sb = new StringBuilder();
		sb.append(getText(Labels.LABELS_ABOUT_PROGRAM)).append("\n");
		sb.append(getText(Labels.LABELS_VERSION)).append(": ").append(VERSION).append("\n");
		sb.append(getText(Labels.LABELS_AUTHOR)).append(": ").append(AUTHOR);

		txaAbout.setText(sb.toString());
		lnkReleases.setTitle(getText(Labels.LABELS_RELEASES));
		lnkTracker.setTitle(getText(Labels.LABELS_TRACKER));
		lnkLicense.setTitle(getText(Labels.LABELS_LICENSE));
		txaLicense.setText(getLicense());
		btnOk.setText(getText(Labels.BUTTONS_OK));
	}

	@Override
	protected JButton getDefaultButton() {
		return btnOk;
	}

}
