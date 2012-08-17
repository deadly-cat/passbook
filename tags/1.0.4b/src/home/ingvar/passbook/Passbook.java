package home.ingvar.passbook;

import home.ingvar.passbook.console.Console;
import home.ingvar.passbook.gui.MainFrame;

import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

/**
 * @author ingvar
 * @version 0.2
 *
 */
public class Passbook {
	
	private static final Logger logger = Logger.getLogger(Passbook.class);

	/**
	 * @param args
	 * @throws NoSuchAlgorithmException 
	 * 
	 */
	public static void main(String[] args) {
		try {
			if(args.length > 0 && args[0].equals("console")) {
				new Console().program();
			} else {
				new MainFrame().setVisible(true);
			}
		} catch(Exception e) {
			logger.error("Initialization error", e);
		}
	}

}
