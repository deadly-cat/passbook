package home.ingvar.passbook;

import home.ingvar.passbook.console.Console;
import home.ingvar.passbook.ui.MainFrame;
import home.ingvar.passbook.utils.LOG;

import javax.swing.JFrame;

/**
 * @author ingvar
 * @version 0.2
 *
 */
public class Passbook {

	public static void main(String[] args) {
		boolean isConsole = args.length > 0 && args[0].equals("console");
		LOG.setConsole(isConsole);
		try {
			if(isConsole) {
				new Console().program();
			} else {
				JFrame frame = new MainFrame();
				LOG.setFrame(frame);
				frame.setVisible(true);
			}
		} catch(Exception e) {
			LOG.error("Initialization error", "Something wrong see log for details", e); //TODO: i18n
		}
	}

}
