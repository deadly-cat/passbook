package home.ingvar.passbook.ui.dialogs;

import home.ingvar.passbook.lang.Labels;
import home.ingvar.passbook.ui.AbstractDialog;
import home.ingvar.passbook.ui.GBH;
import home.ingvar.passbook.ui.Link;
import home.ingvar.passbook.ui.MainFrame;
import home.ingvar.passbook.utils.LOG;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.UIManager;

public class ErrorDialog extends AbstractDialog<Object> {

	private static final long serialVersionUID = 1L;
	private static final String URL = "http://code.google.com/p/passbook/issues/entry";
	private static final String URI_TEMPLATE = "Defect report from user";
	private static final String URI_COMMENT = "What steps will reproduce the problem?\n1. \n2. \n3. \n\nWhat is the expected output? What do you see instead?\n\n\nPlease use labels and text to provide additional information.\n\n";
	
	private Icon icoError;
	private JLabel lblMessage;
	private Link link;
	private JButton btnOk;
	
	public ErrorDialog(MainFrame frame) {
		super(frame);
		icoError = UIManager.getIcon("OptionPane.errorIcon");
		lblMessage = new JLabel();
		btnOk = new JButton();
		
		try {
			link = new Link(createURI(""));
		} catch (Exception e) {/*not good*/}
		
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		
		setResizable(false);
		setLayout(new GridBagLayout());
		
		add(new JLabel(icoError), GBH.get().anchor(GBH.FIRST_LINE_START));
		add(lblMessage, GBH.get());
		add(new JLabel(), GBH.get(0, 0, 0, 0).width(GBH.REMAINDER));
		if(link != null) {
			add(link, GBH.get().width(2).anchor(GBH.LINE_END));
			add(new JLabel(), GBH.get(0, 0, 0, 0).width(GBH.REMAINDER));
		}
		add(btnOk, GBH.get().width(3).anchor(GBH.CENTER));
	}

	@Override
	public void updateI18n() {
		if(link != null) {
			link.setTitle(getText(Labels.LABELS_BUGLINK));
		}
		btnOk.setText(getText(Labels.BUTTONS_OK));
	}
	
	@Override
	public Object showDialog(Object... params) {
		if(params.length > 0) {
			String title = (String) params[0];
			String message = (String) params[1];
			Throwable exception = params.length > 2 ? (Throwable) params[2] : null;
			setTitle(title);
			setMessage(message, exception);
		}
		return showDialog();
	}

	@Override
	protected Object getResult() {
		return null;
	}

	@Override
	protected JButton getDefaultButton() {
		return btnOk;
	}
	
	private void setMessage(String message, Throwable exception) {
		lblMessage.setText("<html>" + message.replaceAll("\n", "<br/>") + "</html>");
		try {
			String m = message;
			if(exception != null) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				exception.printStackTrace(pw);
				m += "\n\nDetails:\n-----------------------------------------\n" + sw.toString();
			}
			link.setURI(createURI(m));
			if(!link.isVisible()) {
				link.setVisible(true);
			}
		} catch (UnsupportedEncodingException e) {
			LOG.error(getText(Labels.TITLE_ERROR), e.getMessage(), e);
			if(link != null) {
				link.setVisible(false);
			}
		} catch (URISyntaxException e) {
			LOG.error(getText(Labels.TITLE_ERROR), e.getMessage(), e);
			if(link != null) {
				link.setVisible(false);
			}
		}
	}
	
	private URI createURI(String comment) throws UnsupportedEncodingException, URISyntaxException {
		return new URI(URL + "?template=" + URLEncoder.encode(URI_TEMPLATE, "UTF-8") + "&comment=" + URLEncoder.encode(URI_COMMENT + comment, "UTF-8"));
	}
	
}
