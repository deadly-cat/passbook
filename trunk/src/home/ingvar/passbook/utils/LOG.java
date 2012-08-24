package home.ingvar.passbook.utils;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

public class LOG {

	private static final Logger log = Logger.getLogger("passbook");
	private static boolean isConsole;
	private static JFrame parentFrame;
	
	public static void error(String title, Object message, Throwable e) {
		log.error(message, e);
		if(!isConsole) {
			JOptionPane.showMessageDialog(parentFrame, message, title, JOptionPane.ERROR_MESSAGE); //TODO: show message this link to issue tracker
		}
	}
	
	public static void warn(String title, Object message, Throwable e) {
		log.warn(message, e);
		if(!isConsole) {
			JOptionPane.showMessageDialog(parentFrame, message, title, JOptionPane.WARNING_MESSAGE);
		}
	}
	
	public static void setConsole(boolean isCons) {
		isConsole = isCons;
	}
	
	public static void setFrame(JFrame frame) {
		parentFrame = frame;
	}
	
}
