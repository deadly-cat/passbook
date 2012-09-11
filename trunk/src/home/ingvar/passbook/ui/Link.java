package home.ingvar.passbook.ui;

import home.ingvar.passbook.lang.Labels;
import home.ingvar.passbook.utils.I18n;
import home.ingvar.passbook.utils.LOG;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;

import javax.swing.JLabel;

public class Link extends JLabel {

	private static final long serialVersionUID = 1L;
	private static final String TMP = "<HTML><a href='#'>%s</a></HTML>";
	private URI uri;
	
	public Link(URI uri) {
		this(uri, uri.toString());
		addMouseListener(new Listener());
	}
	
	public Link(URI uri, String title) {
		this.uri = uri;
		setTitle(title);
	}
	
	public void setTitle(String title) {
		setText(String.format(TMP, title));
	}
	
	public void setURI(URI uri) {
		this.uri = uri;
	}
	
	public URI getURI() {
		return uri;
	}
	
	public void open() throws IOException {
		if(Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
			Desktop.getDesktop().browse(uri);
		}
	}
	
	private class Listener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent event) {
			try {
				open();
			} catch (IOException e) {
				LOG.error(I18n.getInstance().get(Labels.TITLE_ERROR), e.getMessage(), e);
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {
			setCursor(new Cursor(Cursor.HAND_CURSOR));
		}

		@Override
		public void mouseExited(MouseEvent e) {
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		
	}

}
