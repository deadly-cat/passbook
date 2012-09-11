package home.ingvar.passbook.utils;

import home.ingvar.passbook.ui.views.ErrorDialog;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

public class LOG {

	private static final Logger log = Logger.getLogger("passbook");
	private static boolean isConsole;
	private static JFrame parentFrame;
	
	public static void error(String title, String message, Throwable e) {
		log.error(message, e);
		if(!isConsole) {
			ErrorDialog.show(parentFrame, title, message, e);
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
