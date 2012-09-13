package home.ingvar.passbook.ui.res;

import home.ingvar.passbook.utils.LOG;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public enum IMG {

	FAVICON("favicon.png"),
	LOGO("logo.png"),
	USER("user.png"),
	EXIT("exit.png"),
	ADD_ITEM("add.png"),
	EDIT_ITEM("edit.png"),
	DELETE_ITEM("delete.png"),
	DELETE_USER("delete_user.png"),
	PASSGEN("pass_gen.png");
	
	private static final String RES_PATH = "/home/ingvar/passbook/ui/res/";
	private static final BufferedImage NO_LOAD = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB) {
		{
			Graphics2D g2 = (Graphics2D) getGraphics();
			g2.setColor(Color.WHITE);
			g2.drawRect(0, 0, 100, 100);
			g2.setColor(Color.BLACK);
			g2.setStroke(new BasicStroke(20F));
			g2.drawLine(0, 0, 100, 100);
			g2.drawLine(0, 100, 100, 0);
		}
	};
	
	private BufferedImage img;
	
	IMG(String imageName) {
		try {
			img = ImageIO.read(getClass().getResource(RES_PATH + imageName));
		} catch (Exception e) {
			LOG.error("Image load", "Cannot load image " + imageName, e);
		}
	}
	
	public BufferedImage getImage() {
		return (img == null) ? NO_LOAD : img;
	}
	
}
